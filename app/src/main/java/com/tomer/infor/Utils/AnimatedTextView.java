package com.tomer.infor.Utils;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by tomer on 10/5/15.
 */
public class AnimatedTextView extends TextView {
    public AnimatedTextView(Context context) {
        super(context);
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
