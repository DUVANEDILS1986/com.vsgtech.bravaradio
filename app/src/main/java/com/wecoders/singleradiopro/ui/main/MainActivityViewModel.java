package com.wecoders.singleradiopro.ui.main;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wecoders.singleradiopro.data.network.responses.Feedback;
import com.wecoders.singleradiopro.data.repositories.MainActivityRepository;
import com.wecoders.singleradiopro.util.AppUtil;

public class MainActivityViewModel  extends AndroidViewModel {

    private MutableLiveData<Feedback> reportResponseLiveData=new MutableLiveData<>();
    private MainActivityRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository =  new MainActivityRepository(application);
    }

    public String setGreetingText(){

        return "";
    }

    public String setDateText(){

        return "";
    }


    public void onFacebookClicked(View view){
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

    public void onInstagramClicked(View view){
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

    public void onTwitterClicked(View view){

        String twitter_user_name = "AndroTesteur";
        try {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
        }catch (Exception e) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
        }
    }

    public void onReportStreamClicked(View view){
        AppUtil.showReportDialog(view.getContext(), new AppUtil.AlertDialogListener() {
            @Override
            public void onPositive() {
                MutableLiveData<Feedback> flag= repository.reportRadio();
                flag.observe((LifecycleOwner) view.getContext(), feedback -> reportResponseLiveData.setValue(feedback));
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void onSetTimerClicked(View view){

    }

    public void onPlayClicked(View view){

    }

    public LiveData<Feedback> getReportResponseLiveData() {
        return reportResponseLiveData;
    }


}
