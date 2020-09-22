package com.wecoders.singleradiopro.data.network;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;


import com.wecoders.singleradiopro.util.NoConnectivityDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeApiRequest {

    public static final String NO_CONNECTIVITY_DIALOG = "no_connectivity_dialog";


    public static <T> MutableLiveData<T> callRetrofitObjectResponse(Context context, Call<T> call) {
        MutableLiveData<T> responseObject = new MutableLiveData<>();

        Log.e("inside", call.request().url() + "------");

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                responseObject.setValue(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                responseObject.setValue(null);
                showDialog(context, t);
            }
        });

        return responseObject;
    }

    private static void showDialog(Context context, Throwable t) {
        if (t instanceof NoConnectivityException) {
            NoConnectivityDialog dialog = NoConnectivityDialog.newInstance();
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(), NO_CONNECTIVITY_DIALOG);
        }
    }
}


