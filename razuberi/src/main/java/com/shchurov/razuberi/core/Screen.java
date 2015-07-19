package com.shchurov.razuberi.core;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>Screen is a piece of the user interface that is contained by {@link com.shchurov.razuberi.core.ScreensActivity}.
 * Screens are managed by {@link com.shchurov.razuberi.core.ScreensManager}. Every class that extends
 * Screen must not have any other constructors than no-argument constructor, it allows the system to
 * re-instantiate screens when needed.
 * <h3>Lifecycle</h3>
 * <ol>
 * <li> {@link #onAdd(android.view.ViewGroup, boolean)} called when the screen is going to
 * be added to a {@link com.shchurov.razuberi.core.ScreensActivity}.
 * <li> {@link #onStart()} called after {@link #onAdd(android.view.ViewGroup, boolean)}
 * or from {@link android.app.Activity#onStart()}
 * <li> {@link #onStop()} called before {@link #onRemove()} or from {@link android.app.Activity#onStop()}
 * <li> {@link #onRemove()} called when the screen is going to
 * be removed from a {@link com.shchurov.razuberi.core.ScreensActivity}.
 * </ol>
 */

public abstract class Screen {

    private ScreensManager screensManager;
    private View view;
    private String tag;
    private int containerId = -1;
    private Bundle persistentData = new Bundle();
    private boolean onStartCalled;
    private boolean onStopCalled;

    View performAdd(ScreensManager screensManager, String tag, ViewGroup container, Bundle persistentData,
            SparseArray<Parcelable> viewState, int animationCode) {
        this.screensManager = screensManager;
        this.tag = tag;
        this.containerId = container.getId();
        if (persistentData != null) {
            this.persistentData = persistentData;
        }
        view = onAdd(container, viewState != null);
        if (viewState != null) {
            view.restoreHierarchyState(viewState);
        }
        if (animationCode != ScreensManager.ANIMATION_CODE_ACTIVITY_RE_INSTANTIATE) {
            createAddAnimation(animationCode);
        }
        return view;
    }

    ScreenState getScreenState() {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        onSaveState();
        return new ScreenState(getClass(), containerId, tag, viewState, persistentData);
    }

    /**
     * Called when the screen is going to be added to a {@link com.shchurov.razuberi.core.ScreensActivity}.
     *
     * @param parentView The parent view which this screen will be added to.
     * It can be used to generate proper LayoutParams for the screen's view.
     * @param restoring False if the screen is added for the first time, true otherwise.
     * @return the screen's view.
     */
    protected abstract View onAdd(ViewGroup parentView, boolean restoring);

    /**
     * Called after {@link #onAdd(android.view.ViewGroup, boolean)} to let you initialize an adding animation.
     *
     * @param animationCode The animation code that was passed to {@link com.shchurov.razuberi.core.ScreensManager#add(Screen, int, String, int)}
     * or {@link com.shchurov.razuberi.core.ScreensManager#restoreStateAndAdd(ScreenState, int)}. It is used to determine
     * which animation should be run when the screen is added.
     */
    protected void createAddAnimation(int animationCode) {
    }

    void performOnStart() {
        if (onStartCalled)
            return;
        onStartCalled = true;
        onStopCalled = false;
        onStart();
    }

    /**
     * Called in two cases:
     * 1. After {@link #onAdd(android.view.ViewGroup, boolean)};
     * 2. From {@link ScreensActivity#onStart()}.
     * If both of the events come in a row, the method will be called only on the first one.
     * It's a good place to start a background thread, register for an event-bus, etc.
     */
    protected void onStart() {
    }

    void performOnStop() {
        if (onStopCalled)
            return;
        onStopCalled = true;
        onStartCalled = false;
        onStop();
    }

    /**
     * Called before {@link #onStop()} when the screen is going to be removed and is expected to be restored later.
     * It's a good place to save any data that you want to retain.
     * Use {@link android.os.Bundle} from {@link #getPersistentData()} for this purpose.
     */
    protected void onSaveState() {
    }

    /**
     * Called in two cases:
     * 1. Before {@link #onRemove()}
     * 2. From {@link ScreensActivity#onStop()}
     * If both of the events come in a row, the method will be called only on the first one.
     * It's a good place to stop an associated background thread, unregister from an event-bus, etc.
     */
    protected void onStop() {
    }

    void performRemove(int animationCode) {
        onRemove();
        createRemoveAnimation(animationCode);
    }

    /**
     * Called in two cases:
     * 1. When the screen is explicitly removed
     * 2. From {@link ScreensActivity#onDestroy()}
     */
    protected void onRemove() {
    }

    /**
     * Called after {@link #onRemove()} to let you initialize a removal animation.
     * The screen's view will be removed only after calling {@link #confirmViewRemoval()}.
     * Default implementation of this method just calls {@link #confirmViewRemoval()} immediately.
     * However if you want to run some removal animation, call {@link #confirmViewRemoval()} in the end of it.
     *
     * @param animationCode The animation code that was passed to
     * {@link com.shchurov.razuberi.core.ScreensManager#remove(Screen, int)} or
     * {@link com.shchurov.razuberi.core.ScreensManager#getStateAndRemove(Screen, int)}. It is used to determine
     * which animation should be run before the screen is removed.
     */
    protected void createRemoveAnimation(int animationCode) {
        confirmViewRemoval();
    }

    /**
     * Called from {@link android.app.Activity#onBackPressed()}.
     *
     * @return true if the screen consumes this back button press event, false otherwise.
     */
    protected boolean onBackPressed() {
        return false;
    }

    /**
     * @return the screen's parent view id, -1 otherwise.
     */
    public final int getContainerId() {
        return containerId;
    }

    /**
     * Use it to keep all data that must be persistent from the moment when you
     * add this screen until the moment when you remove it.
     *
     * @return a {@link android.os.Bundle} that is persistent across parent Activity instances.
     */
    public Bundle getPersistentData() {
        return persistentData;
    }

    /**
     * @return the {@link com.shchurov.razuberi.core.ScreensManager} that is responsible for this screen.
     */
    public ScreensManager getScreensManager() {
        return screensManager;
    }

    /**
     * @return the parent {@link com.shchurov.razuberi.core.ScreensActivity}.
     */
    public ScreensActivity getActivity() {
        return screensManager.getActivity();
    }

    /**
     * @return true if this screen is added.
     */
    public boolean isAdded() {
        return screensManager.getScreenByTag(tag) != null;
    }

    /**
     * @return the tag that was passed to {@link com.shchurov.razuberi.core.ScreensManager#add(Screen, int, String, int)}.
     */
    public String getTag() {
        return tag;
    }

    /**
     * @return the screen's view that was created in {@link #onAdd(android.view.ViewGroup, boolean)}.
     */
    public View getView() {
        return view;
    }

    /**
     * Call this method to confirm that the screen's view is ready to be removed. Usually
     * called in the end of removal animation that was started in {@link #createRemoveAnimation(int)}.
     */
    protected void confirmViewRemoval() {
        screensManager.onScreenViewRemovalConfirmed(this);
        this.screensManager = null;
        this.containerId = 0;
        this.view = null;
    }

    protected String getString(int resId) {
        return getActivity().getString(resId);
    }

}
