package com.shchurov.razuberi.core;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * It is used to manage {@link com.shchurov.razuberi.core.Screen}s.
 * Call {@link ScreensActivity#getScreensManager()} to obtain an instance.
 */
public class ScreensManager {

    private static final String SAVE_ADDED_SCREENS_KEY = "razuberi_saved_screens";
    static final int ANIMATION_CODE_ACTIVITY_RE_INSTANTIATE = -1;

    private LinkedHashMap<String, Screen> addedScreens;
    private ScreensActivity activity;

    public ScreensManager(ScreensActivity activity, Bundle savedState) {
        this.activity = activity;
        addedScreens = new LinkedHashMap<>();
        restoreScreensManagerState(savedState);
    }

    protected void restoreScreensManagerState(Bundle savedState) {
        if (savedState == null)
            return;
        ArrayList<ScreenState> addedScreenStates = savedState.getParcelableArrayList(SAVE_ADDED_SCREENS_KEY);
        for (ScreenState screenState : addedScreenStates) {
            restoreStateAndAdd(screenState, ANIMATION_CODE_ACTIVITY_RE_INSTANTIATE);
        }
    }

    public void saveScreensManagerState(Bundle instanceState) {
        ArrayList<ScreenState> addedScreenStates = new ArrayList<>();
        for (Screen screen : addedScreens.values()) {
            addedScreenStates.add(screen.getScreenState());
        }
        instanceState.putParcelableArrayList(SAVE_ADDED_SCREENS_KEY, addedScreenStates);
    }

    /**
     * Adds a screen to an Activity.
     *
     * @param screen        The screen to be added.
     * @param containerId   Id of the view where the screen's view is to be added.
     * @param screenTag     Tag of the screen to be added. It can be used to get the reference to the screen
     *                      by calling {@link #getScreenByTag(String)}.
     * @param animationCode The animation code to be passed to {@link Screen#createAddAnimation(int)}.
     *                      It is used to specify which animation should be run when the screen is added.
     */
    public void add(Screen screen, int containerId, String screenTag, int animationCode) {
        add(screen, containerId, screenTag, null, null, animationCode);
    }

    /**
     * Restores the screen and add it to an Activity.
     *
     * @param screenState   Saved state of the screen.
     * @param animationCode The animation code to be passed to {@link Screen#createAddAnimation(int)}.
     *                      It is used to specify which animation should be run when the screen is added.
     * @return The restored and added screen.
     */
    public Screen restoreStateAndAdd(ScreenState screenState, int animationCode) {
        Screen screen;
        try {
            screen = screenState.getScreenClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(screenState.getScreenClass().getName() + " should have only default constructor.");
        }
        add(screen, screenState.getContainerId(), screenState.getTag(),
                screenState.getPersistentData(), screenState.getViewState(), animationCode);
        return screen;
    }

    private void add(Screen screen, int containerId, String screenTag, Bundle persistentData,
                     SparseArray<Parcelable> viewState, int animationCode) {
        addedScreens.put(screenTag, screen);
        ViewGroup container = (ViewGroup) activity.findViewById(containerId);
        View screenView = screen.performAdd(this, screenTag, container, persistentData, viewState, animationCode);
        container.addView(screenView);
        screen.performOnStart();
    }

    /**
     * Removes the screen from an Activity. Notice that the screen's view will be removed only when the
     * screen's method {@link com.shchurov.razuberi.core.Screen#confirmViewRemoval()} is called.
     *
     * @param screen        The screen to be removed.
     * @param animationCode The animation code to be passed to {@link Screen#createRemoveAnimation(int)}.
     *                      It is used to specify which animation should be run before the screen is removed.
     */
    public void remove(Screen screen, int animationCode) {
        screen.performOnStop();
        screen.performRemove(animationCode);
        addedScreens.remove(screen.getTag());
    }

    /**
     * Copies the screen's state and removes it from an Activity. Notice that the screen's view will be removed only when the
     * screen's method {@link com.shchurov.razuberi.core.Screen#confirmViewRemoval()} is called.
     *
     * @param screen        The screen to be removed.
     * @param animationCode The animation code to be passed to {@link Screen#createRemoveAnimation(int)}.
     *                      It is used to specify which animation should be run before the screen is removed.
     * @return State of the removed screen.
     */
    public ScreenState getStateAndRemove(Screen screen, int animationCode) {
        ScreenState screenState = screen.getScreenState();
        remove(screen, animationCode);
        return screenState;
    }

    void onScreenViewRemovalConfirmed(Screen screen) {
        ViewGroup container = (ViewGroup) activity.findViewById(screen.getContainerId());
        container.removeView(screen.getView());
    }

    public void onActivityStart() {
        for (Screen screen : addedScreens.values()) {
            screen.performOnStart();
        }
    }

    public void onActivityStop() {
        for (Screen screen : addedScreens.values()) {
            screen.performOnStop();
        }
    }

    public void onActivityDestroy() {
        for (Screen screen : addedScreens.values()) {
            screen.onRemove();
            screen.confirmViewRemoval();
        }
    }

    /**
     * @return A list of the added screens.
     */
    public ArrayList<Screen> getAddedScreens() {
        return new ArrayList<>(addedScreens.values());
    }

    /**
     * Finds a screen that was added with the given tag.
     *
     * @param tag The tag to search for.
     * @return a screen with the given tag, null otherwise.
     */
    public Screen getScreenByTag(String tag) {
        return addedScreens.get(tag);
    }

    /**
     * @return host {@link com.shchurov.razuberi.core.ScreensActivity}
     */
    public ScreensActivity getActivity() {
        return activity;
    }

    /**
     * It's easier to perform any screen transitions when UI is in consistent state. Call this to know
     * if there are any playing transition animations.
     *
     * @return true if any of the added screens hasn't finished its addition or removal animation yet.
     */
    public boolean isAnimationInProgress() {
        for (Screen screen : addedScreens.values()) {
            if (screen.isAddAnimationInProgress() || screen.isRemoveAnimationInProgress())
                return true;
        }
        return false;
    }

    public boolean onBackPressed() {
        for (Screen screen : addedScreens.values()) {
            if (screen.onBackPressed())
                return true;
        }
        return false;
    }

}
