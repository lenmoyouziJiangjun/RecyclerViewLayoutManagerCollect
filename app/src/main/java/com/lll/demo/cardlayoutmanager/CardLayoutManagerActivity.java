package com.lll.demo.cardlayoutmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lll.layoutmanager.demo.R;
import com.lll.layoutmanager.swipelayoutmanager.CardLayoutManager;
import com.lll.layoutmanager.swipelayoutmanager.CardSetting;
import com.lll.layoutmanager.swipelayoutmanager.utils.CardTouchHelperCallback;
import com.lll.layoutmanager.swipelayoutmanager.utils.OnSwipeCardListener;
import com.lll.layoutmanager.swipelayoutmanager.utils.ReItemTouchHelper;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2018/12/3.
 * Description
 * <pre>
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class CardLayoutManagerActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    Button mChangeBtn, mLeftBtn, mRightBtn;
    ReItemTouchHelper mReItemTouchHelper;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.turn_left:
                mReItemTouchHelper.swipeManually(ReItemTouchHelper.LEFT);
                break;
            case R.id.turn_right:
                mReItemTouchHelper.swipeManually(ReItemTouchHelper.RIGHT);
                break;
            case R.id.change_type:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        List<CardBean> list = CardMaker.initCards();
        CardSetting setting = new CardSetting();
        setting.setSwipeListener(new OnSwipeCardListener<CardBean>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float dx, float dy, int direction) {
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Log.e("aaa", "swiping direction=down");
                        break;
                    case ReItemTouchHelper.UP:
                        Log.e("aaa", "swiping direction=up");
                        break;
                    case ReItemTouchHelper.LEFT:
                        Log.e("aaa", "swiping direction=left");
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Log.e("aaa", "swiping direction=right");
                        break;
                }
            }

            @Override
            public void onSwipedOut(RecyclerView.ViewHolder viewHolder, CardBean o, int direction) {
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Toast.makeText(CardLayoutManagerActivity.this, "swipe down out", Toast.LENGTH_SHORT).show();
                        break;
                    case ReItemTouchHelper.UP:
                        Toast.makeText(CardLayoutManagerActivity.this, "swipe up out ", Toast.LENGTH_SHORT).show();
                        break;
                    case ReItemTouchHelper.LEFT:
                        Toast.makeText(CardLayoutManagerActivity.this, "swipe left out", Toast.LENGTH_SHORT).show();
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Toast.makeText(CardLayoutManagerActivity.this, "swipe right out", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(CardLayoutManagerActivity.this, "cards are consumed", Toast.LENGTH_SHORT).show();
            }
        });
        CardTouchHelperCallback helperCallback = new CardTouchHelperCallback(mRecyclerView, list, setting);
        mReItemTouchHelper = new ReItemTouchHelper(helperCallback);
        CardLayoutManager layoutManager = new CardLayoutManager(mReItemTouchHelper, setting);
        mRecyclerView.setLayoutManager(layoutManager);
        CardAdapter cardAdapter = new CardAdapter(list);
        mRecyclerView.setAdapter(cardAdapter);

        mLeftBtn = findViewById(R.id.turn_left);
        mRightBtn = findViewById(R.id.turn_right);
        mChangeBtn = findViewById(R.id.change_type);
        mLeftBtn.setOnClickListener(this);
        mChangeBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
    }
}
