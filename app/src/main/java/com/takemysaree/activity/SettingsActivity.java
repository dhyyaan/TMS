package com.takemysaree.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.takemysaree.R;

/**
 * Created by think360user on 3/11/17.
 */

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }
}
