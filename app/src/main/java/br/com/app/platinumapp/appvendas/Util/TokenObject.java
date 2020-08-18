package br.com.app.platinumapp.appvendas.Util;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JAVA on 03/09/2017.
 */

public class TokenObject {

    @SerializedName("token")
    private String token;
    public TokenObject(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
