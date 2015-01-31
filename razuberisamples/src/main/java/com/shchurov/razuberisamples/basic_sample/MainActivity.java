package com.shchurov.razuberisamples.basic_sample;

import android.os.Bundle;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreensActivity;
import com.shchurov.razuberi.core.ScreensManager;
import com.shchurov.razuberi.helpers.history.HistoryScreensManager;
import com.shchurov.razuberisamples.R;

import java.util.List;

public class MainActivity extends ScreensActivity {

    private HistoryScreensManager screensManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        screensManager = (HistoryScreensManager) getScreensManager();
        setupScreens();
    }

    @Override
    protected ScreensManager createScreensManager(Bundle savedInstanceState) {
        return new HistoryScreensManager(this, savedInstanceState);
    }

    private void setupScreens() {
        List<Screen> screens = screensManager.getAddedScreens();
        if (screens.isEmpty()) {
            ListScreen listScreen = new ListScreen();
            screensManager.add(listScreen, R.id.fl_main_container, ListScreen.SCREEN_TAG);
        }
    }


}
