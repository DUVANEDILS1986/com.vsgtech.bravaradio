package com.wecoders.singleradiopro.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wecoders.singleradiopro.data.repositories.MainActivityRepository;

public class MainFactory extends ViewModelProvider.NewInstanceFactory {
    Context mContext;
    MainActivityRepository mRepository;

    public MainFactory(Context context, MainActivityRepository repository) {
        mContext = context;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mContext,mRepository);
    }
}
