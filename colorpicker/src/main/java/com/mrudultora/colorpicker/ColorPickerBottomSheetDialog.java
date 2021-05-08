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

package com.mrudultora.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mrudultora.colorpicker.listeners.OnColorItemClickListener;
import com.mrudultora.colorpicker.listeners.OnDirectSelectColorListener;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;
import com.mrudultora.colorpicker.util.ColorItemShape;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 * @since 3 May, 2021
 */
public class ColorPickerBottomSheetDialog implements OnColorItemClickListener {

    private final Context context;

    private final RecyclerView recyclerViewColors;
    private final View bottomSheetDialogView;
    private final RelativeLayout colorPaletteRelLayout;
    private final AppCompatTextView dialogTitleText;
    private final View dividerView;
    private final AppCompatButton positiveButton;
    private final AppCompatButton negativeButton;
    private BottomSheetDialog bottomSheetDialog;

    private ColorAdapter colorAdapter;

    private  int selectedColorPosition = -1;

    private final boolean cardSizeChanged ;
    private final boolean tickSizeChanged;
    private final boolean positiveButtonTextChanged;
    private final boolean negativeButtonTextChanged;
    private final boolean titleTextChanged;
    private final int columns;
    private final int defaultColor;
    private final int itemDrawableRes;
    private final int tickColor;
    private final int dividerViewColor;
    private final ColorItemShape colorShape;
    private final ArrayList<ColorPaletteItemModel> colorsList;
    private final HashMap<Integer, Integer> colorItems;
    private final String dialogTitle;
    private final String dialogPositiveButtonText;
    private final String dialogNegativeButtonText;
    private final OnDirectSelectColorListener directSelectColorListener;
    private final OnSelectColorListener selectColorListener;
    private final float tickSizeDimen;                 // when equals 0 (default used would be 24dp)
    private final float cardViewDimen;                 // when equals 0 (default used would be 45dp)

    public ColorPickerBottomSheetDialog(Context context,
                                        int columns,
                                        int defaultColor,
                                        int itemDrawableRes,
                                        int tickColor,
                                        int dividerViewColor,
                                        ColorItemShape colorShape,
                                        ArrayList<ColorPaletteItemModel> colorsList,
                                        HashMap<Integer, Integer> colorItems,
                                        boolean titleTextChanged,
                                        String dialogTitle,
                                        boolean positiveButtonTextChanged,
                                        String dialogPositiveButtonText,
                                        boolean negativeButtonTextChanged,
                                        String dialogNegativeButtonText,
                                        OnDirectSelectColorListener directSelectColorListener,
                                        OnSelectColorListener selectColorListener,
                                        boolean cardSizeChanged,
                                        boolean tickSizeChanged,
                                        float tickSizeDimen,
                                        float cardViewDimen) {
        this.context = context;
        this.columns = columns;
        this.defaultColor = defaultColor;
        this.itemDrawableRes = itemDrawableRes;
        this.tickColor = tickColor;
        this.dividerViewColor = dividerViewColor;
        this.colorShape = colorShape;
        this.colorsList = colorsList;
        this.colorItems = colorItems;
        this.titleTextChanged = titleTextChanged;
        this.positiveButtonTextChanged = positiveButtonTextChanged;
        this.negativeButtonTextChanged = negativeButtonTextChanged;
        this.dialogTitle = dialogTitle != null ? dialogTitle : context.getString(R.string.dialog_title);
        this.dialogPositiveButtonText = dialogPositiveButtonText != null ? dialogPositiveButtonText : context.getString(R.string.dialog_positive_button_text);
        this.dialogNegativeButtonText = dialogNegativeButtonText != null ? dialogNegativeButtonText : context.getString(R.string.dialog_negative_button_text);
        this.directSelectColorListener = directSelectColorListener;
        this.selectColorListener = selectColorListener;
        this.cardSizeChanged = cardSizeChanged;
        this.tickSizeChanged = tickSizeChanged;
        this.tickSizeDimen = tickSizeDimen;
        this.cardViewDimen = cardViewDimen;

        bottomSheetDialogView = LayoutInflater.from(context).inflate(R.layout.layout_color_palette_bottomsheet, null, false);
        colorPaletteRelLayout = bottomSheetDialogView.findViewById(R.id.colorPaletteRelLayout);
        recyclerViewColors = bottomSheetDialogView.findViewById(R.id.recyclerViewColors);
        positiveButton = bottomSheetDialogView.findViewById(R.id.positiveButton);
        negativeButton = bottomSheetDialogView.findViewById(R.id.negativeButton);
        dialogTitleText = bottomSheetDialogView.findViewById(R.id.dialogTitleText);
        dividerView = bottomSheetDialogView.findViewById(R.id.dividerView);
    }

    @Override
    public void onColorItemClick(int position) {
        this.selectedColorPosition = position;
        if (directSelectColorListener != null && colorsList != null) {
            int color = colorsList.get(selectedColorPosition).getColor();
            directSelectColorListener.onDirectColorSelected(color, selectedColorPosition);
            dismissDialog();
        }
    }

    /**
     * Shows the dialog box using AlertDialog.Builder using layout_color_palette_view.xml.
     */
    public void show() {
        if (context == null) {
            return;
        }
        if (itemDrawableRes != 0) {
            colorAdapter = new ColorAdapter(colorsList, context, itemDrawableRes, this);
        } else {
            colorAdapter = new ColorAdapter(colorsList, context, colorShape, this);
        }
        if (colorItems != null) {
            colorAdapter.customTickMarkColorForSomeColors(tickColor, colorItems);
        }
        if (defaultColor != 0) {
            colorAdapter.setDefaultColor(defaultColor);
        }
        if (tickColor != Color.WHITE) {
            colorAdapter.setTickMarkColor(tickColor);
        }
        if (dividerViewColor != 0) {
            dividerView.setBackgroundColor(dividerViewColor);
        }
        if (tickSizeChanged) {
            colorAdapter.customTickSize(tickSizeDimen);
        }
        if (cardSizeChanged) {
            colorAdapter.customCardSize(cardViewDimen);
        }
        recyclerViewColors.setLayoutManager(new GridLayoutManager(context, columns));
        recyclerViewColors.setAdapter(colorAdapter);
        if (titleTextChanged) {
            dialogTitleText.setText(dialogTitle);
        }
        if (positiveButtonTextChanged) {
            positiveButton.setText(dialogPositiveButtonText);
        }
        if (negativeButtonTextChanged) {
            negativeButton.setText(dialogNegativeButtonText);
        }

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setTitle("Choose the title");
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetDialogView.getParent());
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectColorListener != null && colorsList != null) {
                    if (selectedColorPosition != -1) {
                        int color = colorsList.get(selectedColorPosition).getColor();
                        selectColorListener.onColorSelected(color, selectedColorPosition);
                    } else if (colorAdapter.getColorPosition() != -1) {
                        int position = colorAdapter.getColorPosition();
                        int color = colorsList.get(position).getColor();
                        selectColorListener.onColorSelected(color, position);
                    }
                    dismissDialog();
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        if (directSelectColorListener != null) {
            positiveButton.setVisibility(View.GONE);
            negativeButton.setVisibility(View.GONE);
        }
    }

    /**
     * Get the positive button from bottom sheet dialog box.
     *
     * @return positiveButton
     */
    public AppCompatButton getPositiveButton() {
        return positiveButton;
    }

    /**
     * Get the negative button from bottom sheet dialog box.
     *
     * @return negativeButton
     */
    public AppCompatButton getNegativeButton() {
        return negativeButton;
    }

    /**
     * Get the bottom sheet dialog title for customising it.
     *
     * @return dialogTitleText (AppCompatTextView)
     */
    public AppCompatTextView getDialogTitle() {
        return dialogTitleText;
    }

    /**
     * Get dialog for more control over bottom sheet dialog.
     *
     * @return bottomSheetDialog
     */
    public BottomSheetDialog getDialog() throws NullPointerException {
        if (bottomSheetDialog == null) {
            throw new NullPointerException("Dialog is null. Call this particular method after the colorpicker.show() is called.");
        }
        return bottomSheetDialog;
    }

    /**
     * Get the view inflated in dialog box.
     *
     * @return bottomSheetDialogView
     */
    public View getDialogView() {
        return bottomSheetDialogView;
    }

    /**
     * Get the base/parent layout of dialog view for more customizations.
     *
     * @return relativeLayout (colorPaletteRelLayout)
     */
    public RelativeLayout getDialogBaseLayout() {
        return colorPaletteRelLayout;
    }

    /**
     * Dismiss the dialog if it's visible on screen.
     */
    public void dismissDialog() {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.cancel();
        }
    }

    public static class Builder extends ColorPickerBuilder<ColorPickerBottomSheetDialog.Builder> {

        protected int dividerViewColor = 0;
        protected boolean positiveButtonTextChanged;
        protected boolean negativeButtonTextChanged;
        protected boolean titleTextChanged;

        public Builder(Context context) {
            super(context);
        }

        public ColorPickerBottomSheetDialog build() {
            return new ColorPickerBottomSheetDialog(
                    context,
                    columns,
                    defaultColor,
                    itemDrawableRes,
                    tickColor,
                    dividerViewColor,
                    colorShape,
                    colorsList,
                    colorItems,
                    titleTextChanged,
                    dialogTitle,
                    positiveButtonTextChanged,
                    dialogPositiveButtonText,
                    negativeButtonTextChanged,
                    dialogNegativeButtonText,
                    directSelectColorListener,
                    selectColorListener,
                    cardSizeChanged,
                    tickSizeChanged,
                    tickSizeDimen,
                    cardViewDimen
            );
        }

        public void show() {
            build().show();
        }


        /**
         * Sets the color of divider (below the title of dialog).
         *
         * @param backgroundColor (color)
         * @return this
         */
        public Builder setDividerViewColor(int backgroundColor) {
            this.dividerViewColor = backgroundColor;
            return this;
        }

        @Override
        public Builder setDialogTitle(String dialogTitle) {
            titleTextChanged = true;
            return super.setDialogTitle(dialogTitle);
        }

        @Override
        public Builder setPositiveButtonText(String dialogPositiveButtonText) {
            positiveButtonTextChanged = true;
            return super.setPositiveButtonText(dialogPositiveButtonText);
        }

        @Override
        public Builder setNegativeButtonText(String dialogNegativeButtonText) {
            negativeButtonTextChanged = true;
            return super.setNegativeButtonText(dialogNegativeButtonText);
        }
    }
}
