package com.tomer.infor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.tomer.infor.R;
import com.tomer.infor.Utils.Profile;
import com.tomer.infor.server.InforServer;

import java.util.ArrayList;

/**
 * Created by tomer.
 */
public class Splash extends Activity {
    boolean facebookMode = false;
    private AnimatedCircleLoadingView animatedCircleLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Intent intent = getIntent();
        facebookMode = intent.getBooleanExtra("Facebook",true);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        startPercentMockThread();
    }

    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (facebookMode) {
                        //Thread.sleep(1500);
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        Thread.sleep(1000);
                        if (AccessToken.getCurrentAccessToken() == null) {
                            startActivity(new Intent(getApplicationContext(), FacebookLogin.class));
                            finish();
                        } else {
                            ArrayList<Profile> list = InforServer.getFriendsList(AccessToken.getCurrentAccessToken().getToken());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("list", list);
                            for (Profile profile : list) {
                                Log.d("name", profile.getName());
                            }
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    } else {
                        ArrayList<Profile> list = new ArrayList<>();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("list", list);
                        String names[] = {"Abbey", "Cherie", "Cate", "Britney", "Sascha"};
                        String images[] = {"http://img01.deviantart.net/5f33/i/2010/292/2/1/just_me_by_pansy_girl-d313e9b.jpg", "http://data.whicdn.com/images/38062161/original.jpg", "https://pbs.twimg.com/profile_images/2201111086/tumblr_m2w5tnohE31r46g7vo1_500.jpg", "http://img06.deviantart.net/5a06/i/2009/362/0/c/autumn_in_winter_by_that_stranger_girl.jpg", "http://www.molliemakes.com/wp-content/uploads/sites/49/2014/09/One-Sheepish-Girl-Profile.jpg"};
                        for (int i = 0; i < 5; i++) {
                            list.add(new Profile(names[i], String.valueOf(i), images[i]));
                            Thread.sleep(200);
                            changePercent(i*20);
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }
}
