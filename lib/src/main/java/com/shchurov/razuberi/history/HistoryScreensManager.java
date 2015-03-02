package com.shchurov.razuberi.history;

import android.os.Bundle;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreenState;
import com.shchurov.razuberi.core.ScreensActivity;
import com.shchurov.razuberi.core.ScreensManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * HistoryScreensManager supports two additional operations:
 * {@link #replace(com.shchurov.razuberi.core.Screen, int, String, int, int)} and
 * {@link #popHistory(int, int)}. It allows you to easily manage screens' flow.
 */
public class HistoryScreensManager extends ScreensManager {

    private static final String SAVE_HISTORY_KEY = "razuberi_saved_history";
    /**
     * This animation code is passed to {@link Screen#createRemoveAnimation(int)} and
     * {@link Screen#createAddAnimation(int)} when the HistoryScreensManager
     * pops history on back button press.
     */
    public static final int ANIMATION_CODE_BACK_PRESSED = -2;

    private ArrayList<HistoryEntry> history;

    public HistoryScreensManager(ScreensActivity activity, Bundle savedState) {
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

    /**
     * @return the history.
     */
    public ArrayList<HistoryEntry> getHistory() {
        return history;
    }

    /**
     * Replaces all added screens in the container view with the specified one.
     *
     * @param screenToBeAdded     The screen to be added.
     * @param containerId         Id of the container.
     * @param toBeAddedTag        Tag of the screen to be added. It can be used to get the reference to the screen
     *                            by calling {@link #getScreenByTag(String)}.
     * @param addAnimationCode    The animation code to be passed to {@link Screen#createAddAnimation(int)}.
     *                            It is used to specify which animation should be run when the screen is added.
     * @param removeAnimationCode The animation code to be passed to {@link Screen#createRemoveAnimation(int)}.
     *                            It is used to specify which animation should be run before the screen is removed.
     */
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
            history.add(new HistoryEntry(addedScreenStates));
        }
        add(screenToBeAdded, containerId, toBeAddedTag, addAnimationCode);
    }

    /**
     * Opposite to {@link #replace(com.shchurov.razuberi.core.Screen, int, String, int, int)}.
     *
     * @param addAnimationCode    The animation code to be passed to {@link Screen#createAddAnimation(int)}.
     *                            It is used to specify which animation should be run when the screen is added.
     * @param removeAnimationCode The animation code to be passed to {@link Screen#createRemoveAnimation(int)}.
     *                            It is used to specify which animation should be run before the screen is removed.
     */
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
