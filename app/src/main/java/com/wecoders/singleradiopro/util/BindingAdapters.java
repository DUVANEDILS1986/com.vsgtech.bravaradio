package com.wecoders.singleradiopro.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.wecoders.singleradiopro.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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


/*
    @BindingAdapter({"android:checked"})
    public static void checkSwitchStatus(SwitchCompat view, boolean isOnOff) {
        view.setChecked(new PrefManager<Boolean>(view.getContext()).get(SWITCH_KEY, true));
    }

*/

}
