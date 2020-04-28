package com.lll.demo.itemhelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lll.demo.itemdecoration.ColorDividerItemDecoration;
import com.lll.demo.util.Cheeses;
import com.lll.demo.util.ConfigToggle;
import com.lll.layoutmanager.demo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2020/4/23.
 * Description
 * <pre>
 *  滑动显示操作按钮
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class SwipeMenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ItemTouchHelper mItemTouchHelper;

    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0,
                    ItemTouchHelper.START |
                            ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            MenuViewHolder touchVH = (MenuViewHolder) viewHolder;
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                touchVH.cardView.setCardBackgroundColor(ContextCompat.getColor(SwipeMenuActivity.this, R.color.card_aquatic));
                // hide it
                touchVH.rightMenu.setTranslationX(viewHolder.itemView.getWidth());
                touchVH.rightMenu.setVisibility(View.VISIBLE);
                touchVH.leftMenu.setTranslationX(-touchVH.leftMenu.getWidth());
                touchVH.leftMenu.setVisibility(View.VISIBLE);
            }


            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (Math.abs(dX) > 30 && (Math.abs(dX) > Math.abs(dY))) {
                ((MenuViewHolder) viewHolder).showOpeButton(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = new RecyclerView(this);
        setContentView(mRecyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MenuAdapter());
        mRecyclerView.addItemDecoration(new ColorDividerItemDecoration(this, RecyclerView.VERTICAL, Color.RED, R.dimen.divider_size));
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

        private List<String> mItems = new ArrayList<String>();

        public MenuAdapter() {
            mItems.addAll(Arrays.asList(Cheeses.sCheeseStrings));
        }

        @Override
        public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_menu, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuViewHolder holder, int position) {
            holder.bind(mItems.get(position));
        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView titleView;
        TextView leftMenu;
        LinearLayout rightMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            titleView = itemView.findViewById(R.id.content);
            leftMenu = findViewById(R.id.left);
            rightMenu = findViewById(R.id.right);
        }

        void bind(String text) {
            titleView.setText(text);
        }

        private void showOpeButton(float dx) {
            final float width = itemView.getMeasuredWidth();
            final float dir = Math.signum(dx);
            final float overlayOffset = dx - dir * width;
            itemView.setTranslationX(overlayOffset);
        }
    }
}
