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
package com.microsoft.identity.common.internal.broker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.microsoft.identity.common.internal.cache.ICacheRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulates the possible responses from the broker.  Both successful response and error response.
 */
public class BrokerResult implements Serializable {

    private class SerializedNames {
        static final String TENANT_PROFILE_CACHE_RECORDS = "tenant_profile_cache_records";
        static final String ACCESS_TOKEN = "access_token";
        static final String ID_TOKEN = "id_token";
        static final String REFRESH_TOKEN = "refresh_token";
        static final String HOME_ACCOUNT_ID = "home_account_id";
        static final String LOCAL_ACCOUNT_ID = "local_account_id";
        static final String USERNAME = "username";
        static final String CLIENT_ID = "client_id";
        static final String FAMILY_ID = "family_id";
        static final String SCOPES = "scopes";
        static final String TOKEN_TYPE = "token_type";
        static final String CLIENT_INFO = "client_info";
        static final String AUTHORITY = "authority";
        static final String ENVIRONMENT = "environment";
        static final String TENANT_ID = "tenant_id";
        static final String EXPIRES_ON = "expires_on";
        static final String EXTENDED_EXPIRES_ON = "ext_expires_on";
        static final String CACHED_AT = "cached_at";
        static final String REFRESH_TOKEN_AGE = "refresh_token_age";
        static final String SUCCESS = "success";

        // Error constants
        /**
         * Error code generated by the broker.
         */
        static final String BROKER_ERROR_CODE = "broker_error_code";

        /**
         * The message accompanying the error code generated by the broker.
         */
        static final String BROKER_ERROR_MESSAGE = "broker_error_message";

        /**
         * The Exception type thrown by the broker.
         */
        static final String BROKER_EXCEPTION_TYPE = "broker_exception_type";

        /**
         * The OAuth2 suberror.
         */
        static final String OAUTH_SUB_ERROR = "oauth_sub_error";
        static final String HTTP_RESPONSE_CODE = "http_response_code";
        static final String HTTP_HEADERS = "http_response_headers";
        static final String HTTP_RESPONSE_BODY = "http_response_body";

        // Telemetry & Diagnostics
        static final String CORRELATION_ID = "correlation_id";
        static final String SPE_RING = "spe_ring";
        static final String CLI_TELEM_ERRORCODE = "cli_telem_error_code";
        static final String CLI_TELEM_SUB_ERROR_CODE = "cli_telem_suberror_code";
    }

    private static final long serialVersionUID = 8606631820514878489L;

    // Success parameters
    /**
     * Access token from the response
     */
    @Nullable
    @SerializedName(SerializedNames.ACCESS_TOKEN)
    private String mAccessToken;

    /**
     * ID token from the response
     */
    @Nullable
    @SerializedName(SerializedNames.ID_TOKEN)
    private String mIdToken;

    /**
     * Refresh Token  from the response
     */
    @Nullable
    @SerializedName(SerializedNames.REFRESH_TOKEN)
    private String mRefreshToken;

    /**
     * Home account id of the user.
     */
    @Nullable
    @SerializedName(SerializedNames.HOME_ACCOUNT_ID)
    private String mHomeAccountId;

    /**
     * Local account id or user id of the User
     */
    @Nullable
    @SerializedName(SerializedNames.LOCAL_ACCOUNT_ID)
    private String mLocalAccountId;

    /**
     * Username of the User.
     */
    @Nullable
    @SerializedName(SerializedNames.USERNAME)
    private String mUserName;

    /**
     * Client id of the application
     */
    @Nullable
    @SerializedName(SerializedNames.CLIENT_ID)
    private String mClientId;

    /**
     * Information to uniquely identify the family that the client application belongs to.
     */
    @Nullable
    @SerializedName(SerializedNames.FAMILY_ID)
    private String mFamilyId;


    /**
     * Scopes requested
     */
    @Nullable
    @SerializedName(SerializedNames.SCOPES)
    private String mScope;

    /**
     * Token type from the response
     */
    @Nullable
    @SerializedName(SerializedNames.TOKEN_TYPE)
    private String mTokenType;

    /**
     * Client Info from the response
     */
    @Nullable
    @SerializedName(SerializedNames.CLIENT_INFO)
    private String mClientInfo;

    /**
     * Authority from the response.
     */
    @Nullable
    @SerializedName(SerializedNames.AUTHORITY)
    private String mAuthority;

    /**
     * Environment used to cache token.
     */
    @Nullable
    @SerializedName(SerializedNames.ENVIRONMENT)
    private String mEnvironment;

    /**
     * Tenant id
     */
    @Nullable
    @SerializedName(SerializedNames.TENANT_ID)
    private String mTenantId;

    /**
     * Expires on value for the token.
     */
    @Nullable
    @SerializedName(SerializedNames.EXPIRES_ON)
    private long mExpiresOn;

    /**
     * Extended expires on value for the token
     */
    @Nullable
    @SerializedName(SerializedNames.EXTENDED_EXPIRES_ON)
    private long mExtendedExpiresOn;

    /**
     * Access token cache at time in millis
     */
    @Nullable
    @SerializedName(SerializedNames.CACHED_AT)
    private long mCachedAt;

    /**
     * Client telemetry SPE ring
     */
    @Nullable
    @SerializedName(SerializedNames.SPE_RING)
    private String mSpeRing;

    /**
     * Refresh token age from client telemetry
     */
    @Nullable
    @SerializedName(SerializedNames.REFRESH_TOKEN_AGE)
    private String mRefreshTokenAge;

    /**
     * Boolean to indicate if the request succeeded without exceptions.
     */
    @NonNull
    @SerializedName(SerializedNames.SUCCESS)
    private boolean mSuccess;

    // Exception parameters

    /**
     * Error code corresponding to any of the {@link com.microsoft.identity.common.exception.ErrorStrings}
     */
    @Nullable
    @SerializedName(SerializedNames.BROKER_ERROR_CODE)
    private String mErrorCode;

    /**
     * Error message
     */
    @Nullable
    @SerializedName(SerializedNames.BROKER_ERROR_MESSAGE)
    private String mErrorMessage;

    /**
     * Correlation id of the request
     */
    @Nullable
    @SerializedName(SerializedNames.CORRELATION_ID)
    private String mCorrelationId;

    /**
     * Sub error code from the error response
     */
    @Nullable
    @SerializedName(SerializedNames.OAUTH_SUB_ERROR)
    private String mSubErrorCode;

    /**
     * Http Status code of the error response
     */
    @Nullable
    @SerializedName(SerializedNames.HTTP_RESPONSE_CODE)
    private int mHttpStatusCode;

    /**
     * Response headers or the error response in json format
     */
    @Nullable
    @SerializedName(SerializedNames.HTTP_HEADERS)
    private String mHttpResponseHeaders;

    /**
     * Response body of the error response
     */
    @Nullable
    @SerializedName(SerializedNames.HTTP_RESPONSE_BODY)
    private String mHttpResponseBody;

    /**
     * Client telemetry error code
     */
    @Nullable
    @SerializedName(SerializedNames.CLI_TELEM_ERRORCODE)
    private String mCliTelemErrorCode;

    /**
     * Client telemetry sub error code
     */
    @Nullable
    @SerializedName(SerializedNames.CLI_TELEM_SUB_ERROR_CODE)
    private String mCliTelemSubErrorCode;


    @Nullable
    @SerializedName(SerializedNames.TENANT_PROFILE_CACHE_RECORDS)
    private final List<ICacheRecord> mTenantProfileData;

    @Nullable
    @SerializedName(SerializedNames.BROKER_EXCEPTION_TYPE)
    private final String mExceptionType;

    private BrokerResult(@NonNull final Builder builder) {
        mAccessToken = builder.mAccessToken;
        mIdToken = builder.mIdToken;
        mRefreshToken = builder.mRefreshToken;
        mHomeAccountId = builder.mHomeAccountId;
        mLocalAccountId = builder.mLocalAccountId;
        mUserName = builder.mUserName;
        mTokenType = builder.mTokenType;
        mClientId = builder.mClientId;
        mFamilyId = builder.mFamilyId;
        mScope = builder.mScope;
        mClientInfo = builder.mClientInfo;
        mAuthority = builder.mAuthority;
        mEnvironment = builder.mEnvironment;
        mTenantId = builder.mTenantId;
        mExpiresOn = builder.mExpiresOn;
        mExtendedExpiresOn = builder.mExtendedExpiresOn;
        mCachedAt = builder.mCachedAt;
        mSpeRing = builder.mSpeRing;
        mRefreshTokenAge = builder.mRefreshTokenAge;
        mSuccess = builder.mSuccess;
        mTenantProfileData = builder.mTenantProfileData;

        mErrorCode = builder.mErrorCode;
        mErrorMessage = builder.mErrorMessage;
        mCorrelationId = builder.mCorrelationId;
        mSubErrorCode = builder.mSubErrorCode;
        mHttpStatusCode = builder.mHttpStatusCode;
        mHttpResponseBody = builder.mHttpResponseBody;
        mHttpResponseHeaders = builder.mHttpResponseHeaders;
        mCliTelemErrorCode = builder.mCliTelemErrorCode;
        mCliTelemSubErrorCode = builder.mCliTelemSubErrorCode;
        mExceptionType = builder.mExceptionType;
    }

    public String getExceptionType() {
        return mExceptionType;
    }

    public List<ICacheRecord> getTenantProfileData() {
        return mTenantProfileData;
    }

    public String getCliTelemSubErrorCode() {
        return mCliTelemSubErrorCode;
    }

    public String getCliTelemErrorCode() {
        return mCliTelemErrorCode;
    }

    public String getHttpResponseBody() {
        return mHttpResponseBody;
    }

    public String getHttpResponseHeaders() {
        return mHttpResponseHeaders;
    }

    public int getHttpStatusCode() {
        return mHttpStatusCode;
    }

    public String getSubErrorCode() {
        return mSubErrorCode;
    }

    public String getCorrelationId() {
        return mCorrelationId;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getErrorCode() {
        return mErrorCode;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public String getRefreshTokenAge() {
        return mRefreshTokenAge;
    }

    public String getSpeRing() {
        return mSpeRing;
    }

    public long getCachedAt() {
        return mCachedAt;
    }

    public long getExtendedExpiresOn() {
        return mExtendedExpiresOn;
    }

    public long getExpiresOn() {
        return mExpiresOn;
    }

    public String getTenantId() {
        return mTenantId;
    }

    public String getEnvironment() {
        return mEnvironment;
    }

    public String getAuthority() {
        return mAuthority;
    }

    public String getClientInfo() {
        return mClientInfo;
    }

    public String getClientId() {
        return mClientId;
    }

    public String getFamilyId() {
        return mFamilyId;
    }

    public String getScope() {
        return mScope;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getLocalAccountId() {
        return mLocalAccountId;
    }

    public String getHomeAccountId() {
        return mHomeAccountId;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public String getIdToken() {
        return mIdToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public static class Builder {
        private String mAccessToken;
        private String mIdToken;
        private String mRefreshToken;
        private String mHomeAccountId;
        private String mLocalAccountId;
        private String mUserName;
        private String mTokenType;
        private String mClientId;
        private String mFamilyId;
        private String mScope;
        private String mClientInfo;
        private String mAuthority;
        private String mEnvironment;
        private String mTenantId;
        private long mExpiresOn;
        private long mExtendedExpiresOn;
        private long mCachedAt;
        private String mSpeRing;
        private String mRefreshTokenAge;
        private boolean mSuccess;
        private String mNegotiatedBrokerProtocolVersion;
        private List<ICacheRecord> mTenantProfileData;

        // Exception parameters
        private String mErrorCode;
        private String mErrorMessage;
        private String mCorrelationId;
        private String mSubErrorCode;
        private int mHttpStatusCode;
        private String mHttpResponseHeaders;
        private String mHttpResponseBody;
        private String mCliTelemErrorCode;
        private String mCliTelemSubErrorCode;
        private String mExceptionType;

        public Builder accessToken(@Nullable final String accessToken) {
            this.mAccessToken = accessToken;
            return this;
        }

        public Builder idToken(@Nullable final String idToken) {
            this.mIdToken = idToken;
            return this;
        }

        public Builder refreshToken(@Nullable final String refreshToken) {
            this.mRefreshToken = refreshToken;
            return this;
        }

        public Builder homeAccountId(@Nullable final String homeAccountId) {
            this.mHomeAccountId = homeAccountId;
            return this;
        }

        public Builder localAccountId(@Nullable final String localAccountId) {
            this.mLocalAccountId = localAccountId;
            return this;
        }

        public Builder userName(@Nullable final String userName) {
            this.mUserName = userName;
            return this;
        }

        public Builder tokenType(@Nullable final String tokenType) {
            this.mTokenType = tokenType;
            return this;
        }

        public Builder clientId(@Nullable final String clientId) {
            this.mClientId = clientId;
            return this;
        }

        public Builder familyId(@Nullable final String familyId) {
            this.mFamilyId = familyId;
            return this;
        }

        public Builder scope(@Nullable final String scope) {
            this.mScope = scope;
            return this;
        }

        public Builder clientInfo(@Nullable final String clientInfo) {
            this.mClientInfo = clientInfo;
            return this;
        }

        public Builder authority(@Nullable final String authority) {
            this.mAuthority = authority;
            return this;
        }

        public Builder environment(@Nullable final String environment) {
            this.mEnvironment = environment;
            return this;
        }

        public Builder tenantId(@Nullable final String tenantId) {
            this.mTenantId = tenantId;
            return this;
        }

        public Builder expiresOn(long mExpiresOn) {
            this.mExpiresOn = mExpiresOn;
            return this;
        }

        public Builder extendedExpiresOn(long extendedExpiresOn) {
            this.mExtendedExpiresOn = extendedExpiresOn;
            return this;
        }

        public Builder cachedAt(long cachedAt) {
            this.mCachedAt = cachedAt;
            return this;
        }

        public Builder speRing(final String speRing) {
            this.mSpeRing = speRing;
            return this;
        }

        public Builder refreshTokenAge(final String refreshTokenAge) {
            this.mRefreshTokenAge = refreshTokenAge;
            return this;
        }

        public Builder success(boolean success) {
            this.mSuccess = success;
            return this;
        }

        public Builder negotiatedBrokerProtocolVersion(final String negotiatedBrokerProtocolVersion) {
            this.mNegotiatedBrokerProtocolVersion = negotiatedBrokerProtocolVersion;
            return this;
        }

        public Builder errorCode(final String errorCode) {
            this.mErrorCode = errorCode;
            return this;
        }

        public Builder errorMessage(final String errorMessage) {
            this.mErrorMessage = errorMessage;
            return this;
        }

        public Builder correlationId(final String correlationId) {
            this.mCorrelationId = correlationId;
            return this;
        }

        public Builder oauthSubErrorCode(final String subErrorCode) {
            this.mSubErrorCode = subErrorCode;
            return this;
        }

        public Builder httpStatusCode(int httpStatusCode) {
            this.mHttpStatusCode = httpStatusCode;
            return this;
        }

        public Builder httpResponseHeaders(final String httpResponseHeaders) {
            this.mHttpResponseHeaders = httpResponseHeaders;
            return this;
        }

        public Builder httpResponseBody(final String httpResponseBody) {
            this.mHttpResponseBody = httpResponseBody;
            return this;
        }

        public Builder cliTelemErrorCode(final String cliTelemErrorCode) {
            this.mCliTelemErrorCode = cliTelemErrorCode;
            return this;
        }

        public Builder cliTelemSubErrorCode(final String cliTelemSubErrorCode) {
            this.mCliTelemSubErrorCode = cliTelemSubErrorCode;
            return this;
        }

        public BrokerResult build() {
            return new BrokerResult(this);
        }

        public Builder tenantProfileRecords(final List<ICacheRecord> cacheRecordWithTenantProfileData) {
            this.mTenantProfileData = cacheRecordWithTenantProfileData;
            return this;
        }

        public Builder exceptionType(String exceptionType) {
            this.mExceptionType = exceptionType;
            return this;
        }
    }

}
