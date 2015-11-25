package com.tomer.infor.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tomer.infor.Fragments.ChatFragment;
import com.tomer.infor.Fragments.FriendsFragment;
import com.tomer.infor.Fragments.MainFragment;
import com.tomer.infor.R;
import com.tomer.infor.Utils.WaterWaveProgress;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.friends, FriendsFragment.class)
                .add(R.string.home, MainFragment.class)
                .add(R.string.chats, ChatFragment.class)
                .create());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Infor");
        WaterWaveProgress waveProgress;
        waveProgress = (WaterWaveProgress) findViewById(R.id.waterWaveProgress1);
        waveProgress.animateWave();
        waveProgress.setMaxProgress(10);
        waveProgress.setProgress(5);
        waveProgress.setShowProgress(false);
        waveProgress.setShowNumerical(false);
    }

}
