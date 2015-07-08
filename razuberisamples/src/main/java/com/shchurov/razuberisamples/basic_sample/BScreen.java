package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberisamples.R;

public class BScreen extends Screen {

    public static final String SCREEN_TAG = "b_screen";
    private static final String SAVED_NUMBER = "number";

    private int number;

    @Override
    protected View onAdd(ViewGroup parentView, boolean restoring) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.b_screen, parentView, false);
        setupNextButton(layout);
        setupRandomNumber(layout, restoring);
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

    private void setupRandomNumber(View layout, boolean restoring) {
        if (!restoring) {
            number = (int) (Math.random() * 99);
        } else {
            number = getPersistentData().getInt(SAVED_NUMBER);
        }
        TextView tvRandom = (TextView) layout.findViewById(R.id.tv_random);
        tvRandom.setText(getActivity().getString(R.string.random_number, number));
    }

    @Override
    protected void createAddAnimation(final int animationCode) {
        if (animationCode != AnimationUtils.ANIMATION_CODE_ADDED && animationCode != AnimationUtils.ANIMATION_CODE_BACK_PRESSED)
            return;
        getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getView().getViewTreeObserver().removeOnPreDrawListener(this);
                AnimationUtils.prepareAddScreenAnimation(getView(), animationCode).start();
                return true;
            }
        });
    }

    @Override
    protected void onSaveState() {
        getPersistentData().putInt(SAVED_NUMBER, number);
    }

    @Override
    protected void createRemoveAnimation(int animationCode) {
        if (animationCode != AnimationUtils.ANIMATION_CODE_REPLACED && animationCode != AnimationUtils.ANIMATION_CODE_BACK_PRESSED) {
            confirmViewRemoval();
            return;
        }
        Animator animator = AnimationUtils.prepareRemoveScreenAnimation(getView(), animationCode);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmViewRemoval();
            }
        });
        animator.start();
    }

}


