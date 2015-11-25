package com.tomer.infor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.tomer.infor.R;
import com.tomer.infor.Utils.FriendsGridAdapter;
import com.tomer.infor.Utils.Profile;
import com.tomer.infor.Utils.RevealLayout;

import java.util.ArrayList;


/**
 * Created by tomer.
 */
public class FriendsFragment extends Fragment {
    Activity context;
    private RevealLayout mRevealLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealLayout.hide(0);
        int position = FragmentPagerItem.getPosition(getArguments());
        Intent intent = getActivity().getIntent();
        ArrayList<Profile> list = (ArrayList<Profile>) intent.getSerializableExtra("list");
        GridView gridview = (GridView) view.findViewById(R.id.friends_grid);

        gridview.setAdapter(new FriendsGridAdapter(context, list));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mRevealLayout.isContentShown()) {
                    mRevealLayout.hide((int) (v.getX() + (v.getWidth() / 2)), (int) (v.getY() + (v.getHeight() / 2)));
                } else {
                    mRevealLayout.show((int) (v.getX() + (v.getWidth() / 2)), (int) (v.getY() + (v.getHeight() / 2)));
                }
            }
        });

    }
}
