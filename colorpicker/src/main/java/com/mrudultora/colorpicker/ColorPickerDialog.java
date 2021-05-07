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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudultora.colorpicker.listeners.OnColorItemClickListener;
import com.mrudultora.colorpicker.listeners.OnDirectSelectColorListener;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;
import com.mrudultora.colorpicker.util.ColorItemShape;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 * @since 1 May, 2021
 */
public class ColorPickerDialog implements OnColorItemClickListener {

    private final Context context;

    private final RecyclerView recyclerViewColors;
    private final View dialogView;
    private final RelativeLayout colorPaletteRelLayout;
    private Button positiveButton;
    private Button negativeButton;

    private Dialog dialog;

    private ColorAdapter colorAdapter;

    private int selectedColorPosition = -1;

    private final int columns;
    private final int defaultColor;
    private final int itemDrawableRes;
    private final int tickColor;
    private final ColorItemShape colorShape;
    private final ArrayList<ColorPaletteItemModel> colorsList;
    private final HashMap<Integer, Integer> colorItems;
    private final String dialogTitle;
    private final String dialogPositiveButtonText;
    private final String dialogNegativeButtonText;
    private final OnDirectSelectColorListener directSelectColorListener;
    private final OnSelectColorListener selectColorListener;
    private final boolean cardSizeChanged;
    private final boolean tickSizeChanged;
    private final float tickSizeDimen;                 // when equals 0 (default used would be 24dp)
    private final float cardViewDimen;                 // when equals 0 (default used would be 45dp)


    public ColorPickerDialog(Context context,
                             int columns,
                             int defaultColor,
                             int itemDrawableRes,
                             int tickColor,
                             ColorItemShape colorShape,
                             ArrayList<ColorPaletteItemModel> colorsList,
                             HashMap<Integer, Integer> colorItems,
                             String dialogTitle,
                             String dialogPositiveButtonText,
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
        this.colorShape = colorShape;
        this.colorsList = colorsList;
        this.colorItems = colorItems;
        this.dialogTitle = dialogTitle != null ? dialogTitle : context.getString(R.string.dialog_title);
        this.dialogPositiveButtonText = dialogPositiveButtonText != null ? dialogPositiveButtonText : context.getString(R.string.dialog_positive_button_text);
        this.dialogNegativeButtonText = dialogNegativeButtonText != null ? dialogNegativeButtonText : context.getString(R.string.dialog_negative_button_text);
        this.directSelectColorListener = directSelectColorListener;
        this.selectColorListener = selectColorListener;
        this.cardSizeChanged = cardSizeChanged;
        this.tickSizeChanged = tickSizeChanged;
        this.tickSizeDimen = tickSizeDimen;
        this.cardViewDimen = cardViewDimen;

        this.dialogView = LayoutInflater.from(context).inflate(R.layout.layout_color_palette_dialog, null, false);
        this.colorPaletteRelLayout = dialogView.findViewById(R.id.colorPaletteRelLayout);
        this.recyclerViewColors = dialogView.findViewById(R.id.recyclerViewColors);
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
        if (tickSizeChanged) {
            colorAdapter.customTickSize(tickSizeDimen);
        }
        if (cardSizeChanged) {
            colorAdapter.customCardSize(cardViewDimen);
        }
        recyclerViewColors.setLayoutManager(new GridLayoutManager(context, columns));
        recyclerViewColors.setAdapter(colorAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton(dialogPositiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectColorListener != null && colorsList != null) {
                            if (selectedColorPosition != -1) {
                                int color = colorsList.get(selectedColorPosition).getColor();
                                selectColorListener.onColorSelected(color, selectedColorPosition);
                            } else if (colorAdapter.getColorPosition() != -1) {
                                int position = colorAdapter.getColorPosition();
                                int color = colorsList.get(position).getColor();
                                selectColorListener.onColorSelected(color, position);
                            } else {
                                dismissDialog();
                            }
                        }
                    }
                })
                .setNegativeButton(dialogNegativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectColorListener.cancel();
                    }
                })
                .setTitle(dialogTitle)
                .setCancelable(true);
        dialog = builder.create();
        dialog.show();
        positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

        if (directSelectColorListener != null) {
            positiveButton.setVisibility(View.GONE);
            negativeButton.setVisibility(View.GONE);
        }
    }

    /**
     * Get the positive button from dialog box.
     * This method may throw NullPointerException if the dialog box is not showing on screen.
     *
     * @return positiveButton
     * @throws NullPointerException (if the dialog is null or dialog is not showing).
     */
    public Button getPositiveButton() throws NullPointerException {
        if (dialog == null || !dialog.isShowing()) {
            throw new NullPointerException("Dialog is null or not showing. Call this particular method after the colorPickerDialog.show() is called.");
        }
        positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        return positiveButton;
    }

    /**
     * Get the negative button from dialog box.
     * This method may throw NullPointerException if the dialog box is not showing on screen.
     *
     * @return negativeButton
     * @throws NullPointerException (if the dialog is null or dialog is not showing).
     */
    public Button getNegativeButton() throws NullPointerException {
        if (dialog == null || !dialog.isShowing()) {
            throw new NullPointerException("Dialog is null or not showing. Call this particular method after the colorPickerDialog.show() is called.");
        }
        negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        return negativeButton;
    }

    /**
     * Get the dialog title for customising it.
     * This method may throw NullPointerException if the dialog box is not showing on screen.
     *
     * @return dialogTitle (String)
     */
    public TextView getDialogTitle() throws NullPointerException {
        int titleId = context.getResources().getIdentifier("alertTitle", "id", "android");
        if (dialog.findViewById(titleId) == null) {
            throw new NullPointerException("Dialog is null or not showing. Call this particular method after the colorPickerDialog.show() is called.");
        }
        return dialog.findViewById(titleId);
    }

    /**
     * Get dialog for more control over dialog.
     *
     * @return dialog
     */
    public Dialog getDialog() throws NullPointerException {
        if (dialog == null) {
            throw new NullPointerException("Dialog is null. Call this particular method after the colorPickerDialog.show() is called.");
        }
        return dialog;
    }

    /**
     * Get the view inflated in dialog box.
     *
     * @return dialogView
     */
    public View getDialogView() {
        return dialogView;
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static class Builder extends ColorPickerBuilder<Builder> {

        public Builder(Context context) {
            super(context);
        }

        public ColorPickerDialog build() {
            return new ColorPickerDialog(
                    context,
                    columns,
                    defaultColor,
                    itemDrawableRes,
                    tickColor,
                    colorShape,
                    colorsList,
                    colorItems,
                    dialogTitle,
                    dialogPositiveButtonText,
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
    }
}
