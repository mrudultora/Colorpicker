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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudultora.colorpicker.listeners.OnColorItemClickListener;
import com.mrudultora.colorpicker.util.ColorItemShape;
import com.mrudultora.colorpicker.util.ColorUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 * @since 1 May, 2021
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    final ArrayList<ColorPaletteItemModel> colorsList;
    private HashMap<Integer, Integer> colorItems;
    private final Context context;
    private int colorPosition = -1;              // would be used if none of the color is selected and there is an default color.
    private ColorItemShape colorItemShape;       // default is square.
    private int drawableRes = -1;
    private int tickMarkColor = -1;              // the default color is white.
    private final OnColorItemClickListener onColorItemClickListener;
    private boolean cardSizeChanged = false;
    private boolean tickSizeChanged = false;
    private boolean multiTickColor = false;
    private float tickSizeDimen = 0f;                   // when equals 0 (default used would be 24dp)
    private float cardViewDimen = 0f;                 // when equals 0 (default used would be 45dp)

    public ColorAdapter(ArrayList<ColorPaletteItemModel> colorsList,
                        Context context,
                        ColorItemShape colorItemShape,
                        OnColorItemClickListener onColorItemClickListener) {
        this.colorsList = colorsList;
        this.context = context;
        this.colorItemShape = colorItemShape;
        this.onColorItemClickListener = onColorItemClickListener;
    }

    public ColorAdapter(ArrayList<ColorPaletteItemModel> colorsList,
                        Context context,
                        int drawableRes,
                        OnColorItemClickListener onColorItemClickListener) {
        this.colorsList = colorsList;
        this.context = context;
        this.drawableRes = drawableRes;
        this.onColorItemClickListener = onColorItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_color_item, parent, false);
        return new ViewHolder(view, onColorItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemCardView.setCardBackgroundColor(colorsList.get(position).getColor());

        if (colorsList.get(position).isCheck()) {
            holder.itemCheckImageView.setVisibility(View.VISIBLE);
        } else {
            holder.itemCheckImageView.setVisibility(View.GONE);
        }

        if (tickMarkColor != -1 && !multiTickColor) {
            ImageViewCompat.setImageTintList(holder.itemCheckImageView, ColorStateList.valueOf(tickMarkColor));
        }

        if (tickSizeChanged) {
            holder.itemCheckImageView.getLayoutParams().height = ColorUtil.dpToPixel(context, tickSizeDimen);
            holder.itemCheckImageView.getLayoutParams().width = ColorUtil.dpToPixel(context, tickSizeDimen);
        }

        if (cardSizeChanged) {
            holder.itemCardView.getLayoutParams().height = ColorUtil.dpToPixel(context, cardViewDimen);
            holder.itemCardView.getLayoutParams().width = ColorUtil.dpToPixel(context, cardViewDimen);
            holder.itemCheckImageView.getLayoutParams().width = ColorUtil.dpToPixel(context, cardViewDimen / 2);
            holder.itemCheckImageView.getLayoutParams().height = ColorUtil.dpToPixel(context, cardViewDimen / 2);
        }

        if (colorItems != null && multiTickColor) {
            if (colorItems.containsKey(colorsList.get(position).getColor())) {
                ImageViewCompat.setImageTintList(holder.itemCheckImageView, ColorStateList.valueOf(tickMarkColor));
            } else {
                ImageViewCompat.setImageTintList(holder.itemCheckImageView, ColorStateList.valueOf(Color.WHITE));
            }
        }

        if (colorItemShape == ColorItemShape.CIRCLE) {
            if (cardSizeChanged) {
                holder.itemCardView.setRadius(ColorUtil.dpToPixel(context, cardViewDimen / 2));
            } else {
                holder.itemCardView.setRadius(ColorUtil.dpToPixel(context, 22.5f));
            }
        }

        if (drawableRes != -1) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
            if (drawable == null)
                return;
            DrawableCompat.setTint(DrawableCompat.wrap(drawable).mutate(), colorsList.get(position).getColor());
            holder.itemCardView.setBackground(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView itemCardView;
        AppCompatImageView itemCheckImageView;
        OnColorItemClickListener colorItemClickListener;

        public ViewHolder(@NonNull View itemView, OnColorItemClickListener colorItemClickListener) {
            super(itemView);
            itemCardView = itemView.findViewById(R.id.itemCardView);
            itemCheckImageView = itemView.findViewById(R.id.itemCheckImageView);
            itemCardView.setOnClickListener(this);
            this.colorItemClickListener = colorItemClickListener;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (colorPosition != -1 && colorPosition != position) {
                colorsList.get(colorPosition).setCheck(false);
                notifyItemChanged(colorPosition);
            }
            colorsList.get(position).setCheck(true);
            notifyItemChanged(position);
            colorItemClickListener.onColorItemClick(position);
            colorPosition = position;
        }
    }

    /**
     * Sets the default color from the list of color.
     * If the color passed is not present in the list, then none of the colors would be selected.
     *
     * @param defaultColor (default color)
     */
    public void setDefaultColor(int defaultColor) {
        for (int i = 0; i < colorsList.size(); i++) {
            if (colorsList.get(i).getColor() == defaultColor) {
                colorsList.get(i).setCheck(true);
                colorPosition = i;
                notifyItemChanged(i);
                break;
            }
        }
    }

    public int getColorPosition() {
        return colorPosition;
    }

    /**
     * Sets the color of tick mark in default color
     *
     * @param tickMarkColor (tick color)
     */
    public void setTickMarkColor(int tickMarkColor) {
        this.tickMarkColor = tickMarkColor;
        notifyDataSetChanged();
    }

    public void customCardSize(float dimen) {
        cardSizeChanged = true;
        cardViewDimen = dimen;
        notifyDataSetChanged();
    }

    public void customTickSize(float dimen) {
        tickSizeChanged = true;
        tickSizeDimen = dimen;
        notifyDataSetChanged();
    }

    public void customTickMarkColorForSomeColors(int tickMarkColor, HashMap<Integer, Integer> map) {
        this.colorItems = map;
        this.multiTickColor = true;
        this.tickMarkColor = tickMarkColor;
        notifyDataSetChanged();
    }
}
