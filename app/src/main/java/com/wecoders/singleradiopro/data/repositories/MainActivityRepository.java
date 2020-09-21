package com.wecoders.singleradiopro.data.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.wecoders.singleradiopro.data.network.MyApi;
import com.wecoders.singleradiopro.data.network.SafeApiRequest;
import com.wecoders.singleradiopro.data.network.SetupRetrofit;
import com.wecoders.singleradiopro.data.network.responses.Feedback;

public class MainActivityRepository extends SafeApiRequest {

    private MyApi api;
    Context context;

    public MainActivityRepository(Application application) {
        this.context = application.getApplicationContext();
        SetupRetrofit setupRetrofit = new SetupRetrofit(application.getApplicationContext());
        this.api = SetupRetrofit.createService(MyApi.class);

    }

    /**
     * increase view for individual radio station.
     */
    public MutableLiveData<Feedback> reportRadio() {
        return callRetrofitObjectResponse(context, api.reportRadio("streaming/issue/report/store"));
    }

}
