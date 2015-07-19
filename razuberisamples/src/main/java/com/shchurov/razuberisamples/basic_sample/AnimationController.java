package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shchurov.razuberisamples.R;

public class AnimationController {

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    public static Animator prepareAddAnimation(SampleScreen screen) {
        View screenView = screen.getView();
        Animator animator1 = prepareAddAnimationPart1(screenView);
        Animator animator2 = prepareAddAnimationPart2(screenView);
        AnimatorSet set = new AnimatorSet();
        set.setStartDelay(300);
        set.playSequentially(animator1, animator2);
        return set;
    }

    private static Animator prepareAddAnimationPart1(View screenView) {
        final LinearLayout llTop = (LinearLayout) screenView.findViewById(R.id.ll_top);
        final LinearLayout llBottom = (LinearLayout) screenView.findViewById(R.id.ll_bottom);
        final float topStartTranslation = -llTop.getHeight();
        final float bottomStartTranslation = llBottom.getHeight();
        llTop.setTranslationY(topStartTranslation);
        llBottom.setTranslationY(bottomStartTranslation);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(300);
        animator.setInterpolator(LINEAR_INTERPOLATOR);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                llTop.setTranslationY(topStartTranslation * (1 - value));
                llBottom.setTranslationY(bottomStartTranslation * (1 - value));
            }
        });
        return animator;
    }

    private static Animator prepareAddAnimationPart2(View screenView) {
        final ImageView ivPhoto = (ImageView) screenView.findViewById(R.id.iv_photo);
        final TextView tvInfo = (TextView) screenView.findViewById(R.id.tv_info);
        ivPhoto.setAlpha(0f);
        tvInfo.setAlpha(0f);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(400);
        animator.setStartDelay(100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ivPhoto.setAlpha(value);
                tvInfo.setAlpha(value);
            }
        });
        return animator;
    }

    public static Animator prepareRemoveAnimation(SampleScreen screen) {
        return ObjectAnimator.ofFloat(screen.getView(), "alpha", 1, 0)
                .setDuration(300);
    }

}
