package com.wecoders.singleradiopro.util;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.wecoders.singleradiopro.R;


public class BindingAdapters {


    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        try {
            Glide
                    .with(view.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.ic_radio_icon)
                    .into(view);
        } catch (Exception ignored) {
        }
    }


}
