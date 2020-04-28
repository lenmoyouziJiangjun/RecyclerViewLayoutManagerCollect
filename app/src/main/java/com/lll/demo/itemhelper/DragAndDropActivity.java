package com.lll.demo.itemhelper;

import android.graphics.Canvas;
import android.graphics.Paint;
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

public class DragAndDropActivity extends ItemTouchHelperActivity {

    boolean mDragUpEnabled = true;
    boolean mDragDownEnabled = true;
    boolean mLongPressDragEnabled = true;

    @Override
    ConfigToggle[] createConfigToggles() {
        return new ConfigToggle[]{
                new ConfigToggle(this, R.string.drag_up) {
                    @Override
                    public boolean isChecked() {
                        return mDragUpEnabled;
                    }

                    @Override
                    public void onChange(boolean newValue) {
                        mDragUpEnabled = newValue;
                    }
                },
                new ConfigToggle(this, R.string.drag_down) {
                    @Override
                    public boolean isChecked() {
                        return mDragDownEnabled;
                    }

                    @Override
                    public void onChange(boolean newValue) {
                        mDragDownEnabled = newValue;
                    }
                },
                new ConfigToggle(this, R.string.long_press_drag) {
                    @Override
                    public boolean isChecked() {
                        return mLongPressDragEnabled;
                    }

                    @Override
                    public void onChange(boolean newValue) {
                        mLongPressDragEnabled = newValue;
                        mAdapter.notifyDataSetChanged();
                    }
                }
        };
    }

    @Override
    public void onBind(ItemTouchViewHolder viewHolder) {
        super.onBind(viewHolder);
        viewHolder.actionButton.setVisibility(mLongPressDragEnabled ? View.GONE : View.VISIBLE);
    }

    @Override
    public ItemTouchViewHolder createItemViewHolder(ViewGroup parent) {
        final ItemTouchViewHolder vh = super.createItemViewHolder(parent);
        vh.actionButton.setText(R.string.drag);
        vh.actionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mItemTouchHelper.startDrag(vh); //开始拖动
                }
                return false;
            }
        });
        return vh;
    }

    @Override
    ItemTouchHelper.Callback createCallback() {
        return new SimpleTouchItemCallback() {

            //接收哪个方向上面的move 和 swipe事件
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //接收down 和up方向的drag事件
                return makeMovementFlags((mDragUpEnabled ? ItemTouchHelper.UP : 0) | (mDragDownEnabled ? ItemTouchHelper.DOWN : 0), 0);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                ItemTouchViewHolder touchVH = (ItemTouchViewHolder) viewHolder;
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    touchVH.cardView.setCardBackgroundColor(ContextCompat.getColor(DragAndDropActivity.this, R.color.colorAccent));
                }

                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                ItemTouchViewHolder touchVH = (ItemTouchViewHolder) viewHolder;
                touchVH.cardView.setCardBackgroundColor(
                        ContextCompat.getColor(DragAndDropActivity.this, android.R.color.white));
                touchVH.overlay.setVisibility(View.GONE);
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                c.drawText("测试一下，哈哈哈哈", 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return mLongPressDragEnabled;
            }
        };
    }
}
