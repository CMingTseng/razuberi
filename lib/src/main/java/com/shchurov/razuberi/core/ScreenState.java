package com.shchurov.razuberi.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

public class ScreenState implements Parcelable {

    private static final String BUNDLE_HACK_KEY = "key";

    private Class<? extends Screen> screenClass;
    private int containerId;
    private String tag;
    private SparseArray<Parcelable> viewState;
    private Bundle persistentData;

    ScreenState(Class<? extends Screen> screenClass, int containerId, String tag,
                SparseArray<Parcelable> viewState, Bundle persistentData) {
        this.screenClass = screenClass;
        this.containerId = containerId;
        this.tag = tag;
        this.viewState = viewState;
        this.persistentData = persistentData;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }

    int getContainerId() {
        return containerId;
    }

    public String getTag() {
        return tag;
    }

    SparseArray<Parcelable> getViewState() {
        return viewState;
    }

    public Bundle getPersistentData() {
        return persistentData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(screenClass);
        dest.writeInt(containerId);
        dest.writeString(tag);
        Bundle hackBundle = new Bundle();
        hackBundle.putSparseParcelableArray(BUNDLE_HACK_KEY, viewState);
        dest.writeBundle(hackBundle);
        dest.writeBundle(persistentData);
    }

    public static final Parcelable.Creator<ScreenState> CREATOR = new Parcelable.Creator<ScreenState>() {

        public ScreenState createFromParcel(Parcel in) {
            Class<? extends Screen> screenClass = (Class<? extends Screen>) in.readSerializable();
            int containerId = in.readInt();
            String tag = in.readString();
            Bundle hackBundle = in.readBundle();
            SparseArray<Parcelable> viewState = hackBundle.getSparseParcelableArray(BUNDLE_HACK_KEY);
            Bundle persistentData = in.readBundle();
            return new ScreenState(screenClass, containerId, tag, viewState, persistentData);
        }

        public ScreenState[] newArray(int size) {
            return new ScreenState[size];
        }
    };

}
