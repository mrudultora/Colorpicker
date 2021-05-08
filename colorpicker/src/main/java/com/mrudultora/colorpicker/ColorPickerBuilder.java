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

abstract public class ColorPickerBuilder<B extends ColorPickerBuilder<B>> {


    protected final Context context;

    protected int columns = 5;
    protected int defaultColor = 0;
    protected int itemDrawableRes = 0;
    protected int tickColor = Color.WHITE;
    protected ColorItemShape colorShape = ColorItemShape.SQUARE;
    protected ArrayList<ColorPaletteItemModel> colorsList = new ArrayList<>();
    protected HashMap<Integer, Integer> colorItems;
    protected String dialogTitle;
    protected String dialogPositiveButtonText;
    protected String dialogNegativeButtonText;
    protected OnDirectSelectColorListener directSelectColorListener;
    protected OnSelectColorListener selectColorListener;
    protected boolean cardSizeChanged = false;
    protected boolean tickSizeChanged = false;
    protected float tickSizeDimen = 0f;                 // when equals 0 (default used would be 24dp)
    protected float cardViewDimen = 0f;                 // when equals 0 (default used would be 45dp)



    public ColorPickerBuilder(Context context) {
        this.context = context;
    }

    private B getThis() {
        return (B) this;
    }

    /**
     * On using OnSelectColorListener the dialog box would have the positive and negative buttons.
     * It would be fired on pressing either of the buttons.
     */
    public B setOnSelectColorListener(OnSelectColorListener selectColorListener) {
        this.selectColorListener = selectColorListener;
        return getThis();
    }

    /**
     * On using onDirectSelectColorListener the dialog box would not have the positive and negative buttons.
     * It would be fired as soon as a color is pressed.
     */
    public B setOnDirectSelectColorListener(OnDirectSelectColorListener directSelectColorListener) {
        this.directSelectColorListener = directSelectColorListener;
        return getThis();
    }


    /**
     * Sets the colors from array defined in this library (arrays.xml).
     * In total 15 default colors would be added.
     *
     * @return this
     */
    public B setColors() {
        if (context == null) {
            return getThis();
        }
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.default_colors);
        for (int i = 0; i < typedArray.length(); i++) {
            colorsList.add(new ColorPaletteItemModel(typedArray.getColor(i, 0), false));
        }
        typedArray.recycle();
        return getThis();
    }

    /**
     * Sets the colors from array defined in the arrays.xml of app.
     * For example, see arrays.xml of this library.
     *
     * @param resId (Array resource)
     * @return this
     */
    public B setColors(int resId) {
        if (context == null) {
            return getThis();
        }
        TypedArray typedArray = context.getResources().obtainTypedArray(resId);
        for (int i = 0; i < typedArray.length(); i++) {
            colorsList.add(new ColorPaletteItemModel(typedArray.getColor(i, 0), false));
        }
        typedArray.recycle();
        return getThis();
    }

    /**
     * Sets the colors from arrayList of hex values (strings).
     *
     * @param colorsHexList (ArrayList of Strings)
     * @return this
     */
    public B setColors(ArrayList<String> colorsHexList) {
        for (String colors : colorsHexList) {
            int color = Color.parseColor(colors);
            colorsList.add(new ColorPaletteItemModel(color, false));
        }
        return getThis();
    }

    /**
     * Sets the colors from int values of colors.
     * For example, Color.BLUE or Color.parseColor("#000000").
     *
     * @param colors (list of colors int value)
     * @return this
     */
    public B setColors(int... colors) {
        for (int color : colors) {
            colorsList.add(new ColorPaletteItemModel(color, false));
        }
        return getThis();
    }

    /**
     * Set the shape of color item. SQUARE and CIRCLE are the two shapes available.
     * By default, SQUARE would be selected.
     *
     * @param colorShape (shape of color item)
     * @return this
     */
    public B setColorItemShape(ColorItemShape colorShape) {
        this.colorShape = colorShape;
        return getThis();
    }

    /**
     * Sets the drawable of item in color palette (for any shape other than square and circle are
     * also allowed).
     *
     * @param itemDrawable (Drawable resource)
     * @return this
     */
    public B setColorItemDrawable(int itemDrawable) {
        this.itemDrawableRes = itemDrawable;
        return getThis();
    }

    /**
     * Set the value of columns. This value would be used in spanCount of GridLayoutManager.
     *
     * @param columns (column count)
     * @return this
     */
    public B setColumns(int columns) {
        this.columns = columns;
        return getThis();
    }

    /**
     * Sets the default color when dialog box pops up.
     * Default color would have a tick mark on the color.
     *
     * @param defaultColor (default color int value)
     * @return this
     */
    public B setDefaultSelectedColor(int defaultColor) {
        this.defaultColor = defaultColor;
        return getThis();
    }

    /**
     * Sets the default color when dialog box pops up.
     * Default color would have a tick mark on the color.
     *
     * @param defaultColor (default color int value)
     * @return this
     */
    public B setDefaultSelectedColor(String defaultColor) {
        this.defaultColor = Color.parseColor(defaultColor);
        return getThis();
    }

    /**
     * Sets the color of tick mark on item in color palette.
     * Default color is white.
     *
     * @param tickColor (tick color on item in palette)
     * @return this
     */
    public B setTickColor(int tickColor) {
        this.tickColor = tickColor;
        return getThis();
    }

    /**
     * Sets the color of tick mark on particular items in color palette. These items would have
     * the color passed in this method.
     * Default color is white.
     *
     * @param tickColor (tick color on item in palette)
     * @return this
     */
    public B setTickColor(int tickColor, int... colorItems) {
        this.tickColor = tickColor;
        this.colorItems = new HashMap<>();
        for (int item : colorItems) {
            this.colorItems.put(item, item);
        }
        return getThis();
    }

    /**
     * Sets the title of dialog box. Default title is "Choose Color".
     *
     * @param dialogTitle (Title of dialog box)
     * @return this
     */
    public B setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return getThis();
    }

    /**
     * Sets the Positive button text of dialog box. Default text is "Ok".
     *
     * @param dialogPositiveButtonText (Positive button text)
     * @return this
     */
    public B setPositiveButtonText(String dialogPositiveButtonText) {
        this.dialogPositiveButtonText = dialogPositiveButtonText;
        return getThis();
    }

    /**
     * Sets the Negative button text of dialog box. Default text is "Cancel".
     *
     * @param dialogNegativeButtonText (Negative button text)
     * @return this
     */
    public B setNegativeButtonText(String dialogNegativeButtonText) {
        this.dialogNegativeButtonText = dialogNegativeButtonText;
        return getThis();
    }

    /**
     * Sets the height and width of color items in palette in dp.
     * Default Value is 45dp
     *
     * @param dimen (in dp)
     * @return this
     */
    public B setColorItemDimenInDp(int dimen) {
        cardSizeChanged = true;
        this.cardViewDimen = dimen;
        return getThis();
    }

    /**
     * Sets the tick mark size in dp. Default value is 24dp.
     *
     * @param dimen (in dp)
     * @return this
     */
    public B setTickDimenInDp(int dimen) {
        tickSizeChanged = true;
        this.tickSizeDimen = dimen;
        return getThis();
    }
}
