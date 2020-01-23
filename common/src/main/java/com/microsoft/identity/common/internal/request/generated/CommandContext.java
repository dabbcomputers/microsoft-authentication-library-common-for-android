package com.microsoft.identity.common.internal.request.generated;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.microsoft.identity.common.internal.request.SdkType;

public abstract class CommandContext {
    @Nullable
    public abstract String correlationId();

    @Nullable
    public abstract String applicationName();

    @Nullable
    public abstract String applicationVersion();

    @Nullable
    public abstract String requiredBrokerProtocolVersion();

    @NonNull
    public abstract SdkType sdkType(); //Need a default value

    @NonNull
    public abstract String sdkVersion();


}
