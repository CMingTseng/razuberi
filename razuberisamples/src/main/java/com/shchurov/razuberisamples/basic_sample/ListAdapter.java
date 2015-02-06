package com.shchurov.razuberisamples.basic_sample;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shchurov.razuberisamples.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private static final double RED_FACTOR = 0.299;
    private static final double GREEN_FACTOR = 0.587;
    private static final double BLUE_FACTOR = 0.114;
    private static final double WHITE_THRESHOLD = 0.5f;

    private List<ListItem> items;

    public ListAdapter(List<ListItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ListItem item = items.get(i);
        viewHolder.llItem.setBackgroundColor(item.color);
        int textColor = determineTextColor(item.color);
        viewHolder.tvText1.setText("#" + Integer.toHexString(item.color));
        viewHolder.tvText1.setTextColor(textColor);
        viewHolder.tvText2.setText(item.description);
        viewHolder.tvText2.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int determineTextColor(int backgroundColor) {
        double a = 1 - (RED_FACTOR * Color.red(backgroundColor)
                + GREEN_FACTOR * Color.green(backgroundColor) + BLUE_FACTOR * Color.blue(backgroundColor)) / 255;
        if (a < WHITE_THRESHOLD) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;
        TextView tvText1;
        TextView tvText2;

        ViewHolder(View itemView) {
            super(itemView);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
            tvText1 = (TextView) itemView.findViewById(R.id.tv_text1);
            tvText2 = (TextView) itemView.findViewById(R.id.tv_text2);
        }

    }

}
