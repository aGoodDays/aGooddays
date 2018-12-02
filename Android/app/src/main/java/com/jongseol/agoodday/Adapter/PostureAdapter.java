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

/**
 * @file .Adapter.PostureAdapter.java
 * @brief Posture Item과 ListView를 연결해주는 어댑터.
 * @author jeje (las9897@gmail.com)
 *
 */
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

    /**
     * @brief XML로 만들어진 View들을 연결하고 데이터를 세팅해주는 부분입니다. findViewById의 사용을 최소화하기 위해서 ViewHolder를 사용했습니다.
     * @author jeje (las9897@gmail.com)
     * @return View convertView
     */
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

    /**
     * @brief Inner class로 만들어진 ViewHolder 입니다. 리스트에 적용되는 아이템을 바탕으로 작성했습니다.
     * @author jeje (las9897@gmail.com)
     */
    private class ViewHolder{
        public TextView date;
        public TextView all_count;
        public TextView bad_count;
        public TextView ratio;
    }
}
