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

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.mrudultora.colorpicker.ColorPickerBottomSheetDialog;
import com.mrudultora.colorpicker.ColorPickerBuilder;
import com.mrudultora.colorpicker.ColorPickerDialog;
import com.mrudultora.colorpicker.listeners.OnDirectSelectColorListener;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;
import com.mrudultora.colorpicker.util.ColorItemShape;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 */
public class ColorPickerDialogsFragment extends Fragment {
    int defaultColor;
    AppCompatButton btnDialogBox;
    AppCompatButton btnDirectDialogBox;
    AppCompatButton btnBottomSheet;
    AppCompatButton btnDirectBottomSheet;
    ColorPickerDialog colorPickerDialog;
    ColorPickerBottomSheetDialog bottomSheetDialog;
    TabLayout tabLayout;
    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        tabLayout = mainActivity.tabLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colopricker_dialogs, container, false);
        btnDialogBox = view.findViewById(R.id.btnDialogBox);
        btnDirectDialogBox = view.findViewById(R.id.btnDirectDialogBox);
        btnBottomSheet = view.findViewById(R.id.btnBottomSheet);
        btnDirectBottomSheet = view.findViewById(R.id.btnDirectBottomSheet);

        // Some Customizations related to buttons and title must be done after the show() method is called.

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
        return view;
    }

    public void selectColorDialog() {
        colorPickerDialog = new ColorPickerBuilder(getActivity())
                .setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        btnDialogBox.setBackgroundColor(color);
                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(color);
                        defaultColor = color;
                        tabLayout.setBackgroundColor(color);
                    }

                    @Override
                    public void cancel() {
                        colorPickerDialog.dismissDialog();
                    }
                })
                .buildDialog();

//        Some customizations can be done in below ways.

//        colorPickerDialog.setDialogTitle("Choose the color")
//                          .setTickDimenInDp(24);
//        colorPickerDialog.getDialogTitle().setTextColor(Color.RED);
//        colorPickerDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24)

        colorPickerDialog.show();
    }

    public void selectDirectColorDialog() {
        colorPickerDialog = new ColorPickerBuilder(getActivity())
                .setColors()
                .setColumns(5)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.SQUARE)
                .setTickColor(Color.WHITE)
                .setOnDirectSelectColorListener(new OnDirectSelectColorListener() {
                    @Override
                    public void onDirectColorSelected(int color, int position) {
                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(color);
                        btnDirectDialogBox.setBackgroundColor(color);
                        defaultColor = color;
                        tabLayout.setBackgroundColor(color);
                    }
                })
                .buildDialog();

//        Some customizations can be done in below ways.

//        colorPickerDialog.setDialogTitle("Choose the color")
//                        .setTickDimenInDp(45);
//        colorPickerDialog.getDialogTitle().setTextColor(Color.RED);
//        colorPickerDialog.setColorItemDrawable(R.drawable.ic_baseline_star_24)

        colorPickerDialog.show();
    }

    public void selectBottomSheet() {
        bottomSheetDialog = new ColorPickerBottomSheetDialog(getActivity());
        bottomSheetDialog.setColors()
                .setColumns(6)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setTickColor(Color.BLACK, Color.parseColor("#f44236"), Color.parseColor("#ff9700"), Color.parseColor("#4cb050"))
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(color);
                        btnBottomSheet.setBackgroundColor(color);
                        defaultColor = color;
                        tabLayout.setBackgroundColor(color);
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
        bottomSheetDialog = new ColorPickerBottomSheetDialog(getActivity());
        bottomSheetDialog.setColors()
                .setColumns(6)
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.SQUARE)
                .setOnDirectSelectColorListener(new OnDirectSelectColorListener() {
                    @Override
                    public void onDirectColorSelected(int color, int position) {
                        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(color);
                        btnDirectBottomSheet.setBackgroundColor(color);
                        defaultColor = color;
                        tabLayout.setBackgroundColor(color);
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        tabLayout = mainActivity.tabLayout;
    }
}
