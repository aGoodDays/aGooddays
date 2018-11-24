package com.jongseol.agoodday.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jongseol.agoodday.Model.Device;
import com.jongseol.agoodday.R;

import java.text.SimpleDateFormat;

import java.util.List;

/**
 * @file jongseol.agoodday.Adapter.PostureListAdapter.java
 * @brief Adapters that link data to be shown in the list.
 * @author jeje (las9897@gmail.com)
 */
public class PostureListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Device> deviceList;
    private LayoutInflater inflater;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd %HH:%mm");

   /***
    * @brief Constructor 
    * @param context
    * @param layout
    * @param deviceList
    */
    public PostureListAdapter(Context context, int layout, List<Device> deviceList) {
        this.context = context;
        this.layout = layout;
        this.deviceList = deviceList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @brief Inner Class \n
     *        ViewHolder
     */
    class ViewHolder {
        TextView device_id, saX, saY, saZ, sgX, sgY, sgZ, xdegree, ydegree, zdegree, date;
    }

    /**
     * @return int devicelist.size
     */
    @Override
    public int getCount() {
        return deviceList.size();
    }


    /**
     * @brief Decrease findViewById count
     * @param position
     * @param convertView
     * @param parent
     * @return View converView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.posture_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.device_id = (TextView) convertView.findViewById(R.id.postureItem_textview_device_id);
            viewHolder.saX = (TextView) convertView.findViewById(R.id.postureItem_textview_saX);
            viewHolder.saY = (TextView) convertView.findViewById(R.id.postureItem_textview_saY);
            viewHolder.saZ = (TextView) convertView.findViewById(R.id.postureItem_textview_saZ);
            viewHolder.sgX = (TextView) convertView.findViewById(R.id.postureItem_textview_sgX);
            viewHolder.sgY = (TextView) convertView.findViewById(R.id.postureItem_textview_sgY);
            viewHolder.sgZ = (TextView) convertView.findViewById(R.id.postureItem_textview_sgZ);
            viewHolder.xdegree = (TextView) convertView.findViewById(R.id.postureItem_textview_xdegree);
            viewHolder.ydegree = (TextView) convertView.findViewById(R.id.postureItem_textview_ydegree);
            viewHolder.zdegree = (TextView) convertView.findViewById(R.id.postureItem_textview_zdegree);
            viewHolder.date = (TextView) convertView.findViewById(R.id.postureItem_textview_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.device_id.setText(deviceList.get(position).device_id);
        viewHolder.saX.setText(String.valueOf(deviceList.get(position).saX));
        viewHolder.saY.setText(String.valueOf(deviceList.get(position).saX));
        viewHolder.saZ.setText(String.valueOf(deviceList.get(position).saX));
        viewHolder.sgX.setText(String.valueOf(deviceList.get(position).sgX));
        viewHolder.sgY.setText(String.valueOf(deviceList.get(position).sgY));
        viewHolder.sgZ.setText(String.valueOf(deviceList.get(position).sgZ));
        viewHolder.xdegree.setText(String.valueOf(deviceList.get(position).xdegree));
        viewHolder.ydegree.setText(String.valueOf(deviceList.get(position).ydegree));
        viewHolder.zdegree.setText(String.valueOf(deviceList.get(position).zdegree));
        viewHolder.date.setText(deviceList.get(position).date);
        return convertView;
    }

    /**
     * 
     * @param position
     * @return object device
     */
    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    /**
     * 
     * @param position
     * @return position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

}
