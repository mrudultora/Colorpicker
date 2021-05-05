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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    int defaultColor;
    Button btnDialogBox, btnDirectDialogBox, btnBottomSheet, btnDirectBottomSheet, btnColorPickerPopup;
    ColorPickerDialog colorPickerDialog;
    ColorPickerBottomSheetDialog bottomSheetDialog;
    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDialogBox = findViewById(R.id.btnDialogBox);
        btnDirectDialogBox = findViewById(R.id.btnDirectDialogBox);
        btnBottomSheet = findViewById(R.id.btnBottomSheet);
        btnDirectBottomSheet = findViewById(R.id.btnDirectBottomSheet);
        btnColorPickerPopup = findViewById(R.id.btnColorPickerPopup);
        colorPickerView = findViewById(R.id.colorPickerView);
        btnDialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                selectColorDialog();
                ColorPickerPopUp colorPickerPopUp = new ColorPickerPopUp(MainActivity.this);
                colorPickerPopUp.show();
            }
        });
        btnDirectDialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDirectColorDialog();
            }
        });
        btnBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomSheet();
            }
        });
        btnDirectBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDirectBottomSheet();
            }
        });
        btnColorPickerPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerPopUp colorPickerPopUp = new ColorPickerPopUp(MainActivity.this);
                colorPickerPopUp.setDefaultColor(Color.parseColor("#D900FF00"));
                colorPickerPopUp.show();
            }
        });
    }

    public void selectColorDialog() {
        colorPickerDialog = new ColorPickerDialog(MainActivity.this);
        colorPickerDialog.setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setTickDimenInDp(24)
                .setTickColor(Color.BLACK, Color.parseColor("#f44236"), Color.parseColor("#ff9700"), Color.parseColor("#4cb050"))
                .setOnSelectColorListener(new OnSelectColorListener() {
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
                        colorPickerDialog.dismissDialog();
                    }
                })
                .show();
//        customizations
//        colorPickerDialog.setDialogTitle("Choose the color");
//        colorPickerDialog.getDialogTitle().setTextColor(Color.RED);
//        colorPickerDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24)
    }

    public void selectDirectColorDialog() {
        colorPickerDialog = new ColorPickerDialog(MainActivity.this);
        colorPickerDialog.setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.SQUARE)
                .setTickDimenInDp(45)
                .setTickColor(Color.WHITE)
                .setOnDirectSelectColorListener(new OnDirectSelectColorListener() {
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
//        customizations
//        colorPickerDialog.setDialogTitle("Choose the color");
//        colorPickerDialog.getDialogTitle().setTextColor(Color.RED);
//        colorPickerDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24)
    }

    public void selectBottomSheet() {
        bottomSheetDialog = new ColorPickerBottomSheetDialog(this);
        bottomSheetDialog.setColors()
                .setColumns(6)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setTickColor(Color.BLACK, Color.parseColor("#f44236"), Color.parseColor("#ff9700"), Color.parseColor("#4cb050"))
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        btnBottomSheet.setBackgroundColor(color);
                        defaultColor = color;
                    }

                    @Override
                    public void cancel() {
                        bottomSheetDialog.dismissDialog();
                    }
                })
                .show();
//        customizations
//        bottomSheetDialog.getDialogTitle().setTextColor(Color.CYAN);
//        bottomSheetDialog.getPositiveButton().setTextColor(Color.GREEN);
//        bottomSheetDialog.getNegativeButton().setTextColor(Color.RED);
    }

    public void selectDirectBottomSheet() {
        bottomSheetDialog = new ColorPickerBottomSheetDialog(this);
        bottomSheetDialog.setColors()
                .setColumns(6)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.SQUARE)
                .setOnDirectSelectColorListener(new OnDirectSelectColorListener() {
                    @Override
                    public void onDirectColorSelected(int color, int position) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        btnDirectBottomSheet.setBackgroundColor(color);
                        defaultColor = color;
                    }
                });
//        customizations
//        bottomSheetDialog.getDialogBaseLayout().setBackgroundColor(Color.MAGENTA);
//        bottomSheetDialog.setTickColor(Color.YELLOW);
//        bottomSheetDialog.setColorItemDimenInDp(60);
//        bottomSheetDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24);

        bottomSheetDialog.setTickDimenInDp(60);
        bottomSheetDialog.show();
    }
}