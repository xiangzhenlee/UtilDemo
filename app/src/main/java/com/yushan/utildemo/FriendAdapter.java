package com.yushan.utildemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by beiyong on 2017-6-8.
 */

public class FriendAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mLists;

    public FriendAdapter(Context context, ArrayList<String> list) {
        this.mContext = context;
        this.mLists = list;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_list, null);

            holder.tv_name = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String friendName = mLists.get(position);
        holder.tv_name.setText(friendName);

        return convertView;
    }


    class ViewHolder {
        private TextView tv_name;
    }
}
