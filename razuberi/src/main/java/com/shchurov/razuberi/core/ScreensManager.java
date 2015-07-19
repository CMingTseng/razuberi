package com.shchurov.razuberi.core;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * It is used to manage {@link com.shchurov.razuberi.core.Screen}s.
 * Call {@link ScreensActivity#getScreensManager()} to obtain an instance.
 */
public class ScreensManager {

    private static final String SAVE_ADDED_SCREENS_KEY = "razuberi_saved_screens";
    /**
     * This animation code is passed to {@link Screen#createRemoveAnimation(int)} and
     * {@link Screen#createAddAnimation(int)} when the host-activity was re-instantiated.
     * Usually you must not run any animations on this code.
     */
    static final int ANIMATION_CODE_ACTIVITY_RE_INSTANTIATE = -1;

    private LinkedHashMap<String, Screen> addedScreens;
    private LinkedList<Screen> removingScreens;
    private ScreensActivity activity;

    public ScreensManager(ScreensActivity activity, Bundle savedState) {
        this.activity = activity;
        addedScreens = new LinkedHashMap<>();
        removingScreens = new LinkedList<>();
        restoreScreensManagerState(savedState);
    }

    protected void restoreScreensManagerState(Bundle savedState) {
        if (savedState == null)
            return;
        ArrayList<ScreenState> addedScreenStates = savedState.getParcelableArrayList(SAVE_ADDED_SCREENS_KEY);
        if (addedScreenStates == null)
            return;
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
     * @param screen The screen to be added.
     * @param containerId Id of the view where the screen's view is to be added.
     * @param screenTag Tag of the screen to be added. It can be used to get the reference to the screen
     * by calling {@link #getScreenByTag(String)}.
     * @param animationCode The animation code to be passed to {@link Screen#createAddAnimation(int)}.
     * It is used to specify which animation should be run when the screen is added.
     */
    public void add(Screen screen, int containerId, String screenTag, int animationCode) {
        add(screen, containerId, screenTag, null, null, animationCode);
    }

    /**
     * Restores the screen and add it to an Activity.
     *
     * @param screenState Saved state of the screen.
     * @param animationCode The animation code to be passed to {@link Screen#createAddAnimation(int)}.
     * It is used to specify which animation should be run when the screen is added.
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
        if (screenTag == null) {
            throw new IllegalArgumentException("Tag can't be null.");
        }
        if (addedScreens.get(screenTag) != null) {
            throw new IllegalStateException("A screen with this tag is already added.");
        }
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
     * @param screen The screen to be removed.
     * @param animationCode The animation code to be passed to {@link Screen#createRemoveAnimation(int)}.
     * It is used to specify which animation should be run before the screen is removed.
     */
    public void remove(Screen screen, int animationCode) {
        if (addedScreens.get(screen.getTag()) == null) {
            throw new IllegalStateException("The screen is not added.");
        }
        screen.performOnStop();
        addedScreens.remove(screen.getTag());
        removingScreens.add(screen);
        screen.performRemove(animationCode);
    }

    /**
     * Copies the screen's state and removes it from an Activity. Notice that the screen's view will be removed only when the
     * screen's method {@link com.shchurov.razuberi.core.Screen#confirmViewRemoval()} is called.
     *
     * @param screen The screen to be removed.
     * @param animationCode The animation code to be passed to {@link Screen#createRemoveAnimation(int)}.
     * It is used to specify which animation should be run before the screen is removed.
     * @return State of the removed screen.
     */
    public ScreenState getStateAndRemove(Screen screen, int animationCode) {
        ScreenState screenState = screen.getScreenState();
        remove(screen, animationCode);
        return screenState;
    }

    protected void onScreenViewRemovalConfirmed(Screen screen) {
        removingScreens.remove(screen);
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
     * @return host {@link com.shchurov.razuberi.core.ScreensActivity}.
     */
    public ScreensActivity getActivity() {
        return activity;
    }

    /**
     * @return the list of screens that are running removal animation.
     */
    public List<Screen> getRemovingScreens() {
        return removingScreens;
    }

    public boolean onBackPressed() {
        for (Screen screen : addedScreens.values()) {
            if (screen.onBackPressed())
                return true;
        }
        return false;
    }

}
