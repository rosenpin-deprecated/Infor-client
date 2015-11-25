package com.tomer.infor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.tomer.infor.R;
import com.tomer.infor.Utils.ChatsAdapter;
import com.tomer.infor.Utils.FriendsGridAdapter;
import com.tomer.infor.Utils.Profile;
import com.tomer.infor.Utils.RevealLayout;

import java.util.ArrayList;


/**
 * Created by tomer.
 */
public class ChatFragment extends Fragment {
    private RevealLayout mRevealLayout;
    private Activity context;
    private FloatingActionButton animatedFab;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealLayout.hide(0);
        int position = FragmentPagerItem.getPosition(getArguments());
        ListView chats = (ListView) view.findViewById(R.id.chats_lv);
        ArrayList<Profile> profiles = (ArrayList<Profile>) getActivity().getIntent().getSerializableExtra("list");
        ChatsAdapter myAdapter = new ChatsAdapter(getActivity(), R.layout.chat_item, profiles);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(myAdapter);
        animationAdapter.setAbsListView(chats);
        chats.setAdapter(animationAdapter);
        initiateFabAnimation(view);
    }

    private void initiateFabAnimation(View view) {
        animatedFab = (FloatingActionButton) view.findViewById(R.id.addChat);
        animatedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRevealLayout.isContentShown()) {
                    animatedFab.setVisibility(View.VISIBLE);
                    mRevealLayout.hide((int) (v.getX() + (v.getWidth() / 2)), (int) (v.getY() + (v.getHeight() / 2)));
                } else {
                    mRevealLayout.show((int) (v.getX() + (v.getWidth() / 2)), (int) (v.getY() + (v.getHeight() / 2)));
                    animatedFab.setVisibility(View.INVISIBLE);
                }
            }
        });
        FragmentTransaction mFragmentTransaction = getFragmentManager()
                .beginTransaction();
        mFragmentTransaction.addToBackStack(null);
    }

    private void initiateHiddenGridView(View view) {
        Intent intent = getActivity().getIntent();
        ArrayList<Profile> list = (ArrayList<Profile>) intent.getSerializableExtra("list");
        GridView gridview = (GridView) view.findViewById(R.id.friends_grid);
        gridview.setAdapter(new FriendsGridAdapter(context, list));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

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
                        animatedFab.setVisibility(View.VISIBLE);
                        mRevealLayout.hide((int) (animatedFab.getX() + (animatedFab.getWidth() / 2)), (int) (animatedFab.getY() + (animatedFab.getHeight() / 2)));
                    return true;
                }
                return false;
            }
        });
    }

}
