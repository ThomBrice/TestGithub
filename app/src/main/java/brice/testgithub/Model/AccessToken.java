/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.Model;


import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope(){
        return scope;
    }
}
