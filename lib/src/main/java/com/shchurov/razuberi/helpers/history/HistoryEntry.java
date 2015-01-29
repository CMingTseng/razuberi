package com.shchurov.razuberi.helpers.history;

import android.os.Parcel;
import android.os.Parcelable;

import com.shchurov.razuberi.core.ScreenState;

import java.util.LinkedList;

public class HistoryEntry implements Parcelable {

    private LinkedList<ScreenState> screenStates;

    public HistoryEntry(LinkedList<ScreenState> screenStates) {
        this.screenStates = screenStates;
    }

    public HistoryEntry(ScreenState screenState) {
        this.screenStates = new LinkedList<>();
        screenStates.add(screenState);
    }

    public LinkedList<ScreenState> getEntryScreens() {
        return  screenStates;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(screenStates);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<HistoryEntry> CREATOR = new Parcelable.Creator<HistoryEntry>() {

        public HistoryEntry createFromParcel(Parcel in) {
            LinkedList<ScreenState> screens = new LinkedList<>();
            in.readTypedList(screens, ScreenState.CREATOR);
            return new HistoryEntry(screens);
        }

        public HistoryEntry[] newArray(int size) {
            return new HistoryEntry[size];
        }
    };

}
