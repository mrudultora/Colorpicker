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

import androidx.appcompat.widget.AppCompatImageView;

import com.mrudultora.colorpicker.listeners.OnDirectSelectColorListener;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;

public class ColorPickerPopUp implements ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {
    private final Context context;
    private final View dialogView;
    private final View alphaOverlay;
    private final View viewOldColor;
    private final View viewNewColor;
    private final AppCompatImageView cursorColorPicker;
    private final ColorPickerView colorPickerView;
    private final RelativeLayout colorPickerRelLayout;
    private final AppCompatImageView hueImageView;
    private final AppCompatImageView alphaImageView;
    private final AppCompatImageView cursorHue;
    private final AppCompatImageView cursorAlpha;
    private boolean showAlpha = true;
    private String dialogTitle;
    private String dialogPositiveButtonText;
    private String dialogNegativeButtonText;
    private OnDirectSelectColorListener directSelectColorListener;
    private OnSelectColorListener selectColorListener;
    private Dialog dialog;
    private Button positiveButton;
    private Button negativeButton;
    private int selectedColor = Integer.MAX_VALUE;
    private int alpha = 255;
    private float[] currentColorsHSV = new float[]{1f, 1f, 1f};

    public ColorPickerPopUp(Context context) {
        this.context = context;
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_colorpicker_popup, null, false);
        cursorColorPicker = dialogView.findViewById(R.id.cursor_colorpicker);
        colorPickerView = dialogView.findViewById(R.id.colorPickerView);
        colorPickerRelLayout = dialogView.findViewById(R.id.colorPickerRelLayout);
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

    @SuppressLint("ClickableViewAccessibility")
    public void show() {
        if (!showAlpha) {
            alphaImageView.setVisibility(View.GONE);
            alphaOverlay.setVisibility(View.GONE);
            cursorAlpha.setVisibility(View.GONE);
        }
        if (selectedColor == Integer.MAX_VALUE) {
            selectedColor = Color.HSVToColor(currentColorsHSV);
        }
        alpha = Color.alpha(selectedColor);
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

                    }
                })
                .setNegativeButton(dialogNegativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(true);
        dialog = builder.create();
        dialog.show();

        ViewTreeObserver viewTreeObserver = dialogView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);

        colorPickerView.setOnTouchListener(this);
        hueImageView.setOnTouchListener(this);
        alphaImageView.setOnTouchListener(this);
    }

    public ColorPickerPopUp setDefaultColor(int defaultColor) {
        this.selectedColor = defaultColor;
        return this;
    }

    private void setHue(float hue) {
        currentColorsHSV[0] = hue;
        if (showAlpha) {
            updateAlphaOverlay();
        }
    }

    private float getHue() {
        return currentColorsHSV[0];
    }

    private void setSaturation(float saturation) {
        currentColorsHSV[1] = saturation;
    }

    private float getSaturation() {
        return currentColorsHSV[1];
    }

    private void setValue(float value) {
        currentColorsHSV[2] = value;
    }

    private float getValue() {
        return currentColorsHSV[2];
    }

    private void setShowAlpha(boolean bool) {
        this.showAlpha = bool;
    }

    private void updateAlphaOverlay() {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.HSVToColor(currentColorsHSV), Color.TRANSPARENT});
        alphaOverlay.setBackground(gradientDrawable);
    }

    private int getCurrentColor() {
        selectedColor = Color.HSVToColor(currentColorsHSV);
        return alpha << 24 | (selectedColor & 0X00FFFFFF);
    }

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
            int alphaColor = this.alpha << 24 | getCurrentColor() & 0x00FFFFFF;
            moveCursorAlpha();
            viewNewColor.setBackgroundColor(alphaColor);
            return true;
        }
        return false;
    }

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

    private void moveCursorColorPicker() {
        float x = getSaturation() * colorPickerView.getMeasuredWidth();
        float y = (1f - getValue()) * colorPickerView.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursorColorPicker.getLayoutParams();
        layoutParams.leftMargin = (int) (colorPickerView.getLeft() + x - Math.ceil(cursorColorPicker.getMeasuredWidth() / 2f) - colorPickerRelLayout.getPaddingLeft());
        layoutParams.topMargin = (int) (colorPickerView.getTop() + y - Math.ceil(cursorColorPicker.getMeasuredHeight() / 2f) - colorPickerRelLayout.getPaddingTop());
        cursorColorPicker.setLayoutParams(layoutParams);
    }

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

    private void moveCursorAlpha() {
        final int measuredHeight = alphaImageView.getMeasuredHeight();
        float y = measuredHeight - ((this.alpha * measuredHeight) / 255f);
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursorAlpha.getLayoutParams();
        layoutParams.leftMargin = (int) (alphaImageView.getLeft() - Math.floor(cursorAlpha.getMeasuredWidth() / 2f) - colorPickerRelLayout.getPaddingLeft());
        layoutParams.topMargin = (int) ((alphaImageView.getTop() + y) - Math.floor(cursorAlpha.getMeasuredHeight() / 2f) - colorPickerRelLayout.getPaddingTop());
        cursorAlpha.setLayoutParams(layoutParams);
    }

    private boolean isRequiredMotionEvent(MotionEvent motionEvent) {
        return motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                motionEvent.getAction() == MotionEvent.ACTION_UP ||
                motionEvent.getAction() == MotionEvent.ACTION_MOVE;
    }
}
