package com.lll.demo.itemhelper;

import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lll.demo.util.ConfigToggle;
import com.lll.layoutmanager.demo.R;

public class SwipeToDismissActivity extends ItemTouchHelperActivity {
    boolean mSwipeStartEnabled = true;
    boolean mSwipeEndEnabled = true;
    boolean mPointerSwipeEnabled = true;
    boolean mCustomSwipeEnabled = false;

    @Override
    public void onBind(ItemTouchViewHolder viewHolder) {
        super.onBind(viewHolder);
        viewHolder.actionButton.setVisibility(mPointerSwipeEnabled ? View.GONE : View.VISIBLE);
    }

    @Override
    ConfigToggle[] createConfigToggles() {
        ConfigToggle[] configToggles = {
                new ConfigToggle(this, R.string.swipe_start) {
                    @Override
                    public boolean isChecked() {
                        return mSwipeStartEnabled;
                    }

                    @Override
                    public void onChange(boolean newValue) {
                        mSwipeStartEnabled = newValue;
                    }
                },
                new ConfigToggle(this, R.string.swipe_end) {
                    @Override
                    public boolean isChecked() {
                        return mSwipeEndEnabled;
                    }

                    @Override
                    public void onChange(boolean newValue) {
                        mSwipeEndEnabled = newValue;
                    }
                },
                new ConfigToggle(this, R.string.pointer_swipe_enabled) {
                    @Override
                    public boolean isChecked() {
                        return mPointerSwipeEnabled;
                    }

                    @Override
                    public void onChange(boolean newValue) {
                        mPointerSwipeEnabled = newValue;
                        mAdapter.notifyDataSetChanged();
                    }
                }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ConfigToggle[] copy = new ConfigToggle[configToggles.length + 1];
            System.arraycopy(configToggles, 0, copy, 0, configToggles.length);
            copy[copy.length - 1] = new ConfigToggle(this, R.string.custom_swipe_enabled) {
                @Override
                public boolean isChecked() {
                    return mCustomSwipeEnabled;
                }

                @Override
                public void onChange(boolean newValue) {
                    mCustomSwipeEnabled = newValue;
                }
            };
            return copy;
        } else {
            return configToggles;
        }
    }


    @Override
    public ItemTouchViewHolder createItemViewHolder(ViewGroup parent) {
        final ItemTouchViewHolder vh = super.createItemViewHolder(parent);
        vh.actionButton.setText(R.string.swipe);
        vh.actionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mItemTouchHelper.startSwipe(vh);
                }
                return false;
            }
        });
        return vh;
    }

    @Override
    ItemTouchHelper.Callback createCallback() {
        return new SimpleTouchItemCallback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,
                        (mSwipeStartEnabled ? ItemTouchHelper.START : 0) |
                                (mSwipeEndEnabled ? ItemTouchHelper.END : 0));
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return mPointerSwipeEnabled;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (mCustomSwipeEnabled) {

                    ItemTouchViewHolder touchVH = (ItemTouchViewHolder) viewHolder;
                    final float dir = Math.signum(dX);
                    if (dir == 0) {
                        touchVH.overlay.setTranslationX(-touchVH.overlay.getWidth());
                    } else {
                        final float overlayOffset = dX - dir * viewHolder.itemView.getWidth();
                        touchVH.overlay.setTranslationX(overlayOffset);
                    }
                    float alpha = (float) (.2 + .8 * Math.abs(dX) / viewHolder.itemView.getWidth());
                    touchVH.overlay.setAlpha(alpha);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                            isCurrentlyActive);
                }
            }


            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                ItemTouchViewHolder touchVH = (ItemTouchViewHolder) viewHolder;
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    touchVH.cardView.setCardBackgroundColor(ContextCompat.getColor(SwipeToDismissActivity.this, R.color.card_aquatic));
                    if (mCustomSwipeEnabled) {
                        // hide it
                        touchVH.overlay.setTranslationX(viewHolder.itemView.getWidth());
                        touchVH.overlay.setVisibility(View.VISIBLE);
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                ItemTouchViewHolder touchVH = (ItemTouchViewHolder) viewHolder;
                touchVH.cardView.setCardBackgroundColor(ContextCompat.getColor(SwipeToDismissActivity.this, android.R.color.white));
                touchVH.overlay.setVisibility(View.GONE);
            }
        };
    }


}
