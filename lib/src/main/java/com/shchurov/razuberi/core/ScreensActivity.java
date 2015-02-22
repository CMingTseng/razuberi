package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;

public abstract class ScreensActivity<T extends ScreensManager> extends Activity {

    private T screensManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        screensManager = createScreensManager(savedInstanceState);
    }

    protected abstract T createScreensManager(Bundle savedInstanceState);

    protected abstract void setContentView();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        screensManager.saveScreensManagerState(outState);
        super.onSaveInstanceState(outState);
    }

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
