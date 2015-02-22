package com.shchurov.razuberi.history;

import android.app.Activity;
import android.os.Bundle;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreenState;
import com.shchurov.razuberi.core.ScreensManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class HistoryScreensManager extends ScreensManager {

    private static final String SAVE_HISTORY_KEY = "razuberi_saved_history";
    public static final int ANIMATION_CODE_BACK_PRESSED = -2;

    private ArrayList<HistoryEntry> history;

    public HistoryScreensManager(Activity activity, Bundle savedState) {
        super(activity, savedState);
    }

    @Override
    protected void restoreScreensManagerState(Bundle savedState) {
        if (savedState == null) {
            history = new ArrayList<>();
        } else {
            history = savedState.getParcelableArrayList(SAVE_HISTORY_KEY);
        }
        super.restoreScreensManagerState(savedState);
    }

    @Override
    protected void saveScreensManagerState(Bundle instanceState) {
        instanceState.putParcelableArrayList(SAVE_HISTORY_KEY, history);
        super.saveScreensManagerState(instanceState);
    }

    public ArrayList<HistoryEntry> getHistory() {
        return history;
    }

    public void addToHistory(HistoryEntry entry) {
        history.add(entry);
    }

    public void replace(Screen screenToBeAdded, int containerId, String toBeAddedTag, int addAnimationCode, int removeAnimationCode) {
        LinkedList<ScreenState> addedScreenStates = new LinkedList<>();
        LinkedList<Screen> addedScreens = new LinkedList<>(getAddedScreens());
        for (int i = 0; i < addedScreens.size(); i++) {
            Screen screen = addedScreens.get(i);
            if (screen.getContainerId() == containerId) {
                addedScreenStates.add(getStateAndRemove(screen, removeAnimationCode));
            }
        }
        if (!addedScreenStates.isEmpty()) {
            addToHistory(new HistoryEntry(addedScreenStates));
        }
        add(screenToBeAdded, containerId, toBeAddedTag, addAnimationCode);
    }

    public void popHistory(int addAnimationCode, int removeAnimationCode) {
        if (history.isEmpty())
            return;
        HistoryEntry historyEntry = history.remove(history.size() - 1);
        int containerId = historyEntry.getEntryScreenStates().get(0).getContainerId();
        LinkedList<Screen> addedScreens = new LinkedList<>(getAddedScreens());
        for (int i = 0; i < addedScreens.size(); i++) {
            Screen screen = addedScreens.get(i);
            if (screen.getContainerId() == containerId) {
                remove(screen, removeAnimationCode);
            }
        }
        LinkedList<ScreenState> entryScreenStates = historyEntry.getEntryScreenStates();
        for (int i = 0; i < entryScreenStates.size(); i++) {
            restoreStateAndAdd(entryScreenStates.get(i), addAnimationCode);
        }
    }

    @Override
    protected boolean onBackPressed() {
        if (super.onBackPressed())
            return true;
        if (history.isEmpty())
            return false;
        popHistory(ANIMATION_CODE_BACK_PRESSED, ANIMATION_CODE_BACK_PRESSED);
        return true;
    }

}
