package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public abstract class Screen {

    private Activity activity;
    private View view;
    private String tag;
    private int containerId;

    View performAdd(Activity activity, String tag, int containerId) {
        this.activity = activity;
        this.tag = tag;
        this.containerId = containerId;
        view = onAdd(null);
        return view;
    }

    View performAdd(Activity activity, ScreenState screenState) {
        this.activity = activity;
        this.tag = screenState.getTag();
        view = onAdd(screenState.getSavedInstanceState());
        view.restoreHierarchyState(screenState.getViewState());
        return view;
    }

    void performRemove() {
        onRemove();
        this.activity = null;
        this.containerId = 0;
        this.view = null;
    }

    ScreenState getScreenState() {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        Bundle clientState = onSaveInstanceState();
        return new ScreenState(getClass(), containerId, tag, viewState, clientState);
    }

    public final int getContainerId() {
        return containerId;
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean isAdded() {
        return view != null;
    }

    public String getTag() {
        return tag;
    }

    protected abstract View onAdd(Bundle savedInstanceState);

    protected Bundle onSaveInstanceState() {
        return null;
    }

    protected void onRemove() {
    }

    protected View getView() {
        return view;
    }

    protected boolean onBackPressed() {
        return false;
    }

}
