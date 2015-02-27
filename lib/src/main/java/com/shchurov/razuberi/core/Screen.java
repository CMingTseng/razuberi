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
 * <p/>
 * <h3>Lifecycle</h3>
 * <p/>
 * <ol>
 * <li> {@link #onAdd(android.view.ViewGroup, int)} called when the screen's view is going to
 * be added to a {@link com.shchurov.razuberi.core.ScreensActivity}'s view hierarchy.
 * <li> {@link #onActivityStart()} called from {@link android.app.Activity#onStart()}.
 * <li> {@link #onActivitySaveInstanceState()} called from {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)}.
 * <li> {@link #onActivityStop()} called from {@link android.app.Activity#onStop()}.
 * <li> {@link #onRemove(int)} called when the screen's view is going to
 * be removed from a {@link com.shchurov.razuberi.core.ScreensActivity}'s view hierarchy.
 * </ol>
 */

public abstract class Screen {

    private ScreensManager screensManager;
    private View view;
    private String tag;
    private int containerId = -1;
    private Bundle persistentData = new Bundle();

    View performAdd(ScreensManager screensManager, String tag, ViewGroup container, Bundle persistentData,
                    SparseArray<Parcelable> viewState, int animationCode) {
        this.screensManager = screensManager;
        this.tag = tag;
        this.containerId = container.getId();
        if (persistentData != null) {
            this.persistentData = persistentData;
        }
        view = onAdd(container, animationCode);
        if (viewState != null) {
            view.restoreHierarchyState(viewState);
        }
        return view;
    }

    ScreenState getScreenState() {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        return new ScreenState(getClass(), containerId, tag, viewState, persistentData);
    }

    /**
     * Called when the screen's view is going to be added to a {@link com.shchurov.razuberi.core.ScreensActivity}'s view hierarchy.
     *
     * @param parentView    The parent view which this screen will be added to.
     *                      It can be used to generate proper LayoutParams for the screen's view.
     * @param animationCode The animation code that was passed to {@link com.shchurov.razuberi.core.ScreensManager#add(Screen, int, String, int)}
     *                      or {@link com.shchurov.razuberi.core.ScreensManager#restoreStateAndAdd(ScreenState, int)}. It is used to determine
     *                      which animation should be run when the screen is added. When the parent Activity is re-instantiated
     *                      the animationCode is {@link com.shchurov.razuberi.core.ScreensManager#ANIMATION_CODE_ACTIVITY_RE_INSTANTIATE}.
     * @return the screen's view.
     */
    protected abstract View onAdd(ViewGroup parentView, int animationCode);

    /**
     * Called from {@link android.app.Activity#onStart()}.
     */
    protected void onActivityStart() {
    }

    /**
     * Called from {@link android.app.Activity#onStop()}. It's a good place to clean
     * anything that may cause a memory leak.
     */
    protected void onActivityStop() {
    }

    /**
     * Called from {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)}.
     * It's a good place to save any data you want to retain across Activity instances.
     * Use {@link android.os.Bundle} from {@link #getPersistentData()} for this purpose.
     */
    protected void onActivitySaveInstanceState() {
    }


    /**
     * Called when you remove the screen.
     * The screen will be removed only after calling {@link #confirmRemoval()}.
     * Default implementation of this method just calls {@link #confirmRemoval()} immediately.
     * However if you want to run some removal animation, call {@link #confirmRemoval()} in the end of it.
     *
     * @param animationCode The animation code that was passed to
     * {@link com.shchurov.razuberi.core.ScreensManager#remove(Screen, int)} or
     * {@link com.shchurov.razuberi.core.ScreensManager#getStateAndRemove(Screen, int)}. It is used to determine
     * which animation should be run before the screen is removed.
     */
    protected void onRemove(int animationCode) {
        confirmRemoval();
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
     * @return true if this screen's view is added to an Activity's view hierarchy, false otherwise.
     */
    public boolean isAdded() {
        return view != null;
    }

    /**
     * @return the tag that was passed to {@link com.shchurov.razuberi.core.ScreensManager#add(Screen, int, String, int)}.
     */
    public String getTag() {
        return tag;
    }

    /**
     * @return the screen's view that was created in {@link #onAdd(android.view.ViewGroup, int)}.
     */
    protected View getView() {
        return view;
    }

    /**
     * Call this method to confirm that the screen has finished its internal removal process. Usually
     * called in the end of removal animation that is started in {@link #onRemove(int)}.
     */
    protected void confirmRemoval() {
        screensManager.onScreenRemovalConfirmed(this);
        this.screensManager = null;
        this.containerId = 0;
        this.view = null;
    }

}
