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
package com.microsoft.identity.client.ui.automation.installer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.FIND_UI_ELEMENT_TIMEOUT;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.getResourceId;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.isStringPackageName;
import static com.microsoft.identity.client.ui.automation.utils.CommonUtils.launchApp;

public class PlayStore implements IAppInstaller {

    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    // wait at least 5 mins for app installation from Play Store
    private static final long PLAY_STORE_INSTALL_APP_TIMEOUT = TimeUnit.MINUTES.toMillis(5);

    private void launchMarketPageForPackage(final String appPackageName) {
        final Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)); //sets the intent to start your app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);  //clear out any previous task, i.e., make sure it starts on the initial screen
        context.startActivity(intent);
    }

    private void searchAppOnGooglePlay(@NonNull final String hint) {
        final UiDevice device = UiDevice.getInstance(getInstrumentation());

        launchApp(GOOGLE_PLAY_PACKAGE_NAME);

        final UiObject searchButton = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "search_bar_hint")
        ));
        try {
            searchButton.waitForExists(FIND_UI_ELEMENT_TIMEOUT);
            searchButton.click();
        } catch (final UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        final UiObject searchTextField = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "search_bar_text_input")
        ));
        try {
            searchTextField.waitForExists(FIND_UI_ELEMENT_TIMEOUT);
            searchTextField.setText(hint);
        } catch (final UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        device.pressEnter();
    }

    private void selectGooglePlayAppFromAppList() throws UiObjectNotFoundException {
        final UiDevice device = UiDevice.getInstance(getInstrumentation());

        // we will just take the first app in the list
        final UiObject appIconInSearchResult = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "bucket_items")
        ));

        appIconInSearchResult.waitForExists(FIND_UI_ELEMENT_TIMEOUT);
        appIconInSearchResult.click();
    }

    private void selectGooglePlayAppFromInstallBar() throws UiObjectNotFoundException {
        final UiDevice device = UiDevice.getInstance(getInstrumentation());
        final UiObject appInstallBar = device.findObject(new UiSelector().resourceId(
                getResourceId(GOOGLE_PLAY_PACKAGE_NAME, "install_bar")
        ));
        appInstallBar.waitForExists(FIND_UI_ELEMENT_TIMEOUT);
        appInstallBar.click();
    }

    private void selectGooglePlayAppFromAppName() {
        try {
            selectGooglePlayAppFromInstallBar();
        } catch (final UiObjectNotFoundException e) {
            try {
                selectGooglePlayAppFromAppList();
            } catch (final UiObjectNotFoundException ex) {
                Assert.fail(ex.getMessage());
            }
        }
    }

    private void installAppFromMarketPage() {
        final UiDevice device = UiDevice.getInstance(getInstrumentation());

        try {
            final UiObject installBtn = device.findObject(
                    new UiSelector().className(Button.class).text("Install").enabled(true)
            );

            installBtn.waitForExists(FIND_UI_ELEMENT_TIMEOUT);

            installBtn.click();

            final UiObject openButton = device.findObject(
                    new UiSelector().className(Button.class).text("Open").enabled(true)
            );

            // if we see open button, then we know that the installation is complete
            openButton.waitForExists(PLAY_STORE_INSTALL_APP_TIMEOUT);
        } catch (final UiObjectNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Override
    public void installApp(@NonNull final String searchHint) {
        if (isStringPackageName(searchHint)) {
            launchMarketPageForPackage(searchHint);
        } else {
            searchAppOnGooglePlay(searchHint);
            selectGooglePlayAppFromAppName();
        }

        installAppFromMarketPage();
    }

}
