package com.lll.demo.carouselayoutmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lll.layoutmanager.carousel.CarouselLayoutManager;
import com.lll.layoutmanager.carousel.CarouselZoomPostLayoutListener;
import com.lll.layoutmanager.carousel.CenterScrollListener;
import com.lll.layoutmanager.demo.R;

import java.util.Random;

/**
 * Version 1.0
 * Created by lll on 2018/11/12.
 * Description
 * <pre>
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class CarouseLayoutManagerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CarouselLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        mRecyclerView = findViewById(R.id.rl);


        initRecyclerView();

    }

    private void initRecyclerView() {
        mLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        mLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        mLayoutManager.setMaxVisibleItems(4);

        TestAdapter adapter = new TestAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        // we expect only fixed sized item for now
        mRecyclerView.setHasFixedSize(true);
        // sample adapter with random data
        mRecyclerView.setAdapter(adapter);
        // enable center post scrolling
        mRecyclerView.addOnScrollListener(new CenterScrollListener());
    }

    private static final class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

        @SuppressWarnings("UnsecureRandomNumberGeneration")
        private final Random mRandom = new Random();
        private final int[] mColors;
        private final int[] mPosition;
        private int mItemsCount = 100;

        TestAdapter() {
            mColors = new int[mItemsCount];
            mPosition = new int[mItemsCount];
            for (int i = 0; mItemsCount > i; ++i) {
                //noinspection MagicNumber
                mColors[i] = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
                mPosition[i] = i;
            }
        }

        @Override
        public TestViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            Log.e("!!!!!!!!!", "onCreateViewHolder");

            return new TestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carsousel, parent, false));
        }

        @Override
        public void onBindViewHolder(final TestViewHolder holder, final int position) {
            Log.e("!!!!!!!!!", "onBindViewHolder: " + position);
            holder.cItem1.setText(String.valueOf(mPosition[position]));
            holder.cItem2.setText(String.valueOf(mPosition[position]));
            holder.itemView.setBackgroundColor(mColors[position]);
        }

        @Override
        public int getItemCount() {
            return mItemsCount;
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {

        public TextView cItem1;
        public TextView cItem2;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            cItem1 = itemView.findViewById(R.id.c_item_1);
            cItem2 = itemView.findViewById(R.id.c_item_2);
        }
    }
}
