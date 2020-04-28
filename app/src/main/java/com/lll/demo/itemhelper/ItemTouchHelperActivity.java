package com.lll.demo.itemhelper;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lll.demo.discrete.shop.Item;
import com.lll.demo.itemdecoration.DefaultDividerItemDecoration;
import com.lll.demo.util.Cheeses;
import com.lll.demo.util.ConfigToggle;
import com.lll.demo.util.ConfigViewHolder;
import com.lll.layoutmanager.demo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2020/4/22.
 * Description
 * <pre>
 *
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public abstract class ItemTouchHelperActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public ItemTouchHelper mItemTouchHelper;

    public ItemTouchHelper.Callback mCallback;

    private ConfigToggle[] mConfigToggles;

    public ItemTouchAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemtouch);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemTouchHelper = createItemTouchHelper();
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        initToggles();
    }

    public ItemTouchHelper createItemTouchHelper() {
        mCallback = createCallback();
        return new ItemTouchHelper(mCallback);
    }

    abstract ItemTouchHelper.Callback createCallback();

    abstract ConfigToggle[] createConfigToggles();


    public ItemTouchAdapter createAdapter() {
        return new ItemTouchAdapter();
    }

    private void initToggles() {
        mConfigToggles = createConfigToggles();
        RecyclerView configView = findViewById(R.id.config_recycler_view);
        configView.setAdapter(new RecyclerView.Adapter<ConfigViewHolder>() {
            @Override
            public ConfigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ConfigViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.config_view_toggle, parent, false));
            }

            @Override
            public void onBindViewHolder(ConfigViewHolder holder, int position) {
                ConfigToggle toggle = mConfigToggles[position];
                holder.bind(toggle);
            }

            @Override
            public int getItemCount() {
                return mConfigToggles.length;
            }
        });
        configView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        configView.setHasFixedSize(true);
        configView.addItemDecoration(new DefaultDividerItemDecoration(this, 1));
    }


    public void onBind(ItemTouchViewHolder viewHolder) {

    }

    public ItemTouchViewHolder createItemViewHolder(ViewGroup parent) {
        return new ItemTouchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.touch_item, parent, false));
    }

    public class ItemTouchViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;

        public final Button actionButton;

        public final CardView cardView;

        public final View overlay;

        public ItemTouchViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textView = itemView.findViewById(R.id.text_view);
            actionButton = itemView.findViewById(R.id.action_button);
            overlay = itemView.findViewById(R.id.overlay);
        }
    }

    public class ItemTouchAdapter extends RecyclerView.Adapter<ItemTouchViewHolder> {

        private List<String> mItems = new ArrayList<String>();

        public ItemTouchAdapter() {
            mItems.addAll(Arrays.asList(Cheeses.sCheeseStrings));
        }

        @Override
        public ItemTouchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return createItemViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ItemTouchViewHolder holder, int position) {
            holder.textView.setText(mItems.get(position));
            onBind(holder);
        }

        public void delete(int position) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }

        public void move(int from, int to) {
            String prev = mItems.remove(from);
            mItems.add(to > from ? to - 1 : to, prev);
            notifyItemMoved(from, to);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }


    public abstract class SimpleTouchItemCallback extends ItemTouchHelper.Callback {

        /**
         * @param recyclerView
         * @param viewHolder  当前操作的ViewHolder
         * @param target  需要移动到的ViewHolder
         * @return
         */
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.delete(viewHolder.getAdapterPosition());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }
    }


}
