package com.shchurov.razuberisamples.basic_sample;

import android.os.Bundle;

import com.shchurov.razuberi.core.HistoryEntry;
import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreenState;
import com.shchurov.razuberi.core.ScreensActivity;
import com.shchurov.razuberi.core.ScreensManager;
import com.shchurov.razuberisamples.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ScreensActivity implements OnShowNextScreenListener {

    public static final int ANIMATION_CODE_ADD_FORWARD = 1;
    public static final int ANIMATION_CODE_REMOVE_FORWARD = 2;
    public static final int ANIMATION_CODE_ADD_BACKWARD = 3;
    public static final int ANIMATION_CODE_REMOVE_BACKWARD = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupScreens();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.main_activity);
    }

    private void setupScreens() {
        List<Screen> screens = getScreensManager().getAddedScreens();
        if (screens.isEmpty()) {
            AScreen aScreen = new AScreen();
            getScreensManager().add(aScreen, R.id.fl_main_container, AScreen.SCREEN_TAG, ANIMATION_CODE_ADD_FORWARD);
        }
    }

    @Override
    public void onShowNextScreen() {
        ArrayList<Screen> addedScreens = getScreensManager().getAddedScreens();
        Screen currentScreen = addedScreens.get(addedScreens.size() - 1);
        String currentScreenTag = currentScreen.getTag();
        if (currentScreenTag.equals(AScreen.SCREEN_TAG)) {
            replaceScreen(currentScreen, ANIMATION_CODE_REMOVE_FORWARD, new BScreen(), BScreen.SCREEN_TAG, ANIMATION_CODE_ADD_FORWARD);
        } else if (currentScreenTag.equals(BScreen.SCREEN_TAG)) {
            replaceScreen(currentScreen, ANIMATION_CODE_REMOVE_FORWARD, new CScreen(), CScreen.SCREEN_TAG, ANIMATION_CODE_ADD_FORWARD);
        }
    }

    private void replaceScreen(Screen toBeRemoved, int removeAnimationCode, Screen toBeAdded, String addedScreenTag, int addAnimationCode) {
        ScreensManager screensManager = getScreensManager();
        screensManager.add(toBeAdded, R.id.fl_main_container, addedScreenTag, addAnimationCode);
        ScreenState state = screensManager.getStateAndRemove(toBeRemoved, removeAnimationCode);
        screensManager.addToHistory(new HistoryEntry(state));
    }

    @Override
    public void onBackPressed() {
        if (getScreensManager().onBackPressed())
            return;
        if (!getScreensManager().getHistory().isEmpty()) {
            reverseHistory();
        } else {
            super.onBackPressed();
        }
    }

    private void reverseHistory() {
        ScreensManager screensManager = getScreensManager();
        ArrayList<Screen> addedScreens = getScreensManager().getAddedScreens();
        Screen currentScreen = addedScreens.get(addedScreens.size() - 1);
        screensManager.remove(currentScreen, ANIMATION_CODE_REMOVE_BACKWARD);
        HistoryEntry historyEntry = screensManager.popLastHistoryEntry();
        for (ScreenState screenState : historyEntry.getEntryScreens()) {
            screensManager.restoreStateAndAdd(screenState, ANIMATION_CODE_ADD_BACKWARD);
        }
    }

}
