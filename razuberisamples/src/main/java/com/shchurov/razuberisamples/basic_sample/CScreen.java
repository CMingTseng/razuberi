package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberisamples.R;

public class CScreen extends Screen {

    public static final String SCREEN_TAG = "c_screen";

    @Override
    protected View onAdd(ViewGroup parentView, int animationCode, boolean restoringState) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.c_screen, parentView, false);
        setupAnimations(animationCode, layout);
        setupSpinner(layout);
        return layout;
    }

    private void setupAnimations(final int animationCode, final View layout) {
        if (animationCode != AnimationUtils.ANIMATION_CODE_ADDED)
            return;
        layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                layout.getViewTreeObserver().removeOnPreDrawListener(this);
                AnimationUtils.prepareAddScreenAnimation(layout, animationCode).start();
                return true;
            }
        });
    }

    private void setupSpinner(View layout) {
        Spinner spSample = (Spinner) layout.findViewById(R.id.sp_sample);
        String[] items = {getActivity().getString(R.string.option_1),
                getActivity().getString(R.string.option_2), getActivity().getString(R.string.option_3)};
        spSample.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, items));
    }

    @Override
    protected void onRemove(int animationCode) {
        if (animationCode == AnimationUtils.ANIMATION_CODE_BACK_PRESSED) {
            runRemoveAnimation();
        } else {
            super.onRemove(animationCode);
        }
    }

    private void runRemoveAnimation() {
        Animator animator = AnimationUtils.prepareRemoveScreenAnimation(getView(), AnimationUtils.ANIMATION_CODE_BACK_PRESSED);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmRemoval();
            }
        });
        animator.start();
    }

}
