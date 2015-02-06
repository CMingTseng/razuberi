package com.shchurov.razuberisamples.basic_sample;

import android.os.Parcel;
import android.os.Parcelable;

import com.shchurov.razuberi.core.ScreenState;

import java.util.LinkedList;

public class ListItem implements Parcelable {

    public int color;
    public String description;

    public ListItem(int color, String description) {
        this.color = color;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>() {

        public ListItem createFromParcel(Parcel in) {
            int color = in.readInt();
            String description = in.readString();
            return new ListItem(color, description);
        }

        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

}
