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
package com.microsoft.identity.client.ui.automation.app;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.ui.automation.installer.IAppInstaller;
import com.microsoft.identity.client.ui.automation.installer.LocalApkInstaller;
import com.microsoft.identity.client.ui.automation.installer.PlayStore;
import com.microsoft.identity.client.ui.automation.utils.AdbShellUtils;
import com.microsoft.identity.client.ui.automation.utils.CommonUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * A model for interacting with an app during a UI Test. We can interact with this app during the
 * test by performing specific operation on/with it.
 */
@Getter
public abstract class App implements IApp {

    @Setter
    private IAppInstaller appInstaller;

    private final String packageName;

    @Setter
    private String appName;

    protected String localApkFileName = null;

    /**
     * Indicates whether the first run experience should be handled in the UI. This value can
     * (should) be changed to false by child classes as appropriate. Usually if the app is launched
     * for the first time, then it makes sense for this value to be true and first run experience
     * to be handled, and this value to be set to false for subsequent launches of the app.
     */
    protected boolean shouldHandleFirstRun = true;

    public App(@NonNull final String packageName) {
        this.packageName = packageName;
        this.appInstaller = new PlayStore();
    }

    public App(@NonNull final String packageName, @NonNull final String appName) {
        this(packageName);
        this.appName = appName;
    }

    public App(@NonNull final String packageName, @NonNull final IAppInstaller appInstaller) {
        this.appInstaller = appInstaller;
        this.packageName = packageName;
    }

    public App(@NonNull final String packageName,
               @NonNull final String appName,
               @NonNull final IAppInstaller appInstaller) {
        this.appInstaller = appInstaller;
        this.packageName = packageName;
        this.appName = appName;
    }

    @Override
    public void install() {
        //TODO: make it build time configurable to specify the installer that should be used.
        // Ideally we can specify different installers on app basis
        if (appInstaller instanceof LocalApkInstaller && !TextUtils.isEmpty(localApkFileName)) {
            appInstaller.installApp(localApkFileName);
        } else {
            appInstaller.installApp(packageName);
        }

        // the app is just installed, first run should be handled
        // this value can (should) be changed to false by child classes as appropriate
        shouldHandleFirstRun = true;
    }

    @Override
    public void launch() {
        CommonUtils.launchApp(packageName);
    }

    @Override
    public void clear() {
        AdbShellUtils.clearPackage(packageName);
    }

    @Override
    public void uninstall() {
        AdbShellUtils.removePackage(packageName);
        shouldHandleFirstRun = true;
    }

    @Override
    public boolean hasPermission(@NonNull final String permission) {
        return CommonUtils.hasPermission(packageName, permission);
    }

    @Override
    public void grantPermission(@NonNull final String permission) {
        if (!hasPermission(permission)) {
            CommonUtils.grantPackagePermission();
        }
    }

    @Override
    public void forceStop() {
        AdbShellUtils.forceStopPackage(packageName);
    }

    @Override
    public boolean isInstalled() {
        return CommonUtils.isPackageInstalled(getPackageName());
    }
}
