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
    protected View onAdd(ViewGroup parentView, boolean restoring) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.c_screen, parentView, false);
        setupSpinner(layout);
        return layout;
    }

    @Override
    protected void createAddAnimation(final int animationCode) {
        if (animationCode != AnimationUtils.ANIMATION_CODE_ADDED)
            return;
        getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getView().getViewTreeObserver().removeOnPreDrawListener(this);
                Animator animator = AnimationUtils.prepareAddScreenAnimation(getView(), animationCode);
                animator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        confirmViewAddition();
                    }
                });
                animator.start();
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
    protected void createRemoveAnimation(int animationCode) {
        if (animationCode != AnimationUtils.ANIMATION_CODE_BACK_PRESSED) {
            confirmViewRemoval();
            return;
        }
        Animator animator = AnimationUtils.prepareRemoveScreenAnimation(getView(), AnimationUtils.ANIMATION_CODE_BACK_PRESSED);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmViewRemoval();
            }
        });
        animator.start();
    }

}
