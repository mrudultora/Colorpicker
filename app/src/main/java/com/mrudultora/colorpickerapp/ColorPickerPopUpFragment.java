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
import com.mrudultora.colorpicker.ColorPickerPopUp;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 */
public class ColorPickerPopUpFragment extends Fragment {
    AppCompatButton btnColorPickerPopup;
    ColorPickerPopUp colorPickerPopUp;
    int defaultColor = Color.parseColor("#D9FF0089");
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
        View view = inflater.inflate(R.layout.fragment_colorpicker_popup, container, false);
        btnColorPickerPopup = view.findViewById(R.id.btnColorPickerPopup);
        btnColorPickerPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpColorPicker();
            }
        });
        return view;
    }

    public void popUpColorPicker() {
        colorPickerPopUp = new ColorPickerPopUp(getActivity());
        colorPickerPopUp.setShowAlpha(true)
                .setDefaultColor(defaultColor)
                .setDialogTitle("Pick a Color")
                .setOnPickColorListener(new ColorPickerPopUp.OnPickColorListener() {
                    @Override
                    public void onColorPicked(int color) {
                        if (((AppCompatActivity) getActivity()) != null) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        }
                        ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(color);
                        btnColorPickerPopup.setBackgroundColor(color);
                        defaultColor = color;
                        tabLayout.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {
                        colorPickerPopUp.dismissDialog();
                    }
                });
        colorPickerPopUp.show();

//        Some Customizations related to buttons and title must be done after the show() method is called.
//        colorPickerPopUp.getDialogTitle().setTextColor(Color.RED);
//        colorPickerPopUp.getPositiveButton().setTextColor(Color.BLUE);

    }
}
