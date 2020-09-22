package com.wecoders.singleradiopro.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.onesignal.OneSignal;
import com.wecoders.singleradiopro.R;
import com.wecoders.singleradiopro.data.preferences.PrefManager;
import com.wecoders.singleradiopro.databinding.ActivityMainBinding;
import com.wecoders.singleradiopro.databinding.NavHeaderMainBinding;
import com.wecoders.singleradiopro.ui.about.AboutActivity;
import com.wecoders.singleradiopro.ui.feedback.FeedbackActivity;
import com.wecoders.singleradiopro.ui.radio.PlaybackStatus;
import com.wecoders.singleradiopro.util.AdsUtil;
import com.wecoders.singleradiopro.util.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SwitchCompat drawerSwitch;
    MainActivityViewModel model;
    ActivityMainBinding binding;
    boolean exitFlag = false;
    boolean minimizeFlag = false;
    public String SWITCH_KEY = "SWITCH_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        model = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setViewmodel(model);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdsUtil.loadBannerAd(this, binding.appBarMainLayout.adLayout);

        setSupportActionBar(binding.appBarMainLayout.toolbar);
        getSupportActionBar().setTitle("");



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMainLayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        View view = binding.navView.getHeaderView(0);
        NavHeaderMainBinding navHeaderMainBinding = NavHeaderMainBinding.bind(view);


        model.getReportResponseLiveData().observe(this, response -> {
            try {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {

            }

        });

        model.getRadioLiveData().observe(this, response -> {
            try {
                model.radio = response;
                binding.appBarMainLayout.setRadio(response);
                navHeaderMainBinding.setRadio(response);

            } catch (Exception ignored) {

            }

        });

        drawerSwitch = (SwitchCompat) binding.navView.getMenu().findItem(R.id.nav_notification).getActionView();
        drawerSwitch.setChecked(new PrefManager<Boolean>(this).get(SWITCH_KEY, true));

        drawerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                new PrefManager<Boolean>(this).set(SWITCH_KEY, true);
                OneSignal.setSubscription(true);
            }else{
                new PrefManager<Boolean>(this).set(SWITCH_KEY, false);
                OneSignal.setSubscription(false);
            }

        });


    }


    private void setNotificationStatus(){
        if (new PrefManager<Boolean>(this).get(SWITCH_KEY, true)) {
            Log.e("Notification" , "OFF" );
            new PrefManager<Boolean>(this).set(SWITCH_KEY, false);
        } else {
            Log.e("Notification" , "ON" );
            new PrefManager<Boolean>(this).set(SWITCH_KEY, true);
        }

        drawerSwitch.setChecked(new PrefManager<Boolean>(this).get(SWITCH_KEY, false));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_notification) {
            setNotificationStatus();
            return  false;
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(MainActivity.this, FeedbackActivity.class));

        } else if (id == R.id.nav_pp) {

            AppUtil.loadWebView(this, "https://codecanyon.net/user/we3coders/portfolio");


        } else if (id == R.id.nav_rate) {

            final String appPackageName = getPackageName();
            try {
                startActivity(new Intent(
                        Intent.ACTION_VIEW, Uri
                        .parse("market://details?id="
                                + appPackageName)));
            } catch (android.content.ActivityNotFoundException exception) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id="
                                + appPackageName)));
            }

        } else if (id == R.id.nav_share) {

            final String appPackageName = getPackageName();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download our app and listen to our radio station https://play.google.com/store/apps/details?id="
                    + appPackageName);
            startActivity(Intent.createChooser(shareIntent, "Share Via:"));

        } else if (id == R.id.nav_about) {

            startActivity(new Intent(MainActivity.this, AboutActivity.class));

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.bind();
    }


    @Override
    public void onBackPressed() {

        if (exitFlag) {
            exitFlag = false;
            finish();
        } else if (minimizeFlag) {
            minimizeFlag = false;
            moveTaskToBack(true);
        } else {

            AppUtil.showExitDialog(MainActivity.this, model.isPlaying(), new AppUtil.AlertDialogListener() {
                @Override
                public void onPositive() {
                    exitFlag = true;
                    onBackPressed();
                }

                @Override
                public void onCancel() {
                    minimizeFlag = true;
                    onBackPressed();
                }
            });
        }

    }

    @Subscribe
    public void onEvent(String status) {

        switch (status) {
            case PlaybackStatus.PLAYING:

                break;

            case PlaybackStatus.LOADING:

                // loading

                break;


            case PlaybackStatus.ERROR:

                Toast.makeText(this, "Error loading radio.", Toast.LENGTH_SHORT).show();
                break;

            case PlaybackStatus.PAUSED:
                break;

        }

        if (status.equals(PlaybackStatus.PLAYING)) {
            binding.appBarMainLayout.btnPlayStop.setText(R.string.stop);
            binding.appBarMainLayout.btnPlayStop.setIconResource(R.drawable.ic_stop);
            binding.appBarMainLayout.animationView.playAnimation();

        } else {
            binding.appBarMainLayout.btnPlayStop.setText(R.string.play);
            binding.appBarMainLayout.btnPlayStop.setIconResource(R.drawable.ic_play);
            binding.appBarMainLayout.animationView.pauseAnimation();
        }

        binding.appBarMainLayout.btnPlayStop.setIconGravity(MaterialButton.ICON_GRAVITY_END);


    }


}