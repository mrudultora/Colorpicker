# <div align="center">Colorpicker Library for Android</div>
<div align="center">Simple, maintained and highly customizable color picker library for Android. It is packed, ColorPicker Popup, Colorpicker dialogs and ColorPicker BottomSheet dialogs. ColorPicker Popup supports alpha and uses HSV (behind the scene) and allows you set your defaults. It supports Dark Mode and Material Theme and uses AndroidX. 🎨</div>
<br>
<div align="center">
  
   [![Android Build](https://github.com/mrudultora/Colorpicker/actions/workflows/generate-apk.yml/badge.svg)](https://github.com/mrudultora/Colorpicker/actions/workflows/generate-apk.yml)
   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
   [![](https://jitpack.io/v/mrudultora/Colorpicker.svg)](https://jitpack.io/#mrudultora/Colorpicker)
   <img src="https://img.shields.io/badge/API-21%2B-darkcyan.svg" alt="API" /> 
   <span class="badge-paypal"><a href="https://www.paypal.com/paypalme/mrudultora" title="Donate to this project using Paypal"><img src="https://img.shields.io/badge/paypal-donate-purple.svg" alt="Donate" /></a></span>
  [![Twitter](https://img.shields.io/twitter/url/https/twitter.com/mrudultora24.svg?style=social&label=Follow%20%40mrudultora24)](https://twitter.com/mrudultora24)
</div>

## Highlights

• ColorPicker Popup (comes with ColorPickerView) to pick colors using Hue, Saturation and Value (HSV).<br>
• ColorPicker AlertDialogs to choose color from the list of colors.<br>
• ColorPicker BottomSheetDialogs to choose color from the list of colors.<br>
• Supports Dark Mode as well as Landscape Orientation.<br>

## Demo

<div align="center"> 
  
  | ColorPicker Dialogs | ColorPicker Popup |
  | --- | --- |
  | <img src="https://github.com/mrudultora/Colorpicker/blob/master/demo-art/colorpicker-dialogs.gif" alt="ColorPicker Dialogs" width="300" height="650"> | <img src="https://github.com/mrudultora/Colorpicker/blob/master/demo-art/colorpicker-popup.gif" alt="ColorPicker Dialogs" width="300" height="650"> |
  
</div>

## Gradle
Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Now add the following dependency in your app's `build.gradle`
```groovy
dependencies {
        implementation 'com.github.mrudultora:Colorpicker:1.2.0'
}
```

## Usage
This library comes with three types of dialogs. These are as follows:<br>

• ColorPickerPopup (An AlertDialog with pick color using HSV).<br>
• ColorPickerDialog (An AlertDialog with list of colors).<br>
• ColorPickerBottomSheetDialog (An BottomSheetDialog with list of colors).<br>

**• ColorPickerPopUp**
```java
ColorPickerPopUp colorPickerPopUp = new ColorPickerPopUp(context);
        colorPickerPopUp.setShowAlpha(true)
                .setDefaultColor(defaultColor)
                .setDialogTitle("Pick a Color")
                .setOnPickColorListener(new ColorPickerPopUp.OnPickColorListener() {
                    @Override
                    public void onColorPicked(int color) {
                          // handle the use of color
                    }
                    
                    @Override
                    public void onCancel() {
                        colorPickerPopUp.dismissDialog();
                    }
                });
        colorPickerPopUp.show();
```
**• ColorPickerDialog**
```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(context);
        colorPickerDialog.setColors()
                .setColumns(4)                        // default number of columns is 5.
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        // handle color or position
                    }

                    @Override
                    public void cancel() {
                        colorPickerDialog.dismissDialog();
                    }
                }).show();
```
**• ColorPickerBottomSheetDialog**
```java
ColorPickerBottomSheetDialog bottomSheetDialog = new ColorPickerBottomSheetDialog(context);
        bottomSheetDialog.setColors()
                .setColumns(6)                                   // default number of columns is 5.
                .setDefaultSelectedColor(defaultColor)
                .setColorItemShape(ColorItemShape.CIRCLE)        // default shape is SQAURE.
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        // handle color or position
                    }

                    @Override
                    public void cancel() {
                        bottomSheetDialog.dismissDialog();
                    }
                }).show();
```
