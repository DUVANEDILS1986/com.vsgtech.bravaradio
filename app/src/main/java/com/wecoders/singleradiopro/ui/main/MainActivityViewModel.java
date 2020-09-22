package com.wecoders.singleradiopro.ui.main;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wecoders.singleradiopro.data.network.responses.Response;
import com.wecoders.singleradiopro.data.network.responses.Radio;
import com.wecoders.singleradiopro.data.repositories.MainActivityRepository;
import com.wecoders.singleradiopro.ui.player.TimerDialog;
import com.wecoders.singleradiopro.ui.radio.RadioManager;
import com.wecoders.singleradiopro.util.AppUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivityViewModel extends AndroidViewModel {


    private RadioManager radioManager;
    private MutableLiveData<Response> reportResponseLiveData = new MutableLiveData<>();
    public MutableLiveData<Radio> radioObjectLiveData;
    private MainActivityRepository repository;
    private MutableLiveData<String> timerText = new MutableLiveData<>();
    public Radio radio;
    private boolean isTimerSet = false;
    Handler handler = new Handler();
    Runnable r = () -> {
        isTimerSet = false;
        timerText.setValue("none");
        stopPlayer();
    };

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        radioManager = RadioManager.with(application.getApplicationContext());
        this.repository = new MainActivityRepository(application);
        radioObjectLiveData = repository.getRadio();
    }

    public String setGreetingText() {

        Calendar cal = Calendar.getInstance();
        int calendarHour = cal.get(Calendar.HOUR_OF_DAY);

        if(calendarHour >= 12 && calendarHour<=16){
           return "Good Afternoon,";
        }else if(calendarHour >= 17 && calendarHour<=20){
            return "Good Evening,";
        }else if(calendarHour >= 21 && calendarHour<=23){
            return "Good Night,";
        }else{
            return "Good Morning,";
        }

    }

    public String setDateText() {
        return  new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());
    }


    public void onFacebookClicked(View view) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(view.getContext());
        facebookIntent.setData(Uri.parse(facebookUrl));
        view.getContext().startActivity(facebookIntent);
    }

    //method to get the right URL to use in the intent
    public static String getFacebookPageURL(Context context) {
        String FB_PAGE_USERNAME = "w3cod3rs";
        String FACEBOOK_PAGE_ID = "106864031155552";
        String FACEBOOK_URL = "https://m.facebook.com/" + FB_PAGE_USERNAME;

        PackageManager packageManager = context.getPackageManager();
        try {
            boolean activated = packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if (activated) {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } else {
                return FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public void onInstagramClicked(View view) {
        String instagramUser = "because.nepal";
        Uri uri = Uri.parse("http://instagram.com/_u/" + instagramUser);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            view.getContext().startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagramUser)));
        }
    }

    public void onTwitterClicked(View view) {

        String twitter_user_name = "AndroTesteur";
        try {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
        } catch (Exception e) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
        }
    }

    public void onReportStreamClicked(View view) {
        AppUtil.showReportDialog(view.getContext(), new AppUtil.AlertDialogListener() {
            @Override
            public void onPositive() {
                MutableLiveData<Response> flag = repository.reportRadio();
                flag.observe((LifecycleOwner) view.getContext(), response -> reportResponseLiveData.setValue(response));
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void onSetTimerClicked(View view) {
        TimerDialog timerDialog = new TimerDialog(view.getContext(), (time, timeText) -> {
            timerText.setValue(timeText);
            if (time == 0) {
                if (isTimerSet) {
                    isTimerSet = false;
                    handler.removeCallbacksAndMessages(null);

                }
            } else {
                if (isTimerSet) {
                    handler.removeCallbacksAndMessages(null);
                }
                isTimerSet = true;
                handler.postDelayed(r, time);
            }

        });
        timerDialog.show();
    }

    public void onPlayClicked(View view) {

        if (radio != null && radioManager!= null) {
            radioManager.playOrPause(radio);
        }

    }

    public LiveData<Response> getReportResponseLiveData() {
        return reportResponseLiveData;
    }

    public LiveData<Radio> getRadioLiveData() {
        return radioObjectLiveData;
    }

    public MutableLiveData<String> getTimerText() {
        return timerText;
    }


    public boolean isPlaying() {
        return radioManager.isPlaying();
    }

    public void bind() {
        radioManager.bind();
    }

    public void unbind() {
        radioManager.unbind();
    }

    public void stopPlayer() {
        radioManager.stopPlayer();
    }

}
