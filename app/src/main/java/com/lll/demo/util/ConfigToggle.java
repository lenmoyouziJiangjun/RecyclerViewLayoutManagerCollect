package com.lll.demo.util;

import android.content.Context;

abstract public class ConfigToggle {
    private String mLabel;

    protected ConfigToggle(Context context, int labelId) {
        mLabel = context.getResources().getString(labelId);
    }

    public String getText() {
        return mLabel;
    }

    abstract public boolean isChecked();

    abstract public void onChange(boolean newValue);
}