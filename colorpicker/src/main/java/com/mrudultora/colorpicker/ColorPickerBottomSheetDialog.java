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
    private final RecyclerView recyclerViewColors;
    private final View bottomSheetDialogView;
    private final RelativeLayout colorPaletteRelLayout;
    private final AppCompatTextView dialogTitleText;
    private final View dividerView;
    private final AppCompatButton positiveButton;
    private final AppCompatButton negativeButton;
    private boolean cardSizeChanged = false;
    private boolean tickSizeChanged = false;
    private boolean positiveButtonTextChanged = false;
    private boolean negativeButtonTextChanged = false;
    private boolean titleTextChanged = false;
    private int columns = 5;
    private int defaultColor = 0;
    private int itemDrawableRes = 0;
    private int tickColor = Color.WHITE;
    private int dividerViewColor = 0;
    private ColorItemShape colorShape = ColorItemShape.SQUARE;
    private final Context context;
    private final ArrayList<ColorPaletteItemModel> colorsList;
    private HashMap<Integer,Integer> colorItems;
    private String dialogTitle;
    private String dialogPositiveButtonText;
    private String dialogNegativeButtonText;
    private OnDirectSelectColorListener directSelectColorListener;
    private OnSelectColorListener selectColorListener;
    private BottomSheetDialog bottomSheetDialog;
    private ColorAdapter colorAdapter;
    private int selectedColorPosition = -1;
    private float tickSizeDimen = 0f;                 // when equals 0 (default used would be 24dp)
    private float cardViewDimen = 0f;                 // when equals 0 (default used would be 45dp)

    public ColorPickerBottomSheetDialog(Context context) {
        this.context = context;
        colorsList = new ArrayList<>();
        bottomSheetDialogView = LayoutInflater.from(context).inflate(R.layout.layout_color_palette_bottomsheet, null, false);
        colorPaletteRelLayout = bottomSheetDialogView.findViewById(R.id.colorPaletteRelLayout);
        recyclerViewColors = bottomSheetDialogView.findViewById(R.id.recyclerViewColors);
        positiveButton = bottomSheetDialogView.findViewById(R.id.positiveButton);
        negativeButton = bottomSheetDialogView.findViewById(R.id.negativeButton);
        dialogTitleText = bottomSheetDialogView.findViewById(R.id.dialogTitleText);
        dividerView = bottomSheetDialogView.findViewById(R.id.dividerView);
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
     * On using OnSelectColorListener the bottom sheet dialog box would have the positive and negative buttons.
     * It would be fired on pressing either of the buttons.
     */
    public ColorPickerBottomSheetDialog setOnSelectColorListener(OnSelectColorListener selectColorListener) {
        this.selectColorListener = selectColorListener;
        return this;
    }

    /**
     * On using onDirectSelectColorListener the bottom sheet dialog box would not have the positive and negative buttons.
     * It would be fired as soon as a color is pressed.
     */
    public ColorPickerBottomSheetDialog setOnDirectSelectColorListener(OnDirectSelectColorListener directSelectColorListener) {
        this.directSelectColorListener = directSelectColorListener;
        return this;
    }

    /**
     * Sets the colors from array defined in this library (arrays.xml).
     * In total 15 default colors would be added.
     *
     * @return this
     */
    public ColorPickerBottomSheetDialog setColors() {
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
    public ColorPickerBottomSheetDialog setColors(int resId) {
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
    public ColorPickerBottomSheetDialog setColors(ArrayList<String> colorsHexList) {
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
    public ColorPickerBottomSheetDialog setColors(int... colors) {
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
    public ColorPickerBottomSheetDialog setColorItemShape(ColorItemShape colorShape) {
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
    public ColorPickerBottomSheetDialog setColorItemDrawable(int itemDrawable) {
        this.itemDrawableRes = itemDrawable;
        return this;
    }

    /**
     * Set the value of columns. This value would be used in spanCount of GridLayoutManager.
     *
     * @param columns (column count)
     * @return this
     */
    public ColorPickerBottomSheetDialog setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    /**
     * Sets the default color when bottom sheet dialog pops up.
     * Default color would have a tick mark on the color.
     *
     * @param defaultColor (default color int value)
     * @return this
     */
    public ColorPickerBottomSheetDialog setDefaultSelectedColor(int defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    /**
     * Sets the default color when bottom sheet dialog pops up.
     * Default color would have a tick mark on the color.
     *
     * @param defaultColor (default color int value)
     * @return this
     */
    public ColorPickerBottomSheetDialog setDefaultSelectedColor(String defaultColor) {
        this.defaultColor = Color.parseColor(defaultColor);
        return this;
    }

    /**
     * Sets the color of tick mark on item in color palette on every color item selected.
     * Default color is white.
     *
     * @param tickColor (tick color on item in palette)
     * @return this
     */
    public ColorPickerBottomSheetDialog setTickColor(int tickColor) {
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
    public ColorPickerBottomSheetDialog setTickColor(int tickColor, int... colorItems) {
        this.tickColor = tickColor;
        this.colorItems = new HashMap<>();
        for (int item : colorItems) {
            this.colorItems.put(item,item);
        }
        return this;
    }

    /**
     * Sets the color of divider (below the title of dialog).
     *
     * @param backgroundColor (color)
     * @return this
     */
    public ColorPickerBottomSheetDialog setDividerViewColor(int backgroundColor) {
        this.dividerViewColor = backgroundColor;
        return this;
    }

    /**
     * Sets the title of dialog box. Default title is "Choose Color".
     *
     * @param dialogTitle (Title of bottom sheet dialog box)
     * @return this
     */
    public ColorPickerBottomSheetDialog setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        titleTextChanged = true;
        return this;
    }

    /**
     * Sets the Positive button text of dialog box. Default text is "Ok".
     *
     * @param dialogPositiveButtonText (Positive button text)
     * @return this
     */
    public ColorPickerBottomSheetDialog setPositiveButtonText(String dialogPositiveButtonText) {
        this.dialogPositiveButtonText = dialogPositiveButtonText;
        positiveButtonTextChanged = true;
        return this;
    }

    /**
     * Sets the Negative button text of dialog box. Default text is "Cancel".
     *
     * @param dialogNegativeButtonText (Negative button text)
     * @return this
     */
    public ColorPickerBottomSheetDialog setNegativeButtonText(String dialogNegativeButtonText) {
        this.dialogNegativeButtonText = dialogNegativeButtonText;
        negativeButtonTextChanged = true;
        return this;
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

    /**
     * Sets the height and width of color items in palette in dp.
     * Default Value is 45dp
     *
     * @param dimen (in dp)
     * @return this
     */
    public ColorPickerBottomSheetDialog setColorItemDimenInDp(int dimen) {
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
    public ColorPickerBottomSheetDialog setTickDimenInDp(int dimen) {
        tickSizeChanged = true;
        this.tickSizeDimen = dimen;
        return this;
    }
}
