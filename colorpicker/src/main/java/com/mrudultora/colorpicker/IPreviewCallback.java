package com.mrudultora.colorpicker;

/**
 * Interface which marks a preview callback.
 * The callback can do much more than preview,
 * but its main purpose is to actually show a preview
 * of the object to be colored on the screen.
 */
public interface IPreviewCallback {
    /**
     * callback method.
     * @param selectedColor
     */
    void preview(int selectedColor);
}
