package com.snepos.pitchit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationSet;

/**
 * Created by Omer on 07/09/2015.
 */
public class ExpandableIdeaUtil
{
    public static void openViewHolder(final ExpandableIdeaAdapter.ViewHolder holder, final View bottomView, final View topView, final boolean animate)
    {
        if(animate)
        {
            if(holder.animatorSet != null)
                holder.animatorSet.cancel();
            holder.animatorSet = new AnimatorSet();
            bottomView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
            Animator bottomAnimator = ExpandableIdeaAnimator.ofItemViewHeight(holder, true);
            Animator topAnimator = ExpandableIdeaAnimator.ofViewHeight(topView, true);
            Animator cardElevationAnimator = LayoutAnimator.ofCardViewElevation((CardView)holder.itemView, 0, 12);
            holder.animatorSet.play(bottomAnimator).with(topAnimator).with(cardElevationAnimator);
            holder.animatorSet.start();
        }
        else
        {
            bottomView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
        }
    }

    public static void closeViewHolder(final ExpandableIdeaAdapter.ViewHolder holder, final View bottomView, final View topView, final boolean animate)
    {
        if(animate)
        {
            bottomView.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
            if(holder.animatorSet != null)
                holder.animatorSet.cancel();
            holder.animatorSet = new AnimatorSet();
            Animator bottomAnimator = ExpandableIdeaAnimator.ofItemViewHeight(holder, false);
            Animator topAnimator = ExpandableIdeaAnimator.ofViewHeight(topView, false);
            Animator cardElevationAnimator = LayoutAnimator.ofCardViewElevation((CardView) holder.itemView, 12, 0);
            bottomView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
            holder.animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bottomView.setVisibility(View.GONE);
                    topView.setVisibility(View.GONE);
                }
            });
            holder.animatorSet.play(bottomAnimator).with(topAnimator).with(cardElevationAnimator);
            holder.animatorSet.start();
        }
        else
        {
            bottomView.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
        }
    }

    public interface Expandable {
        View getTopView();
        View getBottomView();
    }


    public static class KeepOneOpen<VH extends ExpandableIdeaAdapter.ViewHolder & Expandable> {
        private int _opened = -1;

        public void bind(VH holder, int pos) {
            if (pos == _opened)
                ExpandableIdeaUtil.openViewHolder(holder, holder.getBottomView(), holder.getTopView(), false);
            else
                ExpandableIdeaUtil.closeViewHolder(holder, holder.getBottomView(), holder.getTopView(), false);
        }

        @SuppressWarnings("unchecked")
        public void toggle(VH holder) {
            if (_opened == holder.getPosition()) {
                _opened = -1;
                ExpandableIdeaUtil.closeViewHolder(holder, holder.getBottomView(), holder.getTopView(), true);
            }
            else {
                int previous = _opened;
                _opened = holder.getPosition();
                ExpandableIdeaUtil.openViewHolder(holder, holder.getBottomView(), holder.getTopView(), true);

                final VH oldHolder = (VH) ((RecyclerView) holder.itemView.getParent()).findViewHolderForPosition(previous);
                if (oldHolder != null)
                    ExpandableIdeaUtil.closeViewHolder(oldHolder, oldHolder.getBottomView(), oldHolder.getTopView(), true);
            }
        }
    }
}
