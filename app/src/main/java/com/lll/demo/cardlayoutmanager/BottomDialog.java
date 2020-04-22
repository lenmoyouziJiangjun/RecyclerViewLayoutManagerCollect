package com.lll.demo.cardlayoutmanager;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.lll.layoutmanager.demo.R;

/**
 * Version 1.0
 * Created by lll on 2020/4/22.
 * Description
 * copyright generalray4239@gmail.com
 */
public class BottomDialog extends Dialog {

    private final static int DP_HEIGHT = 140;

    public BottomDialog(@NonNull Context context) {
        super(context);
    }

    public BottomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setWindowAnimations(R.style.share_dialog);
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
//        windowparams.height = SystemUtils.dp2px(getContext(), DP_HEIGHT);
        windowparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(windowparams);
    }
}
