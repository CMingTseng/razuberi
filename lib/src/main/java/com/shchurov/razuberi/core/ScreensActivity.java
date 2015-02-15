package com.shchurov.razuberi.core;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public abstract class ScreensActivity extends Activity {

    private ScreensManager screensManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        screensManager = new ScreensManager(this, savedInstanceState);
    }

    protected abstract void setContentView();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        screensManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    protected ScreensManager getScreensManager() {
        return screensManager;
    }

}
