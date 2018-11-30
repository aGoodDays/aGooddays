package com.jongseol.agoodday.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jongseol.agoodday.Model.Posture;
import com.jongseol.agoodday.R;

import java.util.ArrayList;

public class PostureAdapter extends BaseAdapter {


    private ArrayList<Posture> posturesList;
    private ViewHolder viewHolder;
    public PostureAdapter(ArrayList<Posture> posturesList) {
        this.posturesList = posturesList;
    }

    @Override
    public int getCount() {
        return posturesList.size();
    }

    @Override
    public Object getItem(int position) {
        return posturesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_posture, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.item_posture_date);
            viewHolder.bad_count = (TextView) convertView.findViewById(R.id.item_posture_bad_count);
            viewHolder.all_count = (TextView) convertView.findViewById(R.id.item_posture_all_count);
            viewHolder.ratio = (TextView) convertView.findViewById(R.id.item_posture_ratio);
            convertView.setTag(viewHolder);
        }else{
            convertView.getTag();
        }

        viewHolder.date.setText(posturesList.get(position).date);
        viewHolder.bad_count.setText(String.valueOf(posturesList.get(position).bad_count));
        viewHolder.all_count.setText(String.valueOf(posturesList.get(position).all_count));
        viewHolder.ratio.setText(String.valueOf(Math.round(posturesList.get(position).ratio *100)) + "%");

        return convertView;
    }

    private class ViewHolder{
        public TextView date;
        public TextView all_count;
        public TextView bad_count;
        public TextView ratio;
    }
}
