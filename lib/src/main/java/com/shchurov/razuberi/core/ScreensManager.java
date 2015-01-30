package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ScreensManager {

    private static final String SAVE_KEY = "razuberi_saved_screens";

    private LinkedHashMap<String, Screen> addedScreens;
    private Activity activity;

    public ScreensManager(Activity activity, Bundle savedState) {
        this.activity = activity;
        addedScreens = new LinkedHashMap<>();
        if (savedState != null) {
            restoreState(savedState);
        }
    }

    public void onSaveInstanceState(Bundle instanceState) {
        ArrayList<ScreenState> addedScreenStates = new ArrayList<>();
        for (Screen screen : addedScreens.values()) {
            addedScreenStates.add(screen.getScreenState());
        }
        instanceState.putParcelableArrayList(SAVE_KEY, addedScreenStates);
    }

    public void add(Screen screen, int containerId, String tag) {
        addedScreens.put(tag, screen);
        View screenView = screen.performAdd(activity, tag, containerId);
        ViewGroup container = (ViewGroup) activity.findViewById(containerId);
        container.addView(screenView);
    }

    public Screen restoreStateAndAdd(ScreenState screenState) {
        Screen screen = null;
        try {
            screen = screenState.getScreenClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addedScreens.put(screenState.getTag(), screen);
        View screenView = screen.performAdd(activity, screenState);
        ViewGroup container = (ViewGroup) activity.findViewById(screenState.getContainerId());
        container.addView(screenView);
        return screen;
    }

    public void remove(Screen screen) {
        screen.performRemove();
        addedScreens.remove(screen);
        ViewGroup container = (ViewGroup) activity.findViewById(screen.getContainerId());
        container.removeView(screen.getView());
    }

    public ScreenState getStateAndRemove(Screen screen) {
        ScreenState screenState = screen.getScreenState();
        remove(screen);
        return screenState;
    }



    public ArrayList<Screen> getAddedScreens() {
        return new ArrayList<>(addedScreens.values());
    }

    public Screen getScreenByTag(String tag) {
        return addedScreens.get(tag);
    }

    private void restoreState(Bundle savedState) {
        ArrayList<ScreenState> addedScreenStates = savedState.getParcelableArrayList(SAVE_KEY);
        for (ScreenState screenState : addedScreenStates) {
            restoreStateAndAdd(screenState);
        }
    }

    protected boolean onBackPressed() {
        for (Screen screen : addedScreens.values()) {
            if (screen.onBackPressed())
                return true;
        }
        return false;
    }

}
