package com.snepos.pitchit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Omer on 07/09/2015.
 */
public class LayoutAnimator {

    public static class LayoutHeightUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final View _view;

        public LayoutHeightUpdateListener(View view) {
            _view = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final ViewGroup.LayoutParams lp = _view.getLayoutParams();
            lp.height = (int) animation.getAnimatedValue();
            _view.setLayoutParams(lp);
        }
    }

    public static class CardElevationUpdateListener implements ValueAnimator.AnimatorUpdateListener
    {
        private final CardView cardView;

        public CardElevationUpdateListener(CardView cardView) {
            this.cardView = cardView;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            cardView.setCardElevation((int)animation.getAnimatedValue());
        }
    }

    public static ValueAnimator ofHeight(View view, int start, int end) {
        final ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new LayoutHeightUpdateListener(view));
        return animator;
    }

    public static ValueAnimator ofCardViewElevation(CardView cardView, int start, int end)
    {
        final ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new CardElevationUpdateListener(cardView));
        return animator;
    }
}
