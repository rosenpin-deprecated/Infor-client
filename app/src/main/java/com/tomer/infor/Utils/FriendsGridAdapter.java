package com.tomer.infor.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.tomer.infor.R;

/**
 * Created by tomer on 9/23/15.
 */
public class FriendsGridAdapter extends BaseAdapter {
    private Activity mContext;
    ArrayList<Profile> profiles;
    public FriendsGridAdapter(Activity c, ArrayList<Profile> profiles) {
        mContext = c;
        this.profiles = profiles;
    }

    public int getCount() {
        return profiles.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = (mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }
        imageView = (ImageView) convertView.findViewById(R.id.friend_image);
        TextView friend_name = (TextView) convertView.findViewById(R.id.friend_name);
        friend_name.setText(profiles.get(position).getName());
        Picasso.with(mContext).load(profiles.get(position).getPictureURL()).into(imageView);
        return convertView;
    }

}
