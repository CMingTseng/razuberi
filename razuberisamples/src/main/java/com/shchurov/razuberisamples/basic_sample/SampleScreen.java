package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberi.history.HistoryScreensManager;
import com.shchurov.razuberisamples.R;

public class SampleScreen extends Screen {

    private static final String ARGS_NAME = "name";
    private static final String ARGS_PHOTO = "photo";
    private static final String ARGS_TOP_COLOR = "top_color";
    private static final String ARGS_BOTTOM_COLOR = "bottom_color";
    private static final String ARGS_HAS_NEXT_BUTTON = "has_next_button";

    public static SampleScreen createInstance(int nameResId, int photoResId, int topColor,
            int bottomColor, boolean hasNextButton) {
        SampleScreen screen = new SampleScreen();
        Bundle args = screen.getPersistentData();
        args.putInt(ARGS_NAME, nameResId);
        args.putInt(ARGS_PHOTO, photoResId);
        args.putInt(ARGS_TOP_COLOR, topColor);
        args.putInt(ARGS_BOTTOM_COLOR, bottomColor);
        args.putBoolean(ARGS_HAS_NEXT_BUTTON, hasNextButton);
        return screen;
    }

    @Override
    protected View onAdd(ViewGroup parentView, boolean restoring) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.screen_layout, parentView, false);
        setupTopBottomColors(layout);
        setupScreenName(layout);
        setupPhoto(layout);
        boolean hasNextButton = getPersistentData().getBoolean(ARGS_HAS_NEXT_BUTTON);
        if (hasNextButton) {
            setupNextButton(layout);
        }
        return layout;
    }

    private void setupTopBottomColors(View layout) {
        int topColor = getPersistentData().getInt(ARGS_TOP_COLOR);
        int bottomColor = getPersistentData().getInt(ARGS_BOTTOM_COLOR);
        layout.findViewById(R.id.ll_top)
                .setBackgroundColor(topColor);
        layout.findViewById(R.id.ll_bottom)
                .setBackgroundColor(bottomColor);
    }

    private void setupScreenName(View layout) {
        int nameResId = getPersistentData().getInt(ARGS_NAME);
        TextView tvName = (TextView) layout.findViewById(R.id.tv_name);
        tvName.setText(nameResId);
    }

    private void setupPhoto(View layout) {
        int photoResId = getPersistentData().getInt(ARGS_PHOTO);
        ImageView ivPhoto = (ImageView) layout.findViewById(R.id.iv_photo);
        ivPhoto.setImageResource(photoResId);
    }

    private void setupNextButton(View layout) {
        int topColor = getPersistentData().getInt(ARGS_TOP_COLOR);
        TextView tvNext = (TextView) layout.findViewById(R.id.tv_next);
        tvNext.setVisibility(View.VISIBLE);
        tvNext.setTextColor(topColor);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                ((OnShowNextScreenListener) getActivity()).onShowNextScreen();
            }
        });
    }

    @Override
    protected void createAddAnimation(int animationCode) {
        if (animationCode != MainActivity.ANIMATION_CODE_ADD
                && animationCode != HistoryScreensManager.ANIMATION_CODE_BACK_PRESSED)
            return;
        getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getView().getViewTreeObserver().removeOnPreDrawListener(this);
                Animator animator = AnimationController.prepareAddAnimation(SampleScreen.this);
                animator.start();
                return true;
            }
        });
    }

    @Override
    protected void createRemoveAnimation(int animationCode) {
        if (animationCode != MainActivity.ANIMATION_CODE_REMOVE
                && animationCode != HistoryScreensManager.ANIMATION_CODE_BACK_PRESSED) {
            confirmViewRemoval();
            return;
        }
        Animator animator = AnimationController.prepareRemoveAnimation(this);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmViewRemoval();
            }
        });
        animator.start();
    }

}
