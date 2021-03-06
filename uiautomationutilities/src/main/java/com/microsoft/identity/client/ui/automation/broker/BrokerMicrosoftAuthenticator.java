//  Copyright (c) Microsoft Corporation.
//  All rights reserved.
//
//  This code is licensed under the MIT License.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files(the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions :
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
package com.microsoft.identity.client.ui.automation.broker;

import android.Manifest;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.microsoft.identity.client.ui.automation.interaction.PromptHandlerParameters;
import com.microsoft.identity.client.ui.automation.interaction.PromptParameter;
import com.microsoft.identity.client.ui.automation.interaction.microsoftsts.AadPromptHandler;
import com.microsoft.identity.client.ui.automation.utils.CommonUtils;
import com.microsoft.identity.client.ui.automation.utils.UiAutomatorUtils;

import org.junit.Assert;

import lombok.Getter;

import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.FIND_UI_ELEMENT_TIMEOUT;

/**
 * A model for interacting with the Microsoft Authenticator Broker App during UI Test.
 */
@Getter
public class BrokerMicrosoftAuthenticator extends AbstractTestBroker implements ITestBroker {

    public final static String AUTHENTICATOR_APP_PACKAGE_NAME = "com.azure.authenticator";
    public final static String AUTHENTICATOR_APP_NAME = "Microsoft Authenticator";
    public final static String AUTHENTICATOR_APK = "Authenticator.apk";

    public static final String TAG = BrokerMicrosoftAuthenticator.class.getSimpleName();

    public BrokerMicrosoftAuthenticator() {
        super(AUTHENTICATOR_APP_PACKAGE_NAME, AUTHENTICATOR_APP_NAME);
        localApkFileName = AUTHENTICATOR_APK;
    }

    @Override
    public void performDeviceRegistration(@NonNull final String username,
                                          @NonNull final String password) {
        performDeviceRegistrationHelper(
                username,
                password,
                "com.azure.authenticator:id/manage_device_registration_email_input",
                "com.azure.authenticator:id/manage_device_registration_register_button"
        );


        try {
            // after device registration, make sure that we see the unregister btn to confirm successful
            // registration
            final UiObject unRegisterBtn = UiAutomatorUtils.obtainUiObjectWithResourceId(
                    "com.azure.authenticator:id/manage_device_registration_unregister_button"
            );
            Assert.assertTrue(
                    "Microsoft Authenticator - Unregister Button appears.",
                    unRegisterBtn.exists()
            );

            Assert.assertTrue(
                    "Microsoft Authenticator - Unregister Button is clickable.",
                    unRegisterBtn.isClickable()
            );

            // after device registration, make sure that the current registration upn matches with
            // with what was passed in
            final UiObject currentRegistration = UiAutomatorUtils.obtainUiObjectWithResourceId(
                    "com.azure.authenticator:id/manage_device_registration_current_registered_email"
            );

            Assert.assertTrue(
                    "Microsoft Authenticator - Registered account info appears.",
                    currentRegistration.exists()
            );

            Assert.assertTrue(
                    "Microsoft Authenticator - Registered account upn matches provided upn.",
                    currentRegistration.getText().equalsIgnoreCase(username)
            );
        } catch (final UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Override
    public void performSharedDeviceRegistration(@NonNull final String username,
                                                @NonNull final String password) {
        performDeviceRegistrationHelper(
                username,
                password,
                "com.azure.authenticator:id/shared_device_registration_email_input",
                "com.azure.authenticator:id/shared_device_registration_button"
        );

        final UiDevice device =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        final UiSelector sharedDeviceConfirmationSelector = new UiSelector()
                .descriptionContains("Shared Device Mode")
                .className("android.widget.ImageView");

        //confirm that we are in Shared Device Mode inside Authenticator
        final UiObject sharedDeviceConfirmation = device.findObject(sharedDeviceConfirmationSelector);
        sharedDeviceConfirmation.waitForExists(FIND_UI_ELEMENT_TIMEOUT);
        Assert.assertTrue(
                "Microsoft Authenticator - Shared Device Confirmation page appears.",
                sharedDeviceConfirmation.exists());
    }

    @Nullable
    @Override
    public String obtainDeviceId() {
        openDeviceRegistrationPage();

        try {
            final UiObject deviceIdElement = UiAutomatorUtils.obtainUiObjectWithResourceId(
                    "com.azure.authenticator:id/device_id_text"
            );

            final String deviceIdText = deviceIdElement.getText();
            final int colonIndex = deviceIdText.indexOf(":");
            return deviceIdText.substring(colonIndex + 1);
        } catch (final UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    @Override
    public void enableBrowserAccess() {
        // open device registration page
        openDeviceRegistrationPage();

        // Click enable browser access
        UiAutomatorUtils.handleButtonClick(
                "com.azure.authenticator:id/manage_device_registration_enable_browser_access_button"
        );

        // click continue in Dialog
        UiAutomatorUtils.handleButtonClick("android:id/button1");

        final UiDevice device =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Install cert
        final UiObject certInstaller = device.findObject(new UiSelector().packageName("com.android.certinstaller"));
        certInstaller.waitForExists(FIND_UI_ELEMENT_TIMEOUT);
        Assert.assertTrue(
                "Microsoft Authenticator - cert installer dialog appears.",
                certInstaller.exists()
        );

        UiAutomatorUtils.handleButtonClick("android:id/button1");
    }

    @Override
    public void createPowerLiftIncident() {
        launch();
        if (shouldHandleFirstRun) {
            handleFirstRun();
        }

        // click the 3 dot menu icon in top right
        UiAutomatorUtils.handleButtonClick("com.azure.authenticator:id/menu_overflow");

        try {
            // select Help from drop down
            final UiObject settings = UiAutomatorUtils.obtainUiObjectWithText("Help");
            settings.click();

            // scroll down the recycler view to find Send logs btn
            final UiObject sendLogs = UiAutomatorUtils.obtainChildInScrollable(
                    android.widget.ScrollView.class,
                    "Send logs"
            );

            assert sendLogs != null;

            // click the send logs button
            sendLogs.click();

            final UiObject sendLogMsgField = UiAutomatorUtils.obtainUiObjectWithClassAndIndex(
                    EditText.class,
                    1
            );

            sendLogMsgField.setText("Broker Automation Incident");

            final UiObject sendBtn = UiAutomatorUtils.obtainEnabledUiObjectWithExactText(
                    "SEND"
            );
            sendBtn.click();

            final UiObject postLogSubmissionMsg = UiAutomatorUtils.obtainUiObjectWithClassAndIndex(
                    TextView.class,
                    3
            );

            Assert.assertTrue(postLogSubmissionMsg.exists());

            // This will post the incident id in text logs
            Log.i(TAG, postLogSubmissionMsg.getText());
        } catch (UiObjectNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Open the device registration page in the Authenticator App
     */
    public void openDeviceRegistrationPage() {
        launch(); // launch Authenticator app

        if (shouldHandleFirstRun) {
            handleFirstRun(); // handle first run experience
        }

        // click the 3 dot menu icon in top right
        UiAutomatorUtils.handleButtonClick("com.azure.authenticator:id/menu_overflow");

        try {
            // select Settings from drop down
            final UiObject settings = UiAutomatorUtils.obtainUiObjectWithText("Settings");
            settings.click();

            // scroll down the recycler view to find device registration btn
            final UiObject deviceRegistration = UiAutomatorUtils.obtainChildInScrollable(
                    "com.azure.authenticator:id/recycler_view",
                    "Device registration"
            );

            assert deviceRegistration != null;

            // click the device registration button
            deviceRegistration.click();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                // grant the GET ACCOUNTS permission if needed
                grantPermission(Manifest.permission.GET_ACCOUNTS);
            }
        } catch (final UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void performDeviceRegistrationHelper(@NonNull final String username,
                                                 @NonNull final String password,
                                                 @NonNull final String emailInputResourceId,
                                                 @NonNull final String registerBtnResourceId) {
        // open device registration page
        openDeviceRegistrationPage();

        // enter email
        UiAutomatorUtils.handleInput(
                emailInputResourceId,
                username
        );

        // click register
        UiAutomatorUtils.handleButtonClick(registerBtnResourceId);

        final PromptHandlerParameters promptHandlerParameters = PromptHandlerParameters.builder()
                .prompt(PromptParameter.LOGIN)
                .broker(this)
                .consentPageExpected(false)
                .expectingBrokerAccountChooserActivity(false)
                .expectingLoginPageAccountPicker(false)
                .sessionExpected(false)
                .loginHint(username)
                .build();

        final AadPromptHandler aadPromptHandler = new AadPromptHandler(promptHandlerParameters);

        // handle AAD login page
        aadPromptHandler.handlePrompt(username, password);
    }

    @Override
    public void handleFirstRun() {
        final String skipButtonResourceId = CommonUtils.getResourceId(
                AUTHENTICATOR_APP_PACKAGE_NAME, "frx_slide_skip_button"
        );
        UiAutomatorUtils.handleButtonClick("android:id/button1");
        // the skip button is actually rendered 3 times in the swipe/slide view
        UiAutomatorUtils.handleButtonClick(skipButtonResourceId);
        UiAutomatorUtils.handleButtonClick(skipButtonResourceId);
        UiAutomatorUtils.handleButtonClick(skipButtonResourceId);
        shouldHandleFirstRun = false;
    }
}
