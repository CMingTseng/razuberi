package com.shchurov.razuberi.helpers.history;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreensManager;

import java.util.ArrayList;

public class HistoryScreensManager extends ScreensManager {

    private static final String HISTORY_SAVE_KEY = "razuberi_saved_history";

    private ArrayList<HistoryEntry> history;

    public HistoryScreensManager(Activity activity, Bundle savedState) {
        super(activity, savedState);
        if (savedState == null) {
            history = new ArrayList<>();
        } else {
            history = savedState.getParcelableArrayList(HISTORY_SAVE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putParcelableArrayList(HISTORY_SAVE_KEY, new ArrayList<Parcelable>(history));
    }

    public ArrayList<HistoryEntry> getHistory() {
        return new ArrayList<>(history);
    }

    public void addToHistory(HistoryEntry entry) {
        history.add(entry);
    }

    public HistoryEntry popLastHistoryEntry() {
        return history.isEmpty() ? null : history.remove(history.size() - 1);
    }

}
