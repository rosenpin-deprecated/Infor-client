package com.tomer.infor.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.tomer.infor.R;

import java.util.ArrayList;


/**
 * Created by tomer.
 */


// here's our beautiful adapter
public class ChatsAdapter extends ArrayAdapter<Profile> {

    Activity mContext;
    int layoutResourceId;
    ArrayList<Profile> users = null;

    public ChatsAdapter(Activity mContext, int layoutResourceId, ArrayList<Profile> users) {
        super(mContext, layoutResourceId, users);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = (mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Profile user = users.get(position);

        TextView userNameTv = (TextView) convertView.findViewById(R.id.name);
        userNameTv.setText(user.getName());
        userNameTv.setTag(user.getId());

        TextView lastMessageTV = (TextView) convertView.findViewById(R.id.message);
        lastMessageTV.setText("Last message goes here");
        lastMessageTV.setTag(user.getId());

        RoundedImageView profilePicIV = (RoundedImageView) convertView.findViewById(R.id.icon);
        Picasso.with(mContext).load(user.getPictureURL()).into(profilePicIV);
        profilePicIV.setTag(user.getId());

        return convertView;

    }
}
