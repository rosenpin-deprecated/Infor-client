package com.tomer.infor.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by tomer.
 */
public class Profile implements Serializable {
    private String name, id, gender;
    private String imageURL;

    public Profile(String name, String id, String imageURL) {
        this.name = name;
        this.id = id;
        this.imageURL = imageURL;
    }

    public String getPictureURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
