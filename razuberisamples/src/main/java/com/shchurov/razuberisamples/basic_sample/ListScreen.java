package com.shchurov.razuberisamples.basic_sample;

import android.animation.Animator;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.shchurov.razuberi.core.Screen;
import com.shchurov.razuberisamples.R;

import java.util.List;

public class ListScreen extends Screen {

    public static final String SCREEN_TAG = "list";
    public static final int ANIMATION_CODE_ADD = 1;
    public static final String KEY_ITEMS = "items";
    private static final int ITEMS_PADDING_DP = 8;

    private RecyclerView rvItems;
    private ImageButton ibAdd;
    private Toolbar toolbar;

    @Override
    protected View onAdd(int animationCode, boolean restoringState) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.screen_list, null);
        rvItems = (RecyclerView) layout.findViewById(R.id.rv_items);
        ibAdd = (ImageButton) getActivity().findViewById(R.id.ib_action);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_main);
        setupList();
        setupActionButton();
        if (!restoringState && animationCode == ANIMATION_CODE_ADD) {
            setupOnAddAnimation(layout);
        }
        return layout;
    }

    private void setupList() {
        final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ITEMS_PADDING_DP,
                getActivity().getResources().getDisplayMetrics());
        rvItems.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = outRect.right = outRect.bottom = padding;
                if (parent.getChildPosition(view) == 0) {
                    outRect.top = padding;
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(layoutManager);
        List<ListItem> items = getPersistentData().getParcelableArrayList(KEY_ITEMS);
        rvItems.setAdapter(new ListAdapter(items));
    }

    private void setupActionButton() {
        ibAdd.setImageResource(R.drawable.ic_add);
        ibAdd.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ibAdd.getViewTreeObserver().removeOnPreDrawListener(this);
                ibAdd.setTranslationY(toolbar.getHeight() - ibAdd.getHeight() / 2);
                return true;
            }
        });
    }

    private void setupOnAddAnimation(final View layout) {
        layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                layout.getViewTreeObserver().removeOnPreDrawListener(this);
                runOnAddAnimation();
                return true;
            }
        });
    }

    private void runOnAddAnimation() {
        toolbar.setTranslationY(-toolbar.getHeight());
        toolbar.animate()
                .translationY(0)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .setStartDelay(300);
        rvItems.setTranslationY(rvItems.getHeight());
        rvItems.animate()
                .translationY(0)
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator())
                .setStartDelay(300);
        ibAdd.setScaleX(0);
        ibAdd.setScaleY(0);
        ibAdd.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .setStartDelay(500);
    }

}
