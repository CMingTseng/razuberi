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
    protected View onAdd(ViewGroup parentView, int animationCode, boolean restoringState) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.a_screen, parentView, false);
        setupAnimations(animationCode, layout);
        setupNextButton(layout);
        return layout;
    }

    private void setupAnimations(int animationCode, View layout) {
        if (animationCode == MainActivity.ANIMATION_CODE_ADD_FORWARD) {
            runLaunchAnimation(layout);
        } else if (animationCode == MainActivity.ANIMATION_CODE_ADD_BACKWARD) {
            runAddBackwardAnimation(layout);
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
                AnimationUtils.prepareAddScreenAnimation(layout, true).start();
                return true;
            }
        });
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
    protected void onRemove(int animationCode) {
        if (animationCode == MainActivity.ANIMATION_CODE_REMOVE_FORWARD) {
            runRemoveAnimation();
        } else {
            super.onRemove(animationCode);
        }
    }

    private void runRemoveAnimation() {
        Animator animator = AnimationUtils.prepareRemoveScreenAnimation(getView(), true);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmRemoval();
            }
        });
        animator.start();
    }

}
