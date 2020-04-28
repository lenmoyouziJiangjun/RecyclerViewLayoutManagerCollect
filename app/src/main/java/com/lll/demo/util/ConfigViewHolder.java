package com.lll.demo.util;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

public class ConfigViewHolder extends RecyclerView.ViewHolder
        implements CompoundButton.OnCheckedChangeListener {

    private CheckBox mCheckBox;

    private ConfigToggle mConfigToggle;

    public ConfigViewHolder(View itemView) {
        super(itemView);
        mCheckBox = (CheckBox) itemView;
        mCheckBox.setOnCheckedChangeListener(this);
    }

    public void bind(ConfigToggle toggle) {
        mConfigToggle = toggle;
        mCheckBox.setText(toggle.getText());
        mCheckBox.setChecked(toggle.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mConfigToggle != null) {
            mConfigToggle.onChange(isChecked);
        }
    }
}
