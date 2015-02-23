package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.shchurov.razuberi.history.HistoryScreensManager;

public class AnimationUtils {

    public static final int ANIMATION_CODE_ADDED = 1;
    public static final int ANIMATION_CODE_REPLACED = 2;
    public static final int ANIMATION_CODE_BACK_PRESSED = HistoryScreensManager.ANIMATION_CODE_BACK_PRESSED;
    private static final float MIN_SCALE = 0.4f;

    public static Animator prepareAddScreenAnimation(final View screenView, int animationCode) {
        screenView.setVisibility(View.INVISIBLE);
        screenView.setScaleX(MIN_SCALE);
        screenView.setScaleY(MIN_SCALE);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(screenView, "scaleX", MIN_SCALE, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(screenView, "scaleY", MIN_SCALE, 1f);
        AnimatorSet scaleAnimator = new AnimatorSet();
        scaleAnimator.playTogether(scaleXAnimator, scaleYAnimator);
        scaleAnimator.setDuration(300);
        scaleAnimator.setInterpolator(new AccelerateInterpolator());
        int displayWidth = screenView.getResources().getDisplayMetrics().widthPixels;
        float fromTranslationX = (displayWidth + screenView.getWidth()) / 2;
        if (animationCode == ANIMATION_CODE_BACK_PRESSED) {
            fromTranslationX = -fromTranslationX;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(screenView, "translationX", fromTranslationX, 0);
        translationAnimator.setDuration(500);
        translationAnimator.setInterpolator(new DecelerateInterpolator());
        translationAnimator.setStartDelay(300);
        translationAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                screenView.setVisibility(View.VISIBLE);
            }
        });
        AnimatorSet removeAnimator = new AnimatorSet();
        removeAnimator.play(translationAnimator).before(scaleAnimator);
        return removeAnimator;
    }

    public static Animator prepareRemoveScreenAnimation(View screenView, int animationCode) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(screenView, "scaleX", 1f, MIN_SCALE);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(screenView, "scaleY", 1f, MIN_SCALE);
        AnimatorSet scaleAnimator = new AnimatorSet();
        scaleAnimator.playTogether(scaleXAnimator, scaleYAnimator);
        scaleAnimator.setDuration(300);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        int displayWidth = screenView.getResources().getDisplayMetrics().widthPixels;
        float toTranslationX = (displayWidth + screenView.getWidth()) / 2;
        if (animationCode == ANIMATION_CODE_REPLACED) {
            toTranslationX = -toTranslationX;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(screenView, "translationX", 0, toTranslationX);
        translationAnimator.setDuration(500);
        translationAnimator.setInterpolator(new AccelerateInterpolator());
        AnimatorSet removeAnimator = new AnimatorSet();
        removeAnimator.play(scaleAnimator).before(translationAnimator);
        return removeAnimator;
    }

}
