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
    ColorPickerPopUp colorPickerPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        For setting the dark mode
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDialogBox = findViewById(R.id.btnDialogBox);
        btnDirectDialogBox = findViewById(R.id.btnDirectDialogBox);
        btnBottomSheet = findViewById(R.id.btnBottomSheet);
        btnDirectBottomSheet = findViewById(R.id.btnDirectBottomSheet);
        btnColorPickerPopup = findViewById(R.id.btnColorPickerPopup);
        colorPickerView = findViewById(R.id.colorPickerView);

//        Some Customizations related to buttons and title must be done after the show() method is called.

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
                popUpColorPicker();
            }
        });
    }

    public void selectColorDialog() {
        colorPickerDialog = new ColorPickerDialog(MainActivity.this);
        colorPickerDialog.setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        btnDialogBox.setBackgroundColor(color);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        getWindow().setStatusBarColor(color);
                        defaultColor = color;
                    }

                    @Override
                    public void cancel() {
                        colorPickerDialog.dismissDialog();
                    }
                });

//        Some customizations can be done in below ways.

//        colorPickerDialog.setDialogTitle("Choose the color")
//                          .setTickDimenInDp(24);
//        colorPickerDialog.getDialogTitle().setTextColor(Color.RED);
//        colorPickerDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24)

        colorPickerDialog.show();
    }

    public void selectDirectColorDialog() {
        colorPickerDialog = new ColorPickerDialog(MainActivity.this);
        colorPickerDialog.setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.SQUARE)
                .setTickColor(Color.WHITE)
                .setOnDirectSelectColorListener(new OnDirectSelectColorListener() {
                    @Override
                    public void onDirectColorSelected(int color, int position) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        getWindow().setStatusBarColor(color);
                        btnDirectDialogBox.setBackgroundColor(color);
                        defaultColor = color;
                    }
                });

//        Some customizations can be done in below ways.

//        colorPickerDialog.setDialogTitle("Choose the color")
//                        .setTickDimenInDp(45);
//        colorPickerDialog.getDialogTitle().setTextColor(Color.RED);
//        colorPickerDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24)

        colorPickerDialog.show();
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
                        getWindow().setStatusBarColor(color);
                        btnBottomSheet.setBackgroundColor(color);
                        defaultColor = color;
                    }

                    @Override
                    public void cancel() {
                        bottomSheetDialog.dismissDialog();
                    }
                });

//        Some customizations can be done in below ways.

//        bottomSheetDialog.getDialogTitle().setTextColor(Color.CYAN);
//        bottomSheetDialog.getPositiveButton().setTextColor(Color.GREEN);
//        bottomSheetDialog.getNegativeButton().setTextColor(Color.RED);

        bottomSheetDialog.show();
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
                        getWindow().setStatusBarColor(color);
                        btnDirectBottomSheet.setBackgroundColor(color);
                        defaultColor = color;
                    }
                });

//        Some customizations can be done in below ways.

//        bottomSheetDialog.getDialogBaseLayout().setBackgroundColor(Color.MAGENTA);
//        bottomSheetDialog.setTickColor(Color.YELLOW);
//                          .setColorItemDimenInDp(60)
//                          .setColorItemDrawable(R.drawable.ic_baseline_star_24)
//                          .setTickDimenInDp(60);

        bottomSheetDialog.show();
    }

    public void popUpColorPicker() {
        colorPickerPopUp = new ColorPickerPopUp(MainActivity.this);
        colorPickerPopUp.setShowAlpha(true)
                .setDefaultColor(defaultColor)
                .setDialogTitle("Pick a Color")
                .setPositiveButtonText("Okay")
                .setNegativeButtonText("Kancel")
                .setOnPickColorListener(new ColorPickerPopUp.OnPickColorListener() {
                    @Override
                    public void onColorPicked(int color) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        getWindow().setStatusBarColor(color);
                        btnColorPickerPopup.setBackgroundColor(color);
                        defaultColor = color;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            System.out.println("Color picked is :" + Color.valueOf(color));
                        }
                    }

                    @Override
                    public void onCancel() {
                        colorPickerPopUp.dismissDialog();
                    }
                });
        colorPickerPopUp.show();

//        colorPickerPopUp.getDialogTitle().setTextColor(Color.RED);
    }
}