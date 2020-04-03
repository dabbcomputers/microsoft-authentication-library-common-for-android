// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.common.internal.commands;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.microsoft.identity.common.internal.commands.parameters.TokenCommandParameters;
import com.microsoft.identity.common.internal.controllers.BaseController;
import com.microsoft.identity.common.internal.result.AcquireTokenResult;

import java.util.List;

public abstract class TokenCommand extends BaseCommand<AcquireTokenResult> {

    public TokenCommand(@NonNull TokenCommandParameters parameters, @NonNull BaseController controller, @NonNull CommandCallback callback, @NonNull String publicApiId) {
        super(parameters, controller, callback, publicApiId);
    }

    public TokenCommand(@NonNull TokenCommandParameters parameters, @NonNull List<BaseController> controllers, @NonNull CommandCallback callback, @NonNull String publicApiId) {
        super(parameters, controllers, callback, publicApiId);
    }

    abstract void notify(int requestCode, int resultCode, final Intent data);
}
