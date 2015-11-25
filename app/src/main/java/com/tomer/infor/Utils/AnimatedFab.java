package com.tomer.infor.Utils;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by tomer.
 */
public class AnimatedFab extends android.support.design.widget.FloatingActionButton {


    public AnimatedFab(Context context) {
        super(context);
        this.setVisibility(INVISIBLE);

    }

    public AnimatedFab(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setVisibility(INVISIBLE);
    }

    public AnimatedFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setVisibility(INVISIBLE);
    }

    public void anim(int delay, int duration) {
        final TranslateAnimation anim = new TranslateAnimation(0, 0, 500, 0);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.setDuration(duration);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setVisibility(VISIBLE);
                startAnimation(anim);
            }
        }, delay);
    }


}
