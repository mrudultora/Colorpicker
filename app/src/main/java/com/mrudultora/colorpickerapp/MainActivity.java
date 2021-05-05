/*
Copyright 2021 Mrudul Tora <mrudultora@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mrudultora.colorpickerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.mrudultora.colorpicker.ColorPickerBottomSheetDialog;
import com.mrudultora.colorpicker.ColorPickerPopUp;
import com.mrudultora.colorpicker.ColorPickerView;
import com.mrudultora.colorpicker.listeners.OnDirectSelectColorListener;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;
import com.mrudultora.colorpicker.util.ColorItemShape;
import com.mrudultora.colorpicker.ColorPickerDialog;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 */
public class MainActivity extends AppCompatActivity {
    public ViewPager viewPager;
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      For setting the dark mode
//      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        prepareViewPager(viewPager);
    }

    public void prepareViewPager(final ViewPager pager) {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), MainActivity.this);
        pager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(pager);
    }

}