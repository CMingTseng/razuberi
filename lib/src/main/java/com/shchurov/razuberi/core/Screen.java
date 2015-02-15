package com.shchurov.razuberi.core;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

public abstract class Screen {

    public static final int ANIMATION_CODE_NONE = -1;

    private ScreensManager screensManager;
    private View view;
    private String tag;
    private int containerId;
    private Bundle persistentData = new Bundle();

    View performAdd(ScreensManager screensManager, String tag, int containerId, Bundle persistentData,
                    SparseArray<Parcelable> viewState, int animationCode) {
        this.screensManager = screensManager;
        this.tag = tag;
        this.containerId = containerId;
        if (persistentData != null) {
            this.persistentData = persistentData;
        }
        view = onAdd(animationCode, viewState != null);
        if (viewState != null) {
            view.restoreHierarchyState(viewState);
        }
        return view;
    }

    void performRemove(int animationCode) {
        onRemove(animationCode);
    }

    ScreenState getScreenState() {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        return new ScreenState(getClass(), containerId, tag, viewState, persistentData);
    }

    protected abstract View onAdd(int animationCode, boolean restoringState);

    protected void onActivitySaveInstanceState() {
    }

    protected void onRemove(int animationCode) {
        confirmRemoval();
    }

    protected boolean onBackPressed() {
        return false;
    }

    public final int getContainerId() {
        return containerId;
    }

    public Bundle getPersistentData() {
        return persistentData;
    }

    public ScreensManager getScreensManager() {
        return screensManager;
    }

    public Activity getActivity() {
        return screensManager.getActivity();
    }

    public boolean isAdded() {
        return view != null;
    }

    public String getTag() {
        return tag;
    }

    protected View getView() {
        return view;
    }

    protected void confirmRemoval() {
        screensManager.onScreenRemovalConfirmed(this);
        this.screensManager = null;
        this.containerId = 0;
        this.view = null;
    }

}
