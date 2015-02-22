package com.shchurov.razuberisamples.basic_sample;

import android.os.Bundle;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreensActivity;
import com.shchurov.razuberi.history.HistoryScreensManager;
import com.shchurov.razuberisamples.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ScreensActivity<HistoryScreensManager> implements OnShowNextScreenListener {

    public static final int ANIMATION_CODE_ADDED = 1;
    public static final int ANIMATION_CODE_REPLACED = 2;
    public static final int ANIMATION_CODE_BACK_PRESSED = HistoryScreensManager.ANIMATION_CODE_BACK_PRESSED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupScreens();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.main_activity);
    }

    @Override
    protected HistoryScreensManager createScreensManager(Bundle savedInstanceState) {
        return new HistoryScreensManager(this, savedInstanceState);
    }

    private void setupScreens() {
        List<Screen> screens = getScreensManager().getAddedScreens();
        if (screens.isEmpty()) {
            AScreen aScreen = new AScreen();
            getScreensManager().add(aScreen, R.id.fl_main_container, AScreen.SCREEN_TAG, ANIMATION_CODE_ADDED);
        }
    }

    @Override
    public void onShowNextScreen() {
        HistoryScreensManager screensManager = getScreensManager();
        ArrayList<Screen> addedScreens = screensManager.getAddedScreens();
        String currentScreenTag = addedScreens.get(addedScreens.size() - 1).getTag();
        if (currentScreenTag.equals(AScreen.SCREEN_TAG)) {
            screensManager.replace(new BScreen(), R.id.fl_main_container, BScreen.SCREEN_TAG, ANIMATION_CODE_ADDED, ANIMATION_CODE_REPLACED);
        } else if (currentScreenTag.equals(BScreen.SCREEN_TAG)) {
            screensManager.replace(new CScreen(), R.id.fl_main_container, CScreen.SCREEN_TAG, ANIMATION_CODE_ADDED, ANIMATION_CODE_REPLACED);
        }
    }

}
