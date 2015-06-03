package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
        scaleAnimator.setDuration(200);
        scaleAnimator.setInterpolator(new AccelerateInterpolator());
        int displayWidth = screenView.getResources().getDisplayMetrics().widthPixels;
        float fromTranslationX = (displayWidth + screenView.getWidth()) / 2;
        if (animationCode == ANIMATION_CODE_BACK_PRESSED) {
            fromTranslationX = -fromTranslationX;
        }
        screenView.setTranslationX(fromTranslationX);
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(screenView, "translationX", screenView.getTranslationX(), 0);
        translationAnimator.setDuration(400);
        translationAnimator.setInterpolator(new DecelerateInterpolator());
        translationAnimator.setStartDelay(300);
        translationAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                screenView.setVisibility(View.VISIBLE);
            }
        });
        AnimatorSet addAnimator = new AnimatorSet();
        addAnimator.play(translationAnimator).before(scaleAnimator);
        if (screenView.getTag() != null) {
            ((Animator) screenView.getTag()).cancel();
        }
        screenView.setTag(addAnimator);
        return addAnimator;
    }

    public static Animator prepareRemoveScreenAnimation(View screenView, int animationCode) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(screenView, "scaleX", 1f, MIN_SCALE);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(screenView, "scaleY", 1f, MIN_SCALE);
        AnimatorSet scaleAnimator = new AnimatorSet();
        scaleAnimator.playTogether(scaleXAnimator, scaleYAnimator);
        scaleAnimator.setDuration(200);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        int displayWidth = screenView.getResources().getDisplayMetrics().widthPixels;
        float toTranslationX = (displayWidth + screenView.getWidth()) / 2;
        if (animationCode == ANIMATION_CODE_REPLACED) {
            toTranslationX = -toTranslationX;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(screenView, "translationX", screenView.getTranslationX(), toTranslationX);
        translationAnimator.setDuration(400);
        translationAnimator.setInterpolator(new AccelerateInterpolator());
        AnimatorSet removeAnimator = new AnimatorSet();
        removeAnimator.play(scaleAnimator).before(translationAnimator);
        if (screenView.getTag() != null) {
            ((Animator) screenView.getTag()).cancel();
        }
        screenView.setTag(removeAnimator);
        return removeAnimator;
    }

}
