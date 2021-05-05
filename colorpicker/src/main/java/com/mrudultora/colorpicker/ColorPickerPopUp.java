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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * A ColorPicker pop up to choose any color with or without alpha.
 * Uses ColorPickerView inside. Supports both portrait and landscape orientation.
 *
 * @author Mrudul Tora (mrudultora@gmail.com)
 * @since 6 May, 2021
 */
public class ColorPickerPopUp extends View implements ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {
    private final Context context;
    private final View dialogView;
    private final View alphaOverlay;
    private final View viewOldColor;
    private final View viewNewColor;
    private final AppCompatImageView cursorColorPicker;
    private final ColorPickerView colorPickerView;
    private final RelativeLayout colorPickerRelLayout;
    private final RelativeLayout colorPickerBaseLayout;
    private final AppCompatImageView hueImageView;
    private final AppCompatImageView alphaImageView;
    private final AppCompatImageView cursorHue;
    private final AppCompatImageView cursorAlpha;
    private OnPickColorListener pickColorListener;
    private boolean showAlpha = true;
    private String dialogTitle;
    private String dialogPositiveButtonText;
    private String dialogNegativeButtonText;
    private Dialog dialog;
    private Button positiveButton;
    private Button negativeButton;
    private int selectedColor = Integer.MAX_VALUE;
    private int alpha = 255;
    private float[] currentColorsHSV = new float[]{1f, 1f, 1f};

    public interface OnPickColorListener {
        void onColorPicked(int color);

        void onCancel();
    }

    public ColorPickerPopUp(Context context) {
        super(context);
        this.context = context;
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_colorpicker_popup, null, false);
        cursorColorPicker = dialogView.findViewById(R.id.cursor_colorpicker);
        colorPickerView = dialogView.findViewById(R.id.colorPickerView);
        colorPickerRelLayout = dialogView.findViewById(R.id.colorPickerRelLayout);
        colorPickerBaseLayout = dialogView.findViewById(R.id.colorPickerBaseLayout);
        hueImageView = dialogView.findViewById(R.id.hueImageView);
        alphaImageView = dialogView.findViewById(R.id.alphaImageView);
        cursorHue = dialogView.findViewById(R.id.cursor_hue);
        cursorAlpha = dialogView.findViewById(R.id.cursor_alpha);
        alphaOverlay = dialogView.findViewById(R.id.alpha_overlay);
        viewOldColor = dialogView.findViewById(R.id.viewOldColor);
        viewNewColor = dialogView.findViewById(R.id.viewNewColor);
        dialogTitle = context.getString(R.string.dialog_title);
        dialogPositiveButtonText = context.getString(R.string.dialog_positive_button_text);
        dialogNegativeButtonText = context.getString(R.string.dialog_negative_button_text);
    }

    /**
     * Shows the dialog box using AlertDialog.Builder using layout_colorpicker_popup.xml.
     */
    @SuppressLint("ClickableViewAccessibility")
    public void show() {
        if (selectedColor == Integer.MAX_VALUE) {
            selectedColor = Color.HSVToColor(currentColorsHSV);
        }
        if (!showAlpha) {
            alphaImageView.setVisibility(View.GONE);
            alphaOverlay.setVisibility(View.GONE);
            cursorAlpha.setVisibility(View.GONE);
            // For removing alpha if the default color passed has some alpha value.
            // FF will make all the initial 8 bits equal to one. Doing a bitwise OR will result in
            // the all initial 8 bits equal to 1 and thus nullify the effect of any alpha in the default color.
            // The rest bits are zero. Doing OR with them result in the original bits.
            selectedColor = selectedColor | 0xFF000000;
        } else {
            alpha = Color.alpha(selectedColor);
        }
        Color.colorToHSV(selectedColor, currentColorsHSV);
        viewNewColor.setBackgroundColor(selectedColor);
        viewOldColor.setBackgroundColor(selectedColor);
        colorPickerView.setHue(getHue());
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setView(dialogView)
                .setPositiveButton(dialogPositiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pickColorListener.onColorPicked(selectedColor);
                    }
                })
                .setNegativeButton(dialogNegativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pickColorListener.onCancel();
                    }
                })
                .setCancelable(true);
        dialog = builder.create();
        dialog.show();

        positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

        ViewTreeObserver viewTreeObserver = dialogView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);

        colorPickerView.setOnTouchListener(this);
        hueImageView.setOnTouchListener(this);
        alphaImageView.setOnTouchListener(this);
    }

    /**
     * For handling touch events on colorPickerView, hueImageView, alphaImageView.
     * Sets the hue, saturation, and value on the basis of current coordinates and the size of
     * respective views.
     *
     * @param view        (view on which touch event happened)
     * @param motionEvent (type of motion event)
     * @return boolean
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == colorPickerView && isRequiredMotionEvent(motionEvent)) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            if (x < 0f) {
                x = 0.01f;
            }
            if (x > colorPickerView.getMeasuredWidth()) {
                x = colorPickerView.getMeasuredWidth();
            }
            if (y < 0f) {
                y = 0.01f;
            }
            if (y > colorPickerView.getMeasuredHeight()) {
                y = colorPickerView.getMeasuredHeight();
            }

            setSaturation(1f / colorPickerView.getMeasuredWidth() * x);
            setValue(1f - (1f / colorPickerView.getMeasuredHeight() * y));
            moveCursorColorPicker();
            viewNewColor.setBackgroundColor(getCurrentColor());
            return true;
        } else if (hueImageView != null && view == hueImageView && isRequiredMotionEvent(motionEvent)) {
            float y = motionEvent.getY();
            if (y < 0f) {
                y = 0.01f;
            }
            if (y > hueImageView.getMeasuredHeight()) {
                y = hueImageView.getMeasuredHeight() - 0.01f;       // subtracted 0.01f to avoid cursor jumping from bottom to top.
            }
            float hue = 360f - 360f / hueImageView.getMeasuredHeight() * y;
            if (hue == 360f) {
                hue = 0f;
            }
            setHue(hue);

            colorPickerView.setHue(getHue());
            viewNewColor.setBackgroundColor(getCurrentColor());
            moveCursorHue();
            updateAlphaOverlay();
            return true;

        } else if (showAlpha && alphaImageView != null && view == alphaImageView && isRequiredMotionEvent(motionEvent)) {
            float y = motionEvent.getY();
            if (y < 0f) {
                y = 0.01f;
            }
            if (y > alphaImageView.getMeasuredHeight()) {
                y = alphaImageView.getMeasuredHeight() - 0.01f;     // subtracted 0.01f to avoid cursor jumping from bottom to top.
            }
            this.alpha = Math.round(255f - ((255f / alphaImageView.getMeasuredHeight()) * y));
            // see javadoc of getCurrentColor().
            selectedColor = this.alpha << 24 | getCurrentColor() & 0x00FFFFFF;
            moveCursorAlpha();
            viewNewColor.setBackgroundColor(selectedColor);
            return true;
        }
        return false;
    }

    /**
     * This method will be called on very first time the dialog would be showed.
     * Used to set the positions of cursors/pointers for first time the dialog pops up.
     */
    @Override
    public void onGlobalLayout() {
        moveCursorHue();
        if (showAlpha) {
            moveCursorAlpha();
            updateAlphaOverlay();
        }
        moveCursorColorPicker();
        dialogView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /**
     * This method is used to move the cursor/pointer in colorPickerView.
     * It sets the position of cursor according to the coordinates of touch on colorPickerView.
     * For accuracy, the x,y coordinates are found out using current saturation and value.
     * Padding of the parent view and size of cursor is taken into account for setting margins.
     */
    private void moveCursorColorPicker() {
        float x = getSaturation() * colorPickerView.getMeasuredWidth();
        float y = (1f - getValue()) * colorPickerView.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursorColorPicker.getLayoutParams();
        layoutParams.leftMargin = (int) (colorPickerView.getLeft() + x - Math.ceil(cursorColorPicker.getMeasuredWidth() / 2f) - colorPickerRelLayout.getPaddingLeft());
        layoutParams.topMargin = (int) (colorPickerView.getTop() + y - Math.ceil(cursorColorPicker.getMeasuredHeight() / 2f) - colorPickerRelLayout.getPaddingTop());
        cursorColorPicker.setLayoutParams(layoutParams);
    }

    /**
     * This method is used to move the cursor/pointer in hueImageView.
     * It sets the position of cursor according to the coordinates of touch on hueImageView.
     * For accuracy, the y coordinate is found out using current hue.
     * Padding of the parent view and size of cursor is taken into account for setting margins.
     */
    private void moveCursorHue() {
        float y = hueImageView.getMeasuredHeight() - (getHue() * hueImageView.getMeasuredHeight() / 360f);
        if (y == hueImageView.getMeasuredHeight()) {
            y = 0.1f;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursorHue.getLayoutParams();
        layoutParams.leftMargin = (int) (hueImageView.getLeft() - Math.ceil(cursorHue.getMeasuredWidth() / 2f) - colorPickerRelLayout.getPaddingLeft());
        layoutParams.topMargin = (int) (hueImageView.getTop() + y - Math.ceil(cursorHue.getMeasuredHeight() / 2f) - colorPickerRelLayout.getPaddingTop());
        cursorHue.setLayoutParams(layoutParams);
    }

    /**
     * This method is used to move the cursor/pointer in alphaImageView (if visible).
     * It sets the position of cursor according to the coordinates of touch on alphaImageView.
     * For accuracy, the y coordinate is found out using current alpha.
     * Padding of the parent view and size of cursor is taken into account for setting margins.
     */
    private void moveCursorAlpha() {
        final int measuredHeight = alphaImageView.getMeasuredHeight();
        float y = measuredHeight - ((this.alpha * measuredHeight) / 255f);
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursorAlpha.getLayoutParams();
        layoutParams.leftMargin = (int) (alphaImageView.getLeft() - Math.floor(cursorAlpha.getMeasuredWidth() / 2f) - colorPickerRelLayout.getPaddingLeft());
        layoutParams.topMargin = (int) ((alphaImageView.getTop() + y) - Math.floor(cursorAlpha.getMeasuredHeight() / 2f) - colorPickerRelLayout.getPaddingTop());
        cursorAlpha.setLayoutParams(layoutParams);
    }

    /**
     * To change the gradient of overlay over the drawable of alphaImageView with repect to the
     * selected color. Only used if showAlpha is set to true.
     */
    private void updateAlphaOverlay() {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.HSVToColor(currentColorsHSV), Color.TRANSPARENT});
        alphaOverlay.setBackground(gradientDrawable);
    }

    /**
     * Selected color is equal to current value of HSV.
     * In case, alpha is added then it must be counted.
     * Understanding the return statement :
     * <p>
     * Alpha is used for first 8 bits (out of 32 bits). To make 8 bits to 32 bits left shift is used.
     * Final alpha would be 32 bits (initial 8 bits are value of current alpha and remaining are zeroes).
     * Any present alpha is removed from the selected color by doing bitwise AND with 0X00FFFFFF.
     * Initial 8 bits are zero and thus all the present alpha in selected color would become zero.
     * After that a bitwise OR would assert that the current value of alpha combines with the
     * selected color without alpha.
     * <p>
     * This would produce the exact required color (color picked).
     *
     * @return color (int)
     */
    private int getCurrentColor() {
        selectedColor = Color.HSVToColor(currentColorsHSV);
        return alpha << 24 | (selectedColor & 0X00FFFFFF);
    }

    /**
     * Sets the hue on basis of position on touch in hueImageView.
     * Range of hue is 0-360.
     *
     * @param hue (float)
     */
    private void setHue(float hue) {
        currentColorsHSV[0] = hue;
        if (showAlpha) {
            updateAlphaOverlay();
        }
    }

    /**
     * Gets the current hue.
     *
     * @return float
     */
    private float getHue() {
        return currentColorsHSV[0];
    }

    /**
     * Sets the saturation on basis of position on touch in colorPickerView.
     * Range of saturation is 0-1.
     *
     * @param saturation (float)
     */
    private void setSaturation(float saturation) {
        currentColorsHSV[1] = saturation;
    }

    /**
     * Gets the current saturation.
     *
     * @return float
     */
    private float getSaturation() {
        return currentColorsHSV[1];
    }

    /**
     * Sets the value on basis of position on touch in colorPickerView.
     * Range of value is 0-1.
     *
     * @param value (float)
     */
    private void setValue(float value) {
        currentColorsHSV[2] = value;
    }

    /**
     * Gets the current value.
     *
     * @return float
     */
    private float getValue() {
        return currentColorsHSV[2];
    }

    /**
     * Returns whether the motion event is valid or not according to the need of color picker.
     *
     * @param motionEvent (Motion Event)
     * @return boolean
     */
    private boolean isRequiredMotionEvent(MotionEvent motionEvent) {
        return motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                motionEvent.getAction() == MotionEvent.ACTION_UP ||
                motionEvent.getAction() == MotionEvent.ACTION_MOVE;
    }

    /**
     * Sets the default color in ColorPickerView, Hue and Alpha (if enabled).
     * Default color if not set is Color.HSVToColor(new float[]{1f, 1f, 1f}).
     *
     * @param defaultColor (int format)
     * @return this
     */
    public ColorPickerPopUp setDefaultColor(int defaultColor) {
        this.selectedColor = defaultColor;
        return this;
    }

    /**
     * On using OnPickColorListener to select the picked color.
     * It would be fired on pressing either of the buttons (positive or negative).
     *
     * @param onPickColorListener (onPickColorListener)
     * @return this
     */
    public ColorPickerPopUp setOnPickColorListener(OnPickColorListener onPickColorListener) {
        this.pickColorListener = onPickColorListener;
        return this;
    }

    /**
     * Sets whether to show Alpha Channel or not.
     * Default value is true.
     *
     * @param showAlpha (to show alpha or not)
     * @return this
     */
    public ColorPickerPopUp setShowAlpha(boolean showAlpha) {
        this.showAlpha = showAlpha;
        return this;
    }

    /**
     * Sets the title of dialog box. Default title is "Choose Color".
     *
     * @param dialogTitle (Title of dialog box)
     * @return this
     */
    public ColorPickerPopUp setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return this;
    }

    /**
     * Sets the Positive button text of dialog box. Default text is "Ok".
     *
     * @param dialogPositiveButtonText (Positive button text)
     * @return this
     */
    public ColorPickerPopUp setPositiveButtonText(String dialogPositiveButtonText) {
        this.dialogPositiveButtonText = dialogPositiveButtonText;
        return this;
    }

    /**
     * Sets the Negative button text of dialog box. Default text is "Cancel".
     *
     * @param dialogNegativeButtonText (Negative button text)
     * @return this
     */
    public ColorPickerPopUp setNegativeButtonText(String dialogNegativeButtonText) {
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
            throw new NullPointerException("Dialog is null or not showing. Call this particular method after the colorPickerPopUp.show() is called.");
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
            throw new NullPointerException("Dialog is null or not showing. Call this particular method after the colorPickerPopUp.show() is called.");
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
        if (dialog == null || !dialog.isShowing()) {
            throw new NullPointerException("Dialog is null or not showing. Call this particular method after the colorPickerPopUp.show() is called.");
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
            throw new NullPointerException("Dialog is null. Call this particular method after the colorPickerPopUp.show() is called.");
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
        return colorPickerBaseLayout;
    }

    /**
     * Dismiss the dialog if it's visible on screen.
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}