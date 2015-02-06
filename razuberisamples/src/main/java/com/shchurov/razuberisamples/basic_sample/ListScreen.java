package com.shchurov.razuberisamples.basic_sample;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberisamples.R;

import java.util.List;

public class ListScreen extends Screen {

    public static final String SCREEN_TAG = "list";
    public static final int ANIMATION_CODE_ADD = 1;
    public static final String KEY_ITEMS = "items";

    private RecyclerView rvItems;

    @Override
    protected View onAdd(int animationCode) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.screen_list, null);
        rvItems = (RecyclerView) layout.findViewById(R.id.rv_items);
        setupList();
        return layout;
    }

    private void setupList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvItems.setLayoutManager(layoutManager);
        List<ListItem> items = getPersistentData().getParcelableArrayList(KEY_ITEMS);
        rvItems.setAdapter(new ListAdapter(items));
    }

}
