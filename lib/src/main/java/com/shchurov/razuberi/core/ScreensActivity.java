package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;

/**
 * This subclass of {@link android.app.Activity} manages {@link com.shchurov.razuberi.core.ScreensManager} lifecycle.
 * @param <T> Appropriate implementation of {@link com.shchurov.razuberi.core.ScreensManager}.
 */
public abstract class ScreensActivity<T extends ScreensManager> extends Activity {

    private T screensManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        screensManager = createScreensManager(savedInstanceState);
    }

    /**
     * Called to initialize a proper {@link com.shchurov.razuberi.core.ScreensManager} for the Activity.
     *
     * @param savedInstanceState You should pass it to
     * {@link com.shchurov.razuberi.core.ScreensManager#ScreensManager(ScreensActivity, android.os.Bundle)} constructor.
     * @return The {@link com.shchurov.razuberi.core.ScreensManager} for the Activity.
     */
    protected abstract T createScreensManager(Bundle savedInstanceState);

    /**
     * Call {@link android.app.Activity#setContentView(int)} (or overloaded methods) here
     * instead of calling it in {@link android.app.Activity#onCreate(android.os.Bundle)}.
     * This is needed to let you add screens in {@link android.app.Activity#onCreate(android.os.Bundle)}.
     */
    protected abstract void setContentView();

    @Override
    protected void onStart() {
        super.onStart();
        screensManager.onActivityStart();
    }

    @Override
    protected void onStop() {
        screensManager.onActivityStop();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        screensManager.saveScreensManagerState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * @return The Activity's {@link com.shchurov.razuberi.core.ScreensManager}.
     */
    protected T getScreensManager() {
        return screensManager;
    }

    @Override
    public void onBackPressed() {
        if (!getScreensManager().onBackPressed()) {
            super.onBackPressed();
        }
    }
}
