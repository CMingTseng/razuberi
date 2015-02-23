package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ScreensManager {

    private static final String SAVE_ADDED_SCREENS_KEY = "razuberi_saved_screens";
    public static final int ANIMATION_CODE_NONE = -1;

    private LinkedHashMap<String, Screen> addedScreens;
    private Activity activity;

    public ScreensManager(Activity activity, Bundle savedState) {
        this.activity = activity;
        addedScreens = new LinkedHashMap<>();
        restoreScreensManagerState(savedState);
    }

    protected void restoreScreensManagerState(Bundle savedState) {
        if (savedState == null)
            return;
        ArrayList<ScreenState> addedScreenStates = savedState.getParcelableArrayList(SAVE_ADDED_SCREENS_KEY);
        for (ScreenState screenState : addedScreenStates) {
            restoreStateAndAdd(screenState, ANIMATION_CODE_NONE);
        }
    }

    protected void saveScreensManagerState(Bundle instanceState) {
        ArrayList<ScreenState> addedScreenStates = new ArrayList<>();
        for (Screen screen : addedScreens.values()) {
            screen.onActivitySaveInstanceState();
            addedScreenStates.add(screen.getScreenState());
        }
        instanceState.putParcelableArrayList(SAVE_ADDED_SCREENS_KEY, addedScreenStates);
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
        ViewGroup container = (ViewGroup) activity.findViewById(containerId);
        View screenView = screen.performAdd(this, screenTag, container, persistentData, viewState, animationCode);
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

    void onScreenRemovalConfirmed(Screen screen) {
        addedScreens.remove(screen.getTag());
        ViewGroup container = (ViewGroup) activity.findViewById(screen.getContainerId());
        container.removeView(screen.getView());
    }

    protected void onActivityStart() {
        for (Screen screen : addedScreens.values()) {
            screen.onActivityStart();
        }
    }

    protected void onActivityStop() {
        for (Screen screen : addedScreens.values()) {
            screen.onActivityStop();
        }
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

    protected boolean onBackPressed() {
        for (Screen screen : addedScreens.values()) {
            if (screen.onBackPressed())
                return true;
        }
        return false;
    }

}
