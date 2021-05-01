/*
Copyright 2020 Mrudul Tora <mrudultora@gmail.com>

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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mrudultora.colorpicker.ColorItemShape;
import com.mrudultora.colorpicker.ColorPicker;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 */
public class MainActivity extends AppCompatActivity {
    int defaultColor;
    Button btnDialogBox, btnDirectDialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDialogBox = findViewById(R.id.btnDialogBox);
        btnDirectDialogBox = findViewById(R.id.btnDirectDialogBox);
        defaultColor = Color.parseColor("#ea1e63");
        btnDialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColorDialog();
            }
        });
        btnDirectDialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDirectColorDialog();
            }
        });
    }

    public void selectColorDialog() {
        final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
        colorPicker.setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setTickDimenInDp(24)
                .setTickColor(Color.WHITE)
                .setOnSelectColorListener(new ColorPicker.OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        btnDialogBox.setBackgroundColor(color);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        defaultColor = color;
                    }

                    @Override
                    public void cancel() {
                        colorPicker.dismissDialog();
                    }
                })
                .show();
//        colorPicker.setDialogTitle("Choose the color");
//        colorPicker.getDialogTitle().setTextColor(Color.RED);
//        colorPicker.setColorItemDrawable(R.drawable.ic_baseline_star_24)
    }

    public void selectDirectColorDialog() {
        final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
        colorPicker.setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.SQUARE)
                .setTickDimenInDp(24)
                .setTickColor(Color.WHITE)
                .setOnDirectSelectColorListener(new ColorPicker.OnDirectSelectColorListener() {
                    @Override
                    public void onDirectColorSelected(int color, int position) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        btnDirectDialogBox.setBackgroundColor(color);
                        defaultColor = color;
                    }
                })
                .show();
//        colorPicker.setDialogTitle("Choose the color");
//        colorPicker.getDialogTitle().setTextColor(Color.RED);
//        colorPicker.setColorItemDrawable(R.drawable.ic_baseline_star_24)
    }
}