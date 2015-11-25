package com.tomer.infor.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tomer.infor.R;
import com.tomer.infor.server.Consts;
import com.tomer.infor.server.InforServer;
import com.tomer.infor.server.JSONParser;


/**
 * Created by tomer.
 */
public class FacebookLogin extends Activity {

    CallbackManager callbackManager;
    SharedPreferences prefs;
    List<NameValuePair> params;
    AccessToken accessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.facebook_login);

        //  progress = new ProgressDialog(this);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        prefs = this.getSharedPreferences(
                Consts.PREFS_TAG, Context.MODE_PRIVATE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(FacebookLogin.this, Arrays.asList("public_profile", "user_friends", "basic_info"));
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // Recieve access token for autohrizing
                accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    startActivity(new Intent(getApplicationContext(),Splash.class));
                    finish();
                    Log.d("DID USER LOG IN? ", String.valueOf(InforServer.login(accessToken.getToken(), "")));
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLogin.this, "Canceled!", Toast.LENGTH_SHORT).show();
                Log.d("LOGED IN ", "CANCEL");
                accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    startActivity(new Intent(getApplicationContext(),Splash.class));
                    finish();
                    Log.d("DID USER LOG IN? ", String.valueOf(InforServer.login(accessToken.getToken(), "")));
                }
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(FacebookLogin.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("LOGED IN ", "ERROR");
            }

        });
    }

    /*
         Authenticate the user
    */
    private class Login extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            // prefs.edit().putString(Consts.GCM_ID, regid).apply();
            params.add(new BasicNameValuePair("access_token", accessToken.getToken()));
            params.add(new BasicNameValuePair("gcm_id", prefs.getString(Consts.GCM_ID, "Doesn't have GCM ID :)")));

            JSONObject jObj = json.getJSONFromUrl(Consts.REQUEST_PREFIX + "/auth/facebook/token", params);

            return jObj;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json != null) {

                    Toast.makeText(FacebookLogin.this, "Jso: " + json, Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = FacebookLogin.this.getSharedPreferences(
                            Consts.PREFS_TAG, Context.MODE_PRIVATE);

                    String res = json.getString("success");

                    if (res.equals("true")) {

                        // Save ID and Name to phone
                        prefs.edit().putString(Consts.USERNAME_KEY, json.getJSONObject("user_details").get("name").toString()).apply();
                        prefs.edit().putString(Consts.ID_KEY, json.getJSONObject("user_details").get("id").toString()).apply();


                    } else {
                        Toast.makeText(FacebookLogin.this, res, Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
