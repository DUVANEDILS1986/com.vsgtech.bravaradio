package com.wecoders.singleradiopro.data.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.wecoders.singleradiopro.data.network.MyApi;
import com.wecoders.singleradiopro.data.network.SafeApiRequest;
import com.wecoders.singleradiopro.data.network.SetupRetrofit;
import com.wecoders.singleradiopro.data.network.responses.Response;
import com.wecoders.singleradiopro.data.network.responses.Radio;
import com.wecoders.singleradiopro.ui.feedback.Feedback;

public class MainActivityRepository extends SafeApiRequest {

    private MyApi api;
    Context context;

    public MainActivityRepository(Context context) {
        this.context = context;
        SetupRetrofit setupRetrofit = new SetupRetrofit(context);
        this.api = SetupRetrofit.createService(MyApi.class);
    }

    /**
     * increase view for individual radio station.
     */
    public MutableLiveData<Response> reportRadio() {
        return callRetrofitObjectResponse(context, api.reportRadio("streaming/issue/report/store"));
    }

    /**
     * send feedback.
     */
    public MutableLiveData<Response> sendFeedback(Feedback feedback) {
        return callRetrofitObjectResponse(context, api.sendFeedback("feedback/store",feedback));
    }

    /**
     * get radio information
     */
    public MutableLiveData<Radio> getRadio() {
        return callRetrofitObjectResponse(context, api.getRadio("radio"));
    }
}
