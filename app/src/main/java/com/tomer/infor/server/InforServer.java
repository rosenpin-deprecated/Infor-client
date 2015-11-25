package com.tomer.infor.server;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.tomer.infor.Utils.Profile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Yoav on 8/23/2015.
 */
public class InforServer {


    /*
        Ex for use:
        boolean isLoggedIn = inforSever.login(accessToken.getToken(),  prefs.getString(Consts.GCM_ID, "Doesn't have GCM ID :)"));
     */
    public static boolean login(String accessToken, String gcm_id) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new Request("/auth/facebook/token", accessToken, gcm_id).execute().get();

            String res = jsonObject.getString("success");

            return res.equals("true");
        } catch (Exception e) {
            Log.d("Error loging in ", e.getMessage());
            return false;
        }

    }

    public static ArrayList<Profile> getFriendsList(String accessToken) {

        JSONObject jsonObject = null;
        ArrayList<Profile> friendsList = new ArrayList<>();
        try {
            jsonObject = new Request("/update/friendslist", accessToken).execute().get();

            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject element = data.getJSONObject(i);
                friendsList.add(new Profile((String) element.get("name"), (String) element.get("id"), "https://graph.facebook.com/" + element.get("id") + "/picture?width=400&height=400"));
            }

            return friendsList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return friendsList;

    }

    public void checkOnUser(String accessToken, String userId, String whatChecked) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new Request("/whatchecked", accessToken, userId, whatChecked).execute().get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String checkAboutUser(String accessToken, String userId) {

        JSONObject jsonObject = null;
        String response = null;

        try {
            jsonObject = new Request("/checkwhatcheck", accessToken, userId, "").execute().get();

            response = jsonObject.get("what_checked") + "";

            return response;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private static class Request extends AsyncTask<String, String, JSONObject> {

        String request;
        String accessToken;

        String whoCheckedID;
        String whatChecked;
        String gcm_id;


        public Request(String request, String accessToken) {
            this.request = request;
            this.accessToken = accessToken;
            this.whoCheckedID = null;
            this.whatChecked = null;
            this.gcm_id = null;
        }

        // For login
        public Request(String request, String accessToken, String gcm_id) {
            this.request = request;
            this.accessToken = accessToken;
            this.gcm_id = gcm_id;
            whoCheckedID = null;
            whatChecked = null;
        }

        public Request(String request, String accessToken, String whoCheckedID, String whatChecked) {
            this.request = request;
            this.accessToken = accessToken;
            this.whoCheckedID = whoCheckedID;
            this.whatChecked = whatChecked;
            this.gcm_id = null;
        }


        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("access_token", accessToken));

            if (gcm_id != null) {
                params.add(new BasicNameValuePair("gcm_id", gcm_id));
            }

            if (whatChecked != null) {

                params.add(new BasicNameValuePair("whochecked_id", whoCheckedID));
                params.add(new BasicNameValuePair("what_checked", whatChecked));
            }


            JSONObject jObj = json.getJSONFromUrl(Consts.REQUEST_PREFIX + "" + request, params);

            return jObj;

        }

        @Override
        protected void onPostExecute(JSONObject json) {


        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
