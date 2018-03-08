package com.microsoft.identity.common.internal.cache;

import com.microsoft.identity.common.model_old.Account;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.AzureActiveDirectoryAccessToken;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.AzureActiveDirectoryAccount;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.AzureActiveDirectoryRefreshToken;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.AzureActiveDirectoryTokenResponse;
import com.microsoft.identity.common.internal.providers.oauth2.AccessToken;
import com.microsoft.identity.common.internal.providers.oauth2.AuthorizationRequest;
import com.microsoft.identity.common.internal.providers.oauth2.OAuth2Strategy;
import com.microsoft.identity.common.internal.providers.oauth2.RefreshToken;
import com.microsoft.identity.common.internal.providers.oauth2.TokenResponse;
import com.microsoft.identity.common.internal.util.DateUtilities;

import java.util.Date;

public class ADALTokenCacheItem {

    private ADALUserInfo mUserInfo;

    private String mResource;

    private String mAuthority;

    private String mClientId;

    private String mAccessToken;

    private String mRefreshtoken;

    private String mRawIdToken;

    /**
     * This time is GMT.
     */
    private Date mExpiresOn;

    private boolean mIsMultiResourceRefreshToken;

    private String mTenantId;

    private String mFamilyClientId;

    private Date mExtendedExpiresOn;

    private String mSpeRing;

    ADALTokenCacheItem(final ADALTokenCacheItem tokenCacheItem) {
        mAuthority = tokenCacheItem.getAuthority();
        mResource = tokenCacheItem.getResource();
        mClientId = tokenCacheItem.getClientId();
        mAccessToken = tokenCacheItem.getAccessToken();
        mRefreshtoken = tokenCacheItem.getRefreshToken();
        mRawIdToken = tokenCacheItem.getRawIdToken();
        mUserInfo = tokenCacheItem.getUserInfo();
        mExpiresOn = tokenCacheItem.getExpiresOn();
        mIsMultiResourceRefreshToken = tokenCacheItem.getIsMultiResourceRefreshToken();
        mTenantId = tokenCacheItem.getTenantId();
        mFamilyClientId = tokenCacheItem.getFamilyClientId();
        mExtendedExpiresOn = tokenCacheItem.getExtendedExpiresOn();
        mSpeRing = tokenCacheItem.getSpeRing();
    }

    ADALTokenCacheItem(OAuth2Strategy strategy, AuthorizationRequest request, TokenResponse response) {

        Account account = strategy.createAccount(response);
        String issuerCacheIdentifier = strategy.getIssuerCacheIdentifier(request);
        AccessToken accessToken = strategy.getAccessTokenFromResponse(response);
        RefreshToken refreshToken = strategy.getRefreshTokenFromResponse(response);

        mAuthority = issuerCacheIdentifier;
        mResource = request.getScope();
        mClientId = request.getClientId();
        mAccessToken = accessToken.getAccessToken();
        mRefreshtoken = refreshToken.getRefreshToken();
        mRawIdToken = response.getIdToken();
        if (account instanceof AzureActiveDirectoryAccount) {
            mUserInfo = new ADALUserInfo((AzureActiveDirectoryAccount) account);
            mTenantId = ((AzureActiveDirectoryAccount) account).getTenantId();
        }

        if (accessToken instanceof AzureActiveDirectoryAccessToken) {
            mExpiresOn = ((AzureActiveDirectoryAccessToken) accessToken).getExpiresOn();
            mExtendedExpiresOn = ((AzureActiveDirectoryAccessToken) accessToken).getExtendedExpiresOn();
        }

        if (refreshToken instanceof AzureActiveDirectoryRefreshToken) {
            mIsMultiResourceRefreshToken = true;
            mFamilyClientId = ((AzureActiveDirectoryRefreshToken) refreshToken).getFamilyId();
        }

        if (response instanceof AzureActiveDirectoryTokenResponse) {
            mSpeRing = ((AzureActiveDirectoryTokenResponse) response).getSpeRing();
        }

    }


    String getSpeRing() {
        return mSpeRing;
    }

    void setSpeRing(final String speRing) {
        mSpeRing = speRing;
    }

    /**
     * Get the user information.
     *
     * @return UserInfo object.
     */
    public ADALUserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * Set the user information.
     *
     * @param info UserInfo object which contains user information.
     */
    public void setUserInfo(ADALUserInfo info) {
        mUserInfo = info;
    }

    /**
     * Get the resource.
     *
     * @return resource String.
     */
    public String getResource() {
        return mResource;
    }

    /**
     * Set the resource.
     *
     * @param resource resource identifier.
     */
    public void setResource(String resource) {
        mResource = resource;
    }

    /**
     * Get the authority.
     *
     * @return authority url string.
     */
    public String getAuthority() {
        return mAuthority;
    }

    /**
     * Set the authority.
     *
     * @param authority String authority url.
     */
    public void setAuthority(String authority) {
        mAuthority = authority;
    }

    /**
     * Get the client identifier.
     *
     * @return client identifier string.
     */
    public String getClientId() {
        return mClientId;
    }

    /**
     * Set the client identifier.
     *
     * @param clientId client identifier string.
     */
    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    /**
     * Get the access token.
     *
     * @return the access token string.
     */
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * Set the access token string.
     *
     * @param accessToken the access token string.
     */
    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    /**
     * Get the refresh token string.
     *
     * @return the refresh token string.
     */
    public String getRefreshToken() {
        return mRefreshtoken;
    }

    /**
     * Set the fresh token string.
     *
     * @param refreshToken the refresh token string.
     */
    public void setRefreshToken(String refreshToken) {
        mRefreshtoken = refreshToken;
    }

    /**
     * Get the expire date.
     *
     * @return the time the token get expired.
     */
    public Date getExpiresOn() {
        return DateUtilities.createCopy(mExpiresOn);
    }

    /**
     * Set the expire date.
     *
     * @param expiresOn the expire time.
     */
    public void setExpiresOn(final Date expiresOn) {
        mExpiresOn = DateUtilities.createCopy(expiresOn);
    }

    /**
     * Get the multi-resource refresh token flag.
     *
     * @return true if the token is a multi-resource refresh token, else return false.
     */
    public boolean getIsMultiResourceRefreshToken() {
        return mIsMultiResourceRefreshToken;
    }

    /**
     * Set the multi-resource refresh token flag.
     *
     * @param isMultiResourceRefreshToken true if the token is a multi-resource refresh token.
     */
    public void setIsMultiResourceRefreshToken(boolean isMultiResourceRefreshToken) {
        mIsMultiResourceRefreshToken = isMultiResourceRefreshToken;
    }

    /**
     * Get tenant identifier.
     *
     * @return the tenant identifier string.
     */
    public String getTenantId() {
        return mTenantId;
    }

    /**
     * Set tenant identifier.
     *
     * @param tenantId the tenant identifier string.
     */
    public void setTenantId(String tenantId) {
        mTenantId = tenantId;
    }

    /**
     * Get raw ID token.
     *
     * @return raw ID token string.
     */
    public String getRawIdToken() {
        return mRawIdToken;
    }

    /**
     * Set raw ID token.
     *
     * @param rawIdToken raw ID token string.
     */
    public void setRawIdToken(String rawIdToken) {
        mRawIdToken = rawIdToken;
    }

    /**
     * Get family client identifier.
     *
     * @return the family client ID string.
     */
    public final String getFamilyClientId() {
        return mFamilyClientId;
    }

    /**
     * Set family client identifier.
     *
     * @param familyClientId the family client ID string.
     */
    public final void setFamilyClientId(final String familyClientId) {
        mFamilyClientId = familyClientId;
    }

    /**
     * Set the extended expired time.
     *
     * @param extendedExpiresOn extended expired date.
     */
    public final void setExtendedExpiresOn(final Date extendedExpiresOn) {
        mExtendedExpiresOn = DateUtilities.createCopy(extendedExpiresOn);
    }

    /**
     * Get the extended expired time.
     *
     * @return the extended expired date.
     */
    public final Date getExtendedExpiresOn() {
        return DateUtilities.createCopy(mExtendedExpiresOn);
    }

}
