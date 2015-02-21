package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberisamples.R;

public class BScreen extends Screen {

    public static final String SCREEN_TAG = "b_screen";

    @Override
    protected View onAdd(ViewGroup parentView, int animationCode, boolean restoringState) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.b_screen, parentView, false);
        runAddAnimation(animationCode, layout);
        setupNextButton(layout);
        return layout;
    }

    private void runAddAnimation(final int animationCode, final View layout) {
        if (animationCode != MainActivity.ANIMATION_CODE_ADD_FORWARD && animationCode != MainActivity.ANIMATION_CODE_ADD_BACKWARD)
            return;
        layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                layout.getViewTreeObserver().removeOnPreDrawListener(this);
                AnimationUtils.prepareAddScreenAnimation(layout, animationCode == MainActivity.ANIMATION_CODE_ADD_BACKWARD).start();
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
        if (animationCode == MainActivity.ANIMATION_CODE_REMOVE_FORWARD || animationCode == MainActivity.ANIMATION_CODE_REMOVE_BACKWARD) {
            runRemoveAnimation(animationCode);
        } else {
            super.onRemove(animationCode);
        }
    }

    private void runRemoveAnimation(int animationCode) {
        Animator animator = AnimationUtils.prepareRemoveScreenAnimation(getView(), animationCode == MainActivity.ANIMATION_CODE_REMOVE_FORWARD);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmRemoval();
            }
        });
        animator.start();
    }

}


