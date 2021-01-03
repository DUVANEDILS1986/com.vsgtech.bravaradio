package com.wecoders.singleradiopro.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wecoders.singleradiopro.data.repositories.MainActivityRepository;

public class MainActivityFactory extends ViewModelProvider.NewInstanceFactory {

    MainActivityRepository mMainActivityRepository;
    Context context;

    public MainActivityFactory(MainActivityRepository mainActivityRepository, Context context) {
        mMainActivityRepository = mainActivityRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(context, mMainActivityRepository);
    }
}
