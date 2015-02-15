package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ScreensManager {

    private static final String SAVE_ADDED_SCREENS_KEY = "razuberi_saved_screens";
    private static final String SAVE_HISTORY_KEY = "razuberi_saved_history";

    private LinkedHashMap<String, Screen> addedScreens;
    private ArrayList<HistoryEntry> history;
    private Activity activity;

    public ScreensManager(Activity activity, Bundle savedState) {
        this.activity = activity;
        addedScreens = new LinkedHashMap<>();
        if (savedState != null) {
            restoreState(savedState);
        } else {
            history = new ArrayList<>();
        }
    }

    public void onSaveInstanceState(Bundle instanceState) {
        ArrayList<ScreenState> addedScreenStates = new ArrayList<>();
        for (Screen screen : addedScreens.values()) {
            screen.onActivitySaveInstanceState();
            addedScreenStates.add(screen.getScreenState());
        }
        instanceState.putParcelableArrayList(SAVE_ADDED_SCREENS_KEY, addedScreenStates);
        instanceState.putParcelableArrayList(SAVE_HISTORY_KEY, history);
    }

    public Screen restoreStateAndAdd(ScreenState screenState, int animationCode) {
        Screen screen = null;
        try {
            screen = screenState.getScreenClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(screen, screenState.getContainerId(), screenState.getTag(),
                screenState.getPersistentData(), screenState.getViewState(), animationCode);
        return screen;
    }

    public void add(Screen screen, int containerId, String screenTag, int animationCode) {
        add(screen, containerId, screenTag, null, null, animationCode);
    }

    private void add(Screen screen, int containerId, String screenTag, Bundle persistentData,
                     SparseArray<Parcelable> viewState, int animationCode) {
        addedScreens.put(screenTag, screen);
        View screenView = screen.performAdd(this, screenTag, containerId, persistentData, viewState, animationCode);
        ViewGroup container = (ViewGroup) activity.findViewById(containerId);
        container.addView(screenView);
    }

    public void remove(Screen screen, int animationCode) {
        screen.performRemove(animationCode);
    }

    public ScreenState getStateAndRemove(Screen screen, int animationCode) {
        ScreenState screenState = screen.getScreenState();
        remove(screen, animationCode);
        return screenState;
    }

    public ArrayList<Screen> getAddedScreens() {
        return new ArrayList<>(addedScreens.values());
    }

    public Screen getScreenByTag(String tag) {
        return addedScreens.get(tag);
    }

    public Activity getActivity() {
        return activity;
    }

    private void restoreState(Bundle savedState) {
        ArrayList<ScreenState> addedScreenStates = savedState.getParcelableArrayList(SAVE_ADDED_SCREENS_KEY);
        history = savedState.getParcelableArrayList(SAVE_HISTORY_KEY);
        for (ScreenState screenState : addedScreenStates) {
            restoreStateAndAdd(screenState, Screen.ANIMATION_CODE_NONE);
        }
    }

    void onScreenRemovalConfirmed(Screen screen) {
        addedScreens.remove(screen);
        ViewGroup container = (ViewGroup) activity.findViewById(screen.getContainerId());
        container.removeView(screen.getView());
    }

    public ArrayList<HistoryEntry> getHistory() {
        return new ArrayList<>(history);
    }

    public void addToHistory(HistoryEntry entry) {
        history.add(entry);
    }

    public HistoryEntry popLastHistoryEntry() {
        return history.isEmpty() ? null : history.remove(history.size() - 1);
    }

    protected boolean onBackPressed() {
        for (Screen screen : addedScreens.values()) {
            if (screen.onBackPressed())
                return true;
        }
        return false;
    }

}
