package com.icreo.perufolkradio.data.network;


import com.icreo.perufolkradio.data.network.responses.Response;
import com.icreo.perufolkradio.data.network.responses.Radio;
import com.icreo.perufolkradio.ui.feedback.Feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi {

    @POST("{path}")
    Call<Response> sendFeedback(@Path(value = "path", encoded = true) String path, @Body Feedback body);

    //reporting radio
    @GET("{path}")
    Call<Response> reportRadio(@Path(value = "path", encoded = true) String path);

    //radio information
    @GET("{path}")
    Call<Radio> getRadio(@Path(value = "path", encoded = true) String path);



}
