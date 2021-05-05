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
    private final RecyclerView recyclerViewColors;
    private final View dialogView;
    private final RelativeLayout colorPaletteRelLayout;
    private int columns = 5;
    private int defaultColor = 0;
    private int itemDrawableRes = 0;
    private int tickColor = Color.WHITE;
    private ColorItemShape colorShape = ColorItemShape.SQUARE;
    private final Context context;
    private final ArrayList<ColorPaletteItemModel> colorsList;
    private HashMap<Integer, Integer> colorItems;
    private String dialogTitle;
    private String dialogPositiveButtonText;
    private String dialogNegativeButtonText;
    private OnDirectSelectColorListener directSelectColorListener;
    private OnSelectColorListener selectColorListener;
    private Dialog dialog;
    private ColorAdapter colorAdapter;
    private Button positiveButton;
    private Button negativeButton;
    private int selectedColorPosition = -1;
    private boolean cardSizeChanged = false;
    private boolean tickSizeChanged = false;
    private float tickSizeDimen = 0f;                 // when equals 0 (default used would be 24dp)
    private float cardViewDimen = 0f;                 // when equals 0 (default used would be 45dp)

    public ColorPickerDialog(Context context) {
        this.context = context;
        colorsList = new ArrayList<>();
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_color_palette_dialog, null, false);
        colorPaletteRelLayout = dialogView.findViewById(R.id.colorPaletteRelLayout);
        recyclerViewColors = dialogView.findViewById(R.id.recyclerViewColors);
        dialogTitle = context.getString(R.string.dialog_title);
        dialogPositiveButtonText = context.getString(R.string.dialog_positive_button_text);
        dialogNegativeButtonText = context.getString(R.string.dialog_negative_button_text);
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
        if (colorsList == null || colorsList.isEmpty()) {
            setColors();
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
     * On using OnSelectColorListener the dialog box would have the positive and negative buttons.
     * It would be fired on pressing either of the buttons.
     */
    public ColorPickerDialog setOnSelectColorListener(OnSelectColorListener selectColorListener) {
        this.selectColorListener = selectColorListener;
        return this;
    }

    /**
     * On using onDirectSelectColorListener the dialog box would not have the positive and negative buttons.
     * It would be fired as soon as a color is pressed.
     */
    public ColorPickerDialog setOnDirectSelectColorListener(OnDirectSelectColorListener directSelectColorListener) {
        this.directSelectColorListener = directSelectColorListener;
        return this;
    }


    /**
     * Sets the colors from array defined in this library (arrays.xml).
     * In total 15 default colors would be added.
     *
     * @return this
     */
    public ColorPickerDialog setColors() {
        if (context == null) {
            return this;
        }
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.default_colors);
        for (int i = 0; i < typedArray.length(); i++) {
            colorsList.add(new ColorPaletteItemModel(typedArray.getColor(i, 0), false));
        }
        typedArray.recycle();
        return this;
    }

    /**
     * Sets the colors from array defined in the arrays.xml of app.
     * For example, see arrays.xml of this library.
     *
     * @param resId (Array resource)
     * @return this
     */
    public ColorPickerDialog setColors(int resId) {
        if (context == null) {
            return this;
        }
        TypedArray typedArray = context.getResources().obtainTypedArray(resId);
        for (int i = 0; i < typedArray.length(); i++) {
            colorsList.add(new ColorPaletteItemModel(typedArray.getColor(i, 0), false));
        }
        typedArray.recycle();
        return this;
    }

    /**
     * Sets the colors from arrayList of hex values (strings).
     *
     * @param colorsHexList (ArrayList of Strings)
     * @return this
     */
    public ColorPickerDialog setColors(ArrayList<String> colorsHexList) {
        for (String colors : colorsHexList) {
            int color = Color.parseColor(colors);
            colorsList.add(new ColorPaletteItemModel(color, false));
        }
        return this;
    }

    /**
     * Sets the colors from int values of colors.
     * For example, Color.BLUE or Color.parseColor("#000000").
     *
     * @param colors (list of colors int value)
     * @return this
     */
    public ColorPickerDialog setColors(int... colors) {
        for (int color : colors) {
            colorsList.add(new ColorPaletteItemModel(color, false));
        }
        return this;
    }

    /**
     * Set the shape of color item. SQUARE and CIRCLE are the two shapes available.
     * By default, SQUARE would be selected.
     *
     * @param colorShape (shape of color item)
     * @return this
     */
    public ColorPickerDialog setColorItemShape(ColorItemShape colorShape) {
        this.colorShape = colorShape;
        return this;
    }

    /**
     * Sets the drawable of item in color palette (for any shape other than square and circle are
     * also allowed).
     *
     * @param itemDrawable (Drawable resource)
     * @return this
     */
    public ColorPickerDialog setColorItemDrawable(int itemDrawable) {
        this.itemDrawableRes = itemDrawable;
        return this;
    }

    /**
     * Set the value of columns. This value would be used in spanCount of GridLayoutManager.
     *
     * @param columns (column count)
     * @return this
     */
    public ColorPickerDialog setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    /**
     * Sets the default color when dialog box pops up.
     * Default color would have a tick mark on the color.
     *
     * @param defaultColor (default color int value)
     * @return this
     */
    public ColorPickerDialog setDefaultSelectedColor(int defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    /**
     * Sets the default color when dialog box pops up.
     * Default color would have a tick mark on the color.
     *
     * @param defaultColor (default color int value)
     * @return this
     */
    public ColorPickerDialog setDefaultSelectedColor(String defaultColor) {
        this.defaultColor = Color.parseColor(defaultColor);
        return this;
    }

    /**
     * Sets the color of tick mark on item in color palette.
     * Default color is white.
     *
     * @param tickColor (tick color on item in palette)
     * @return this
     */
    public ColorPickerDialog setTickColor(int tickColor) {
        this.tickColor = tickColor;
        return this;
    }

    /**
     * Sets the color of tick mark on particular items in color palette. These items would have
     * the color passed in this method.
     * Default color is white.
     *
     * @param tickColor (tick color on item in palette)
     * @return this
     */
    public ColorPickerDialog setTickColor(int tickColor, int... colorItems) {
        this.tickColor = tickColor;
        this.colorItems = new HashMap<>();
        for (int item : colorItems) {
            this.colorItems.put(item, item);
        }
        return this;
    }

    /**
     * Sets the title of dialog box. Default title is "Choose Color".
     *
     * @param dialogTitle (Title of dialog box)
     * @return this
     */
    public ColorPickerDialog setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return this;
    }

    /**
     * Sets the Positive button text of dialog box. Default text is "Ok".
     *
     * @param dialogPositiveButtonText (Positive button text)
     * @return this
     */
    public ColorPickerDialog setPositiveButtonText(String dialogPositiveButtonText) {
        this.dialogPositiveButtonText = dialogPositiveButtonText;
        return this;
    }

    /**
     * Sets the Negative button text of dialog box. Default text is "Cancel".
     *
     * @param dialogNegativeButtonText (Negative button text)
     * @return this
     */
    public ColorPickerDialog setNegativeButtonText(String dialogNegativeButtonText) {
        this.dialogNegativeButtonText = dialogNegativeButtonText;
        return this;
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

    /**
     * Sets the height and width of color items in palette in dp.
     * Default Value is 45dp
     *
     * @param dimen (in dp)
     * @return this
     */
    public ColorPickerDialog setColorItemDimenInDp(int dimen) {
        cardSizeChanged = true;
        this.cardViewDimen = dimen;
        return this;
    }

    /**
     * Sets the tick mark size in dp. Default value is 24dp.
     *
     * @param dimen (in dp)
     * @return this
     */
    public ColorPickerDialog setTickDimenInDp(int dimen) {
        tickSizeChanged = true;
        this.tickSizeDimen = dimen;
        return this;

    }
}
