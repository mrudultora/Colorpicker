/*
Copyright 2021 Tarek Mohamed Abdalla <tarekkma@gmail.com>

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

import com.mrudultora.colorpicker.listeners.OnDirectSelectColorListener;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;
import com.mrudultora.colorpicker.util.ColorItemShape;

import java.util.ArrayList;
import java.util.HashMap;

public class ColorPickerBuilder {

    private final Context context;

    private int columns = 5;
    private int defaultColor = 0;
    private int itemDrawableRes = 0;
    private int tickColor = Color.WHITE;
    private ColorItemShape colorShape = ColorItemShape.SQUARE;
    private ArrayList<ColorPaletteItemModel> colorsList = new ArrayList<>();
    private HashMap<Integer, Integer> colorItems;
    private String dialogTitle;
    private String dialogPositiveButtonText;
    private String dialogNegativeButtonText;
    private OnDirectSelectColorListener directSelectColorListener;
    private OnSelectColorListener selectColorListener;
    private boolean cardSizeChanged = false;
    private boolean tickSizeChanged = false;
    private float tickSizeDimen = 0f;                 // when equals 0 (default used would be 24dp)
    private float cardViewDimen = 0f;                 // when equals 0 (default used would be 45dp)



    public ColorPickerBuilder(Context context) {
        this.context = context;
    }


    public  ColorPickerDialog buildDialog() {
        if (colorsList == null || colorsList.isEmpty()) {
            setColors();
        }
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


    /**
     * On using OnSelectColorListener the dialog box would have the positive and negative buttons.
     * It would be fired on pressing either of the buttons.
     */
    public ColorPickerBuilder setOnSelectColorListener(OnSelectColorListener selectColorListener) {
        this.selectColorListener = selectColorListener;
        return this;
    }

    /**
     * On using onDirectSelectColorListener the dialog box would not have the positive and negative buttons.
     * It would be fired as soon as a color is pressed.
     */
    public ColorPickerBuilder setOnDirectSelectColorListener(OnDirectSelectColorListener directSelectColorListener) {
        this.directSelectColorListener = directSelectColorListener;
        return this;
    }


    /**
     * Sets the colors from array defined in this library (arrays.xml).
     * In total 15 default colors would be added.
     *
     * @return this
     */
    public ColorPickerBuilder setColors() {
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
    public ColorPickerBuilder setColors(int resId) {
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
    public ColorPickerBuilder setColors(ArrayList<String> colorsHexList) {
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
    public ColorPickerBuilder setColors(int... colors) {
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
    public ColorPickerBuilder setColorItemShape(ColorItemShape colorShape) {
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
    public ColorPickerBuilder setColorItemDrawable(int itemDrawable) {
        this.itemDrawableRes = itemDrawable;
        return this;
    }

    /**
     * Set the value of columns. This value would be used in spanCount of GridLayoutManager.
     *
     * @param columns (column count)
     * @return this
     */
    public ColorPickerBuilder setColumns(int columns) {
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
    public ColorPickerBuilder setDefaultSelectedColor(int defaultColor) {
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
    public ColorPickerBuilder setDefaultSelectedColor(String defaultColor) {
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
    public ColorPickerBuilder setTickColor(int tickColor) {
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
    public ColorPickerBuilder setTickColor(int tickColor, int... colorItems) {
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
    public ColorPickerBuilder setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return this;
    }

    /**
     * Sets the Positive button text of dialog box. Default text is "Ok".
     *
     * @param dialogPositiveButtonText (Positive button text)
     * @return this
     */
    public ColorPickerBuilder setPositiveButtonText(String dialogPositiveButtonText) {
        this.dialogPositiveButtonText = dialogPositiveButtonText;
        return this;
    }

    /**
     * Sets the Negative button text of dialog box. Default text is "Cancel".
     *
     * @param dialogNegativeButtonText (Negative button text)
     * @return this
     */
    public ColorPickerBuilder setNegativeButtonText(String dialogNegativeButtonText) {
        this.dialogNegativeButtonText = dialogNegativeButtonText;
        return this;
    }

    /**
     * Sets the height and width of color items in palette in dp.
     * Default Value is 45dp
     *
     * @param dimen (in dp)
     * @return this
     */
    public ColorPickerBuilder setColorItemDimenInDp(int dimen) {
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
    public ColorPickerBuilder setTickDimenInDp(int dimen) {
        tickSizeChanged = true;
        this.tickSizeDimen = dimen;
        return this;

    }
}
