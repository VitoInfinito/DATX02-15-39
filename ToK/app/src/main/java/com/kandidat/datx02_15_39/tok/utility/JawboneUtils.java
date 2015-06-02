package com.kandidat.datx02_15_39.tok.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.api.response.OauthAccessTokenResponse;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.kandidat.datx02_15_39.tok.model.account.Account;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Class used for all utility methods used when connecting to jawbone
 */
public class JawboneUtils {

    private JawboneUtils(){}

    /**
     * Method used to check if the user is connected to UP
     * @param context is the context of the activity
     * @param intent is the intent of the activity (Not used in this stage)
     */
    public static void checkConnectionToUP(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRestApiInterface().getUser(
                    UpPlatformSdkConstants.API_VERSION_STRING,
                    callbackFromUP);
        }


    }

    /**
     * Callback function from UP to check if the user is connected
     */
    private static Callback callbackFromUP = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e("UP Connection", "Got response from UP");
            //isConnected = true;
            Account account = Account.getInstance();
            account.setConnectedUP(true);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e("UP Connection", "Failure with connecting to UP");
            //isConnected = false;
            Account account = Account.getInstance();
            account.setConnectedUP(true);
        }
    };
}
