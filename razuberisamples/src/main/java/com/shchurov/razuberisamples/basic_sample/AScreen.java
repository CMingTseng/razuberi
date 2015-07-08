package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.*;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberisamples.R;

public class AScreen extends Screen {

    public static final String SCREEN_TAG = "a_screen";

    @Override
    protected View onAdd(ViewGroup parentView, boolean restoring) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.a_screen, parentView, false);
        setupNextButton(layout);
        return layout;
    }

    private void setupNextButton(View layout) {
        View tvNext = layout.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnShowNextScreenListener) getActivity()).onShowNextScreen();
            }
        });
    }

    @Override
    protected void createAddAnimation(int animationCode) {
        if (animationCode == AnimationUtils.ANIMATION_CODE_ADDED) {
            runLaunchAnimation(getView());
        } else if (animationCode == AnimationUtils.ANIMATION_CODE_BACK_PRESSED) {
            runAddBackwardAnimation(getView());
        }
    }

    private void runLaunchAnimation(View layout) {
        layout.setAlpha(0);
        layout.animate()
                .alpha(1)
                .setDuration(400)
                .setStartDelay(200)
                .setInterpolator(new DecelerateInterpolator());
    }

    private void runAddBackwardAnimation(final View layout) {
        layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                layout.getViewTreeObserver().removeOnPreDrawListener(this);
                AnimationUtils.prepareAddScreenAnimation(layout, AnimationUtils.ANIMATION_CODE_BACK_PRESSED).start();
                return true;
            }
        });
    }

    @Override
    protected void createRemoveAnimation(int animationCode) {
        if (animationCode != AnimationUtils.ANIMATION_CODE_REPLACED) {
            confirmViewRemoval();
            return;
        }
        Animator animator = AnimationUtils.prepareRemoveScreenAnimation(getView(), AnimationUtils.ANIMATION_CODE_REPLACED);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmViewRemoval();
            }
        });
        animator.start();
    }

}
