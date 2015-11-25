package com.tomer.infor.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.squareup.picasso.Picasso;
import com.tomer.infor.Activities.FirstActivity;
import com.tomer.infor.R;
import com.tomer.infor.Utils.AnimatedFab;
import com.tomer.infor.Utils.AnimatedTextView;
import com.tomer.infor.Utils.Profile;
import com.tomer.infor.Utils.RevealLayout;
import com.tomer.infor.Utils.RoundedImageView;
import com.tomer.infor.Utils.WaterWaveProgress;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.Random;

import com.tomer.infor.Activities.Splash;

import org.w3c.dom.Text;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


/**
 * Created by tomer.
 */
public class MainFragment extends android.support.v4.app.Fragment {
    boolean myLib = false, rounded = false, enlarged, bigLarged = false;
    FrameLayout innerContent;
    Context context;
    CircleRefreshLayout mRefreshLayout;
    ArrayList<View> friends;
    AnimatedTextView skip;
    AnimatedFab animatedFabs[];
    MediaPlayer sound;
    int freindsCounter = 0;
    RoundedImageView picIMG;
    WaterWaveProgress waveProgress;
    long then = 0;
    RevealLayout mRevealLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.context = getActivity().getApplicationContext();
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRevealLayout = (RevealLayout) getActivity().findViewById(R.id.reveal_layout);
        mRevealLayout.hide(0);
        innerContent = (FrameLayout) view.findViewById(R.id.inner_content_wrapper);
        sound = MediaPlayer.create(context, R.raw.swish);
        friends = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        ArrayList<Profile> list = (ArrayList<Profile>) intent.getSerializableExtra("list");
        for (int i = 0; i < list.size(); i++) {
            friends.add(i, addProfile(list.get(i), 1000));
        }
        //ADDING FLOATING ACTION BUTTONS
        animatedFabs = new AnimatedFab[]{(AnimatedFab) view.findViewById(R.id.sex), (AnimatedFab) view.findViewById(R.id.friendship), (AnimatedFab) view.findViewById(R.id.relationship)};
        int counter = 2;
        for (AnimatedFab animatedFab : animatedFabs) {
            //The counter makes the animation take longer
            counter++;
            animatedFab.anim(counter * 100, counter * 200);
        }
        skip = (AnimatedTextView) view.findViewById(R.id.skip);
        skip.anim(100*7, 200*7);
        initiateWaveView();
        if (friends.size() < 1) {
            Snackbar.make(
                    animatedFabs[0], "An error has occurred connecting to the server please restart the app and try again. "
                    , Snackbar.LENGTH_LONG).setAction("Restart", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(getActivity().getApplicationContext(), Splash.class));
                    getActivity().finish();
                }
            }).show();
        } else {
            //Adding the first friend
            addFriend();
            //Initiating the swipe to reload using the parent fragment view
            refreshLayout(view);
            //Add showcase
           // showCase();

        }
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789nyc23an8364xuhskdjfbgdjsvfclasmxdkjadhfcגחשדיכגשדמוב74ש'dfkasjn0";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 100) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private void showCase() {
        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), getSaltString());

        sequence.setConfig(config);

        sequence.addSequenceItem(animatedFabs[0],
                "Something sexually", "GOT IT");

        sequence.addSequenceItem(animatedFabs[1],
                "Would like to get to know you better", "GOT IT");

        sequence.addSequenceItem(animatedFabs[2],
                "Girlfriend/Boyfriend material", "GOT IT");

        sequence.addSequenceItem(picIMG,
                "Long click this to view the larger image", "GOT IT");

        sequence.addSequenceItem(waveProgress,
                "When you get to 1 credit or more, click this", "GOT IT");

        sequence.start();
    }

    private void initiateWaveView() {
        waveProgress = (WaterWaveProgress) getActivity().findViewById(R.id.waterWaveProgress1);
        waveProgress.animateWave();
        waveProgress.setMaxProgress(1f);
        waveProgress.setProgress(0.7f);
        waveProgress.setShowProgress(true);
        waveProgress.setShowNumerical(false);
    }

    private void refreshLayout(View view) {
        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mRefreshLayout.finishRefreshing();
                            }
                        };
                        new Thread(runnable).start();
                    }

                    @Override
                    public void completeRefresh() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                getActivity().startActivity(new Intent(getActivity().getApplicationContext(), FirstActivity.class));
                                getActivity().finish();
                            }
                        };
                        new Thread(runnable).start();

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mRevealLayout.isContentShown())
                        mRevealLayout.hide((int) (waveProgress.getX() + (waveProgress.getWidth() / 2)), (int) (waveProgress.getY() + (waveProgress.getHeight() / 2)));
                    return true;
                }
                return false;
            }
        });
    }

    private void setFABClickListener() {
        waveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waveProgress.getProgress() < 1) {
                    Snackbar.make(
                            animatedFabs[0], "You will get a credit in " +
                                    (int) ((1 - waveProgress.getProgress()) * 10 + 1) +
                                    " marks", Snackbar.LENGTH_SHORT).show();
                } else {
                    mRevealLayout.getChildAt(0).setBackgroundColor(0xFF33B5E5);
                    if (mRevealLayout.isContentShown()) {
                        mRevealLayout.hide((int) (v.getX() + (v.getWidth() / 2)), (int) (v.getY() + (v.getHeight() / 2)));
                    } else {
                        mRevealLayout.show((int) (v.getX() + (v.getWidth() / 2)), (int) (v.getY() + (v.getHeight() / 2)));
                    }
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();
                    mFragmentTransaction.addToBackStack(null);
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposeProfile(friends.get(freindsCounter));
                innerContent.addView(friends.get(freindsCounter));
            }
        });
        for (final AnimatedFab animatedFab : animatedFabs) {
            animatedFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        disposeProfile(friends.get(freindsCounter));
                        innerContent.addView(friends.get(freindsCounter));
                        waveProgress.setProgress(waveProgress.getProgress() + 0.1f);
                        if (waveProgress.getProgress() == 1) {
                            final Animation pulse = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
                            waveProgress.startAnimation(pulse);
                            waveProgress.setmRingColor(getResources().getColor(R.color.orange));
                            waveProgress.setShowNumerical(true);
                        }
                    } catch (Exception e) {
                        Snackbar.make(animatedFabs[0], "OUT OF FRIENDS", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void removeFabClick() {
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        for (AnimatedFab animatedFab : animatedFabs) {
            animatedFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void disposeProfile(final View friend) {
        removeFabClick();
        if (freindsCounter < friends.size() - 1) {
            freindsCounter++;
        }
        final TextView nameTV = (TextView) friend.findViewById(R.id.name);
        picIMG = (RoundedImageView) friend.findViewById(R.id.profile_pic);
        final TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -1000);
        final Handler handler = new Handler();
        anim.setDuration(600);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        handler.postDelayed(new Runnable() {
            public void run() {
                sound.start();
                picIMG.startAnimation(anim);
                picIMG.setVisibility(View.INVISIBLE);
            }
        }, 120);
        final TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, -1000);
        anim2.setDuration(1000);
        anim2.setInterpolator(new FastOutLinearInInterpolator());
        nameTV.startAnimation(anim2);
        nameTV.setVisibility(View.INVISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                innerContent.removeView(friend);
                animateProfile(friends.get(freindsCounter), 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private View addProfile(Profile profile, int delay) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View friends = li.inflate(R.layout.friend_page, null);
        final TextView nameTV = (TextView) friends.findViewById(R.id.name);
        picIMG = (RoundedImageView) friends.findViewById(R.id.profile_pic);
        nameTV.setVisibility(View.INVISIBLE);
        picIMG.setVisibility(View.INVISIBLE);
        nameTV.setText(profile.getName());
        Picasso.with(context).load(profile.getPictureURL()).into(picIMG);
        return friends;

    }

    private void animateProfile(View friend, int delay) {
        removeFabClick();
        final TextView nameTV = (TextView) friend.findViewById(R.id.name);
        picIMG = (RoundedImageView) friend.findViewById(R.id.profile_pic);
        final TranslateAnimation anim = new TranslateAnimation(0, 0, 2000, 0);
        anim.setDuration(800);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                picIMG.startAnimation(anim);
                picIMG.setVisibility(View.VISIBLE);
                picIMG.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setFABClickListener();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }, delay);

        // Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

        // Add a spring to the system.
        final Spring spring = springSystem.createSpring();
        SpringConfig config = new SpringConfig(600, 22);
        spring.setSpringConfig(config);
        // Add a listener to observe the motion of the spring.
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                scale = scale * 2;
                picIMG.setScaleX(scale);
                picIMG.setScaleY(scale);

            }
        });


        // Set the spring in motion; moving from 0 to 1
        spring.setEndValue(1);
        final TranslateAnimation anim2 = new TranslateAnimation(0, 0, 2000, 0);
        anim2.setDuration(700);
        anim2.setInterpolator(new LinearOutSlowInInterpolator());
        final Handler handlerr = new Handler();
        handlerr.postDelayed(new Runnable() {
            public void run() {
                nameTV.startAnimation(anim2);
                nameTV.setVisibility(View.VISIBLE);
            }
        }, delay - 50);
        final ViewGroup.LayoutParams bckupLP = picIMG.getLayoutParams();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int origWidth = picIMG.getLayoutParams().width;
        picIMG.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN || event.getAction() == 2) {
                    if (!enlarged) {
                        spring.setEndValue(0.2);
                        enlarged = true;
                        then = System.currentTimeMillis();
                    }
                } else {
                    then = 0;
                    if (enlarged) {
                        spring.setEndValue(1);
                        picIMG.setRadiousDevider(2 + 0.1f, origWidth * 2);
                        enlarged = false;
                        bigLarged = false;
                    }
                }
                return false;
            }
        });
        picIMG.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(200);
                                    picIMG.setRadiousDevider(1, width);
                                    spring.setEndValue(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        runnable.run();
                        return false;
                    }
                }
        );
    }

    private void addFriend() {
        innerContent.addView(friends.get(freindsCounter));
        animateProfile(friends.get(freindsCounter), 1000);
    }
}
