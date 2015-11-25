package com.tomer.infor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tomer.infor.R;

/**
 * Created by tomer on 10/3/15.
 */
public class FirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        Button face = (Button) findViewById(R.id.facebook);
        Button nonFace = (Button) findViewById(R.id.non);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Splash.class);
                intent.putExtra("Facebook",true);
                startActivity(intent);
            }
        });
        nonFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Splash.class);
                intent.putExtra("Facebook",false);
                startActivity(intent);
            }
        });
    }
}
