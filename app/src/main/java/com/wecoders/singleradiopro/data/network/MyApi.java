package com.wecoders.singleradiopro.data.network;


import com.wecoders.singleradiopro.data.network.responses.Feedback;
import com.wecoders.singleradiopro.data.network.responses.FeedbackBody;
import com.wecoders.singleradiopro.data.network.responses.Radio;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi {

    @POST("{path}")
    Call<Feedback> sendFeedback(@Path(value = "path", encoded = true) String path, @Body FeedbackBody body);

    //reporting radio
    @GET("{path}")
    Call<Feedback> reportRadio(@Path(value = "path", encoded = true) String path);

    //radio information
    @GET("{path}")
    Call<Radio> getRadio(@Path(value = "path", encoded = true) String path);

}
