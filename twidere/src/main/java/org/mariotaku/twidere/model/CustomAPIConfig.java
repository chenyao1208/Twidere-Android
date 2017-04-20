package org.mariotaku.twidere.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import org.mariotaku.restfu.RestFuUtils;
import org.mariotaku.twidere.R;
import org.mariotaku.twidere.annotation.AccountType;
import org.mariotaku.twidere.model.account.cred.Credentials;
import org.mariotaku.twidere.util.JsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.mariotaku.twidere.TwidereConstants.DEFAULT_TWITTER_API_URL_FORMAT;
import static org.mariotaku.twidere.TwidereConstants.TWITTER_CONSUMER_KEY;
import static org.mariotaku.twidere.TwidereConstants.TWITTER_CONSUMER_SECRET;

/**
 * Created by mariotaku on 16/3/12.
 */
@ParcelablePlease
@JsonObject
public final class CustomAPIConfig implements Parcelable {

    @JsonField(name = "name")
    String name;
    @AccountType
    @JsonField(name = "type")
    @Nullable
    String type;
    @JsonField(name = "localized_name")
    String localizedName;
    @JsonField(name = "api_url_format")
    @Nullable
    String apiUrlFormat;
    @Credentials.Type
    @JsonField(name = "auth_type")
    String credentialsType;
    @JsonField(name = "same_oauth_url")
    boolean sameOAuthUrl;
    @JsonField(name = "no_version_suffix")
    boolean noVersionSuffix;
    @JsonField(name = "consumer_key")
    @Nullable
    String consumerKey;
    @JsonField(name = "consumer_secret")
    @Nullable
    String consumerSecret;
    @Nullable
    @JsonField(name = "sign_up_url")
    String signUpUrl;

    public CustomAPIConfig() {
    }

    public CustomAPIConfig(@NonNull String name, @Nullable String type, @Nullable String apiUrlFormat,
            String credentialsType, boolean sameOAuthUrl, boolean noVersionSuffix,
            @Nullable String consumerKey, @Nullable String consumerSecret) {
        this.name = name;
        this.type = type;
        this.apiUrlFormat = apiUrlFormat;
        this.credentialsType = credentialsType;
        this.sameOAuthUrl = sameOAuthUrl;
        this.noVersionSuffix = noVersionSuffix;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalizedName(Context context) {
        if (localizedName == null) return name;
        final Resources res = context.getResources();
        int id = res.getIdentifier(localizedName, "string", context.getPackageName());
        if (id != 0) {
            return res.getString(id);
        }
        return name;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    @Nullable
    public String getApiUrlFormat() {
        return apiUrlFormat;
    }

    public String getCredentialsType() {
        return credentialsType;
    }

    public boolean isSameOAuthUrl() {
        return sameOAuthUrl;
    }

    public boolean isNoVersionSuffix() {
        return noVersionSuffix;
    }

    @Nullable
    public String getConsumerKey() {
        return consumerKey;
    }

    @Nullable
    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setApiUrlFormat(@Nullable String apiUrlFormat) {
        this.apiUrlFormat = apiUrlFormat;
    }

    public void setConsumerKey(@Nullable String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumerSecret(@Nullable String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setCredentialsType(String credentialsType) {
        this.credentialsType = credentialsType;
    }

    public void setSameOAuthUrl(boolean sameOAuthUrl) {
        this.sameOAuthUrl = sameOAuthUrl;
    }

    public void setNoVersionSuffix(boolean noVersionSuffix) {
        this.noVersionSuffix = noVersionSuffix;
    }

    @Nullable
    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(@Nullable String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        CustomAPIConfigParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<CustomAPIConfig> CREATOR = new Creator<CustomAPIConfig>() {
        public CustomAPIConfig createFromParcel(Parcel source) {
            CustomAPIConfig target = new CustomAPIConfig();
            CustomAPIConfigParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public CustomAPIConfig[] newArray(int size) {
            return new CustomAPIConfig[size];
        }
    };

    @NonNull
    public static List<CustomAPIConfig> listDefault(@NonNull Context context) {
        final AssetManager assets = context.getAssets();
        InputStream is = null;
        try {
            is = assets.open("data/default_api_configs.json");
            return JsonSerializer.parseList(is, CustomAPIConfig.class);
        } catch (IOException e) {
            return listBuiltin(context);
        } finally {
            RestFuUtils.closeSilently(is);
        }
    }

    public static CustomAPIConfig builtin(@NonNull Context context) {
        return new CustomAPIConfig(context.getString(R.string.provider_default), AccountType.TWITTER,
                DEFAULT_TWITTER_API_URL_FORMAT, Credentials.Type.OAUTH, true, false,
                TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
    }

    public static CustomAPIConfig mastodon(@NonNull Context context) {
        return new CustomAPIConfig(context.getString(R.string.provider_mastodon), AccountType.MASTODON,
                null, Credentials.Type.OAUTH2, true, true, null, null);
    }

    public static List<CustomAPIConfig> listBuiltin(@NonNull Context context) {
        return Collections.singletonList(builtin(context));
    }
}
