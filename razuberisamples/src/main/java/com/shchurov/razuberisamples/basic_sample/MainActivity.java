package com.shchurov.razuberisamples.basic_sample;

import android.os.Bundle;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreensActivity;
import com.shchurov.razuberisamples.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ScreensActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupScreens();
    }

    private void setupScreens() {
        List<Screen> screens = getScreensManager().getAddedScreens();
        if (screens.isEmpty()) {
            ListScreen listScreen = new ListScreen();
            listScreen.getPersistentData().putParcelableArrayList(ListScreen.KEY_ITEMS, generateSampleData());
            getScreensManager().add(listScreen, R.id.fl_main_container, ListScreen.SCREEN_TAG, ListScreen.ANIMATION_CODE_ADD);
        }
    }

    private ArrayList<ListItem> generateSampleData() {
        ArrayList<ListItem> items = new ArrayList<>();
        items.add(new ListItem(0xfff44336, "Red"));
        items.add(new ListItem(0xffe91e63, "Pink"));
        items.add(new ListItem(0xff9c27b0, "Purple"));
        items.add(new ListItem(0xff673ab7, "Deep purple"));
        items.add(new ListItem(0xff3f51b5, "Indigo"));
        items.add(new ListItem(0xff2196f3, "Blue"));
        items.add(new ListItem(0xff03a9f4, "Light blue"));
        items.add(new ListItem(0xff00bcd4, "Cyan"));
        items.add(new ListItem(0xff009688, "Teal"));
        items.add(new ListItem(0xff4caf50, "Green"));
        items.add(new ListItem(0xff8bc34a, "Light green"));
        items.add(new ListItem(0xffcddc39, "Yellow"));
        items.add(new ListItem(0xffffeb3b, "Amber"));
        items.add(new ListItem(0xffffc107, "Orange"));
        items.add(new ListItem(0xffff9800, "Deep orange"));
        Collections.shuffle(items);
        return items;
    }

}
