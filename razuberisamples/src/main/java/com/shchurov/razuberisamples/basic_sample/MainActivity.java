package com.shchurov.razuberisamples.basic_sample;

import android.os.Bundle;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.core.ScreensActivity;
import com.shchurov.razuberi.history.HistoryScreensManager;
import com.shchurov.razuberisamples.R;

public class MainActivity extends ScreensActivity<HistoryScreensManager> implements OnShowNextScreenListener {

    public static final int ANIMATION_CODE_ADD = 1;
    public static final int ANIMATION_CODE_REMOVE = 2;
    private static final ScreenMetaData[] SCREENS_META_DATA = {
            new ScreenMetaData(R.string.screen_a, R.drawable.photo1, 0xff004d40, 0xff009688, true),
            new ScreenMetaData(R.string.screen_b, R.drawable.photo2, 0xffb71c1c, 0xfff44336, true),
            new ScreenMetaData(R.string.screen_c, R.drawable.photo3, 0xffe65100, 0xffff9800, false),
    };

    private int currentScreenIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getScreensManager().getAddedScreens().isEmpty()) {
            showScreen(0);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.main_activity);
    }

    @Override
    protected HistoryScreensManager createScreensManager(Bundle savedInstanceState) {
        return new HistoryScreensManager(this, savedInstanceState);
    }

    private void showScreen(int screenIndex) {
        String tag = SampleScreen.class.getName() + screenIndex;
        if (getScreensManager().getScreenByTag(tag) != null)
            return;
        ScreenMetaData metaData = SCREENS_META_DATA[screenIndex];
        Screen screen = SampleScreen.createInstance(metaData.nameResId, metaData.photoResId,
                metaData.topColor, metaData.bottomColor, metaData.hasNextButton);
        getScreensManager().replace(screen, R.id.fl_main_container, tag, ANIMATION_CODE_ADD,
                ANIMATION_CODE_REMOVE);
        currentScreenIndex = screenIndex;
    }

    @Override
    public void onShowNextScreen() {
        showScreen(currentScreenIndex + 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currentScreenIndex--;
    }

    private static class ScreenMetaData {

        final int nameResId;
        final int photoResId;
        final int topColor;
        final int bottomColor;
        final boolean hasNextButton;

        public ScreenMetaData(int nameResId, int photoResId, int topColor, int bottomColor, boolean hasNextButton) {
            this.nameResId = nameResId;
            this.photoResId = photoResId;
            this.topColor = topColor;
            this.bottomColor = bottomColor;
            this.hasNextButton = hasNextButton;
        }

    }

}
