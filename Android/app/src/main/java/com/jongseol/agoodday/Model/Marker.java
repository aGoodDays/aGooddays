package com.jongseol.agoodday.Model;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.jongseol.agoodday.R;

public class Marker extends MarkerView {

    private TextView text_ratio_count;

    public Marker(Context context, int layoutResource) {
        super(context, layoutResource);
        text_ratio_count = (TextView) findViewById(R.id.item_marker_text);
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry candleEntry = (CandleEntry) e;
            text_ratio_count.setText("" + Utils.formatNumber(candleEntry.getHigh(), 0, true));
        }else{
            text_ratio_count.setText("" + Utils.formatNumber(e.getY(), 0, true));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() /2), -getHeight());
    }
}
