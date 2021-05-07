# <div align="center">Colorpicker Library for Android</div>
<div align="center">Simple, maintained and highly customizable color picker library for Android. It is packed, ColorPicker Popup, Colorpicker dialogs and ColorPicker BottomSheet dialogs. ColorPicker Popup supports alpha and uses HSV (behind the scene) and allows you set your defaults. It supports Dark Mode and Material Theme and uses AndroidX. 🎨</div>
<br>
<div align="center">
  
   [![Android Build](https://github.com/mrudultora/Colorpicker/actions/workflows/generate-apk.yml/badge.svg)](https://github.com/mrudultora/Colorpicker/actions/workflows/generate-apk.yml)
   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
   [![](https://jitpack.io/v/mrudultora/Colorpicker.svg)](https://jitpack.io/#mrudultora/Colorpicker)
   <img src="https://img.shields.io/badge/API-21%2B-darkcyan.svg" alt="API" /> 
   <span class="badge-paypal"><a href="https://www.paypal.com/paypalme/mrudultora" title="Donate to this project using Paypal"><img src="https://img.shields.io/badge/paypal-donate-purple.svg" alt="Donate" /></a></span>
  <a href="https://mrudultora.github.io/libraries/ColorPicker/javadoc/colorpicker-javadoc.html"><img alt="Javadoc" src="https://mrudultora.github.io/Github-badges/javadoc-colorpicker-badge.svg"/></a>
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
  | <img src="https://github.com/mrudultora/Colorpicker/blob/master/demo-art/colorpicker-dialogs.gif" alt="ColorPicker Dialogs" width="300" height="600"> | <img src="https://github.com/mrudultora/Colorpicker/blob/master/demo-art/colorpicker-popup.gif" alt="ColorPicker Dialogs" width="300" height="600"> |
  
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

**• ColorPickerPopUp**
```java
ColorPickerPopUp colorPickerPopUp = new ColorPickerPopUp(context);	// Pass the context.
        colorPickerPopUp.setShowAlpha(true)			// By default show alpha is true.
                .setDefaultColor(defaultColor)			// By default red color is set.
                .setDialogTitle("Pick a Color")
                .setOnPickColorListener(new ColorPickerPopUp.OnPickColorListener() {
                    @Override
                    public void onColorPicked(int color) {
                          // handle the use of color
                    }
                    
                    @Override
                    public void onCancel() {
                        colorPickerPopUp.dismissDialog();	// Dismiss the dialog.
                    }
                });
        colorPickerPopUp.show();
```
**• ColorPickerDialog**
```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(context);   // Pass the context.
        colorPickerDialog.setColors()					
                .setColumns(4)                        		// Default number of columns is 5.
                .setDefaultSelectedColor(defaultColor)		// By default no color is used.
                .setColorItemShape(ColorItemShape.CIRCLE)		
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        // handle color or position
                    }

                    @Override
                    public void cancel() {
                        colorPickerDialog.dismissDialog();	// Dismiss the dialog.
                    }
                }).show();
```
**• ColorPickerBottomSheetDialog**
```java
ColorPickerBottomSheetDialog bottomSheetDialog = new ColorPickerBottomSheetDialog(context); 
        bottomSheetDialog.setColumns(6) 				  	
                .setColors()                                  // Default colors list is used.
                .setDefaultSelectedColor(defaultColor)		  
                .setColorItemShape(ColorItemShape.CIRCLE)     // Default shape is SQUARE             
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        // handle color or position
                    }

                    @Override
                    public void cancel() {
                        bottomSheetDialog.dismissDialog();     // Dismiss the dialog.
                    }
                }).show();
```
## Customization🎨
• You can change the text of Title, Positive and Negative Buttons directly from the strings.xml by adding following parameters.
```xml
<resources>
    <!-- Strings related to dialog box -->
    <string name="dialog_title">Choose Color</string>
    <string name="dialog_positive_button_text">@android:string/ok</string>
    <string name="dialog_negative_button_text">@android:string/cancel</string>
</resources>
```
• There are 3 ways to pass the list of colors in `setColors` method. If no parameter is passed, then 15 default colors array would be used on calling `setColors()`.
```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(context);   // Pass the context.
        colorPickerDialog
	
	// Option 1: Pass the resource id of array
	.setColors(R.array.array_colors)
	
	//Option 2: Pass the arraylist of HEX colors in String format.
	.setColors(new ArrayList<>(Arrays.asList("#95afc0", "#ff7979", "#ffbe76", "#7ed6df", "#badc58")))
	
	// Option 3: Pass any number of colors in int format.
	.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.YELLOW,Color.CYAN,Color.MAGENTA)
	
	// Option 4: Use Predefined 15 colors array.
	.setColors()
	.setOnDirectSelectColorListener(new OnDirectSelectColorListener() {
                    @Override
                    public void onDirectColorSelected(int color, int position) {
                        
                    }
                })
	.show();
```
• You have options to choose from two types of listeners in case of dialogs. They are `OnSelectColorListener` and OnDirectSelectColorListener. First one works normally and is fired on pressing the buttons of dialog box. On setting the second one, i.e, `OnDirectSelectColorListener`, it will be fired as soon as any color is selected. If this is set in dialog then the positive and negative buttons would be hided (See the demo videos for more clear view on this).

• You can change tick color (by default it would be white) on all the items in color palette of dialogs. This library also supports to change the tick color for some particular color items. This can be achieved as below:
```java
ColorPickerBottomSheetDialog bottomSheetDialog = new ColorPickerBottomSheetDialog(getActivity());
        bottomSheetDialog.setColors()
	
	// Option 1: Set tick colors for all the items
	.setTickColor(Color.BLUE)
	
	// Option 2: Set tick color for some particular items. 
	.setTickColor(Color.YELLOW, Color.parseColor("#f44236"), Color.parseColor("#ff9700"), Color.parseColor("#4cb050"))
	.show();
```
• You can change shape of items in color palette. The library provides Square and Circle in-built. For other shapes, you can pass the drawable for that shape.
```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity());  // context
        colorPickerDialog.setColors()
	
	// Star would be used as a color item in palette. Any drawable is allowed.
        .setColorItemDrawable(R.drawable.ic_baseline_star_24)   
	
	.show();
```
• You can change the size of color item of palette. Also, the size of tick can be changed. This can be achieved as below:
```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity());  // context
        colorPickerDialog.setColors()
	
	// Set tick dimensions in dp (same for height and width). Default is 24dp. 
	.setTickDimenInDp(20)
	
	// Set the color item dimensions in dp (). Default is 45dp.
	.setColorItemDimenInDp(48)
	
	.show();
```
• ColorPicker Popup also suppports similar features. You can pass the default color (with or without alpha). You can remove the support of alpha from the dialog box. It uses different layout for landscape, so you don't have to care about the UI look in landscape.

**• Now, apart from these there are many other methods too. Some of the methods in which you are trying to get something from layout (for ex. `getPositiveButton()`), must be called after `show()` method. This is because these classes are wrapper over Android's AlertDialog class. So, some basic methods work similar to it and may throw `NullPointerException`.**

## Let us Know
It would be great if any of your apps uses this library. Please, do let me know about it on **mrudultora@gmail.com**. The list of the apps using this library would be updated soon. Your views, suggestions and feedbacks are welcomed. Also, feel free to open issues or contributing to this library.

## Find this library useful? :heart:
Support it by starring this repository and joining [stargazers](https://github.com/mrudultora/Colorpicker/stargazers) for this repository. :star: <br>
And __[follow](https://github.com/mrudultora)__ me for more such libraries! 🤩

# License
```xml
Copyright 2021 mrudultora (Mrudul Tora)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
