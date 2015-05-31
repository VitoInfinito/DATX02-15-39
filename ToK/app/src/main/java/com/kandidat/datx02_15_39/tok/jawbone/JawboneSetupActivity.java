
package com.kandidat.datx02_15_39.tok.jawbone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.api.response.OauthAccessTokenResponse;
import com.jawbone.upplatformsdk.oauth.OauthUtils;
import com.jawbone.upplatformsdk.oauth.OauthWebViewActivity;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.layout.AccessoriesHomeActivity;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.utility.JawboneUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activity for connecting to UP servers
 */
public class JawboneSetupActivity extends Activity {

    private static final String TAG = JawboneSetupActivity.class.getSimpleName();

    // The ids used to connect with the specified application
    private static final String CLIENT_ID = "9qZV-Q8xius";
    private static final String CLIENT_SECRET = "413cad28a2662f161a8de887fe2d43d38b88f834";

    // This has to be identical to the OAuth redirect url setup in Jawbone Developer Portal
    private static final String OAUTH_CALLBACK_URL = "http://localhost/tok?";


    private List<UpPlatformSdkConstants.UpPlatformAuthScope> authScope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.jawbone_setup);

        // Set required levels of permissions here, for demonstration purpose
        // we are requesting all permissions
        authScope  = new ArrayList<>();
        authScope.add(UpPlatformSdkConstants.UpPlatformAuthScope.ALL);

        Button oAuthAuthorizeButton = (Button) findViewById(R.id.authorizeButton);
        oAuthAuthorizeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = getIntentForWebView();
            startActivityForResult(intent, UpPlatformSdkConstants.JAWBONE_AUTHORIZE_REQUEST_CODE);
            }
        });

        Intent intent = getIntentForWebView();
        startActivityForResult(intent, UpPlatformSdkConstants.JAWBONE_AUTHORIZE_REQUEST_CODE);

        setConnectionText();
    }

    /**
     * Method for setting the connection text in the activity
     */
    private void setConnectionText() {
        String connectText = "Inte uppkopplad";
        int color = Color.argb(255, 107, 00, 00);
        if(Account.getInstance().isConnectedUP()) {
            Log.e(TAG, "Connection to UP confirmed");

            //connectText = "Uppkopplad som " + JawboneUtils.firstName + " " + JawboneUtils.lastName;
            connectText = "Uppkopplad";
            color = Color.argb(255, 47, 107, 20);
        }

        ((TextView) findViewById(R.id.connectionJawboneText)).setText(connectText);
        ((TextView) findViewById(R.id.connectionJawboneText)).setTextColor(color);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UpPlatformSdkConstants.JAWBONE_AUTHORIZE_REQUEST_CODE && resultCode == RESULT_OK) {
            String code = data.getStringExtra(UpPlatformSdkConstants.ACCESS_CODE);
            if (code != null) {
                //first clear older accessToken, if it exists..
                ApiManager.getRequestInterceptor().clearAccessToken();

                ApiManager.getRestApiInterface().getAccessToken(
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    accessTokenRequestListener);
            }
        }
   }

    private Callback accessTokenRequestListener = new Callback<OauthAccessTokenResponse>() {
        @Override
        public void success(OauthAccessTokenResponse result, Response response) {

            if (result.access_token != null) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(JawboneSetupActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, result.access_token);
                editor.putString(UpPlatformSdkConstants.UP_PLATFORM_REFRESH_TOKEN, result.refresh_token);
                editor.commit();

                /*Intent intent = new Intent(JawboneSetupActivity.this, UpApiListActivity.class);
                intent.putExtra(UpPlatformSdkConstants.CLIENT_SECRET, CLIENT_SECRET);
                startActivity(intent);*/


                Account account = Account.getInstance();

                //JawboneUtils.isConnected = true;
                account.setConnectedUP(true);
                //startActivity(new Intent(JawboneSetupActivity.this, AccessoriesHomeActivity.class));
                Class nextCallback = account.getNextClassCallback();
                if(nextCallback != null) {
                    startActivity(new Intent(JawboneSetupActivity.this, nextCallback));
                }else {
                    startActivity(new Intent(JawboneSetupActivity.this, AccessoriesHomeActivity.class));
                }

                Log.e(TAG, "accessToken:" + result.access_token);
            } else {
                Log.e(TAG, "accessToken not returned by Oauth call, exiting...");
            }
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG, "failed to get accessToken:" + retrofitError.getMessage());
        }
    };

    /**
     * Method for creating an intent for web view
     * @return an intent of the web view
     */
    private Intent getIntentForWebView() {
        Uri.Builder builder = OauthUtils.setOauthParameters(CLIENT_ID, OAUTH_CALLBACK_URL, authScope);

        Intent intent = new Intent(OauthWebViewActivity.class.getName());
        intent.putExtra(UpPlatformSdkConstants.AUTH_URI, builder.build());
        return intent;
    }
}