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
 * Created by tomashasselquist on 06/05/15.
 */
public class JawboneUtils {

    private JawboneUtils(){}

    public static void checkConnectionToUP(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

        if (mAccessToken != null) {
            ApiManager.getRestApiInterface().getUser(
                    UpPlatformSdkConstants.API_VERSION_STRING,
                    callbackFromUP);
        }


    }

    private static Callback callbackFromUP = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e("UP Connection", "Got response from UP");
            //isConnected = true;
            Account account = Account.getInstance();
            account.setConnectedUP(true);
            try {
                //LinkedTreeMap obj = (LinkedTreeMap) o;
                //LinkedTreeMap data = (LinkedTreeMap) obj.get("data");

                //firstName = data.get("first").toString();
                //lastName = data.get("last").toString();
                //Log.e("UP Connection", "Connection to account belonging to " + firstName + " " + lastName + " successfully established");
            }catch(Exception e) {
                Log.e("UP Connection", "Error with fetched UP Data");
                e.printStackTrace();
            }
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
