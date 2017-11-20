/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.Model;


import android.content.Context;
import android.content.SharedPreferences;

import brice.testgithub.R;

public class TokenStore {

    //preferences key
    private static final String KEY = "token";

    //instance of the token
    private static TokenStore instance;

    //sharedPreferences to register the token value
    private SharedPreferences sharedPreferences;

    private TokenStore(Context context){
        sharedPreferences = context.getSharedPreferences(
                String.valueOf(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    public static TokenStore getInstance(Context context){
        if (instance == null)
            instance = new TokenStore(context);

        return instance;
    }

    public String getToken(){
        return sharedPreferences.getString(KEY, null);
    }

    public void saveToken(String token){
        sharedPreferences.edit().putString(KEY, token).apply();
    }
}
