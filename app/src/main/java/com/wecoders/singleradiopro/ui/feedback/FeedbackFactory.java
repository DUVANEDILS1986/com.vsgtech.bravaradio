package com.wecoders.singleradiopro.ui.feedback;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wecoders.singleradiopro.data.repositories.MainActivityRepository;

public class FeedbackFactory extends ViewModelProvider.NewInstanceFactory {
    MainActivityRepository mRepository;

    public FeedbackFactory(MainActivityRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FeedbackViewModel(mRepository);
    }
}
