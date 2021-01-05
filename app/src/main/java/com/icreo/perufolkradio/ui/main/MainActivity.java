package com.icreo.perufolkradio.ui.main;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.icreo.perufolkradio.data.repositories.MainActivityRepository;
import com.icreo.perufolkradio.ui.radio.MetadataListener;
import com.onesignal.OneSignal;
import com.icreo.perufolkradio.R;
import com.icreo.perufolkradio.data.preferences.PrefManager;
import com.icreo.perufolkradio.databinding.ActivityMainBinding;
import com.icreo.perufolkradio.databinding.NavHeaderMainBinding;
import com.icreo.perufolkradio.ui.about.AboutActivity;
import com.icreo.perufolkradio.ui.feedback.FeedbackActivity;
import com.icreo.perufolkradio.ui.radio.PlaybackStatus;
import com.icreo.perufolkradio.util.AdsUtil;
import com.icreo.perufolkradio.util.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MetadataListener {
    SwitchCompat drawerSwitch;
    MainActivityViewModel model;
    ActivityMainBinding binding;
    boolean exitFlag = false;
    boolean minimizeFlag = false;
    public String SWITCH_KEY = "SWITCH_KEY";
    private String privacyPolicyUrl;
    String oldTitle = "oldTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MainActivityRepository repository = new MainActivityRepository(this);
        MainFactory factory = new MainFactory(this,repository);
        model = new ViewModelProvider(this,factory).get(MainActivityViewModel.class);
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
                privacyPolicyUrl = response.getPrivacyPolicy();
                model.radio = response;
                binding.appBarMainLayout.setRadio(response);
                model.onPlayClicked(null);
                navHeaderMainBinding.setRadio(response);

            } catch (Exception ignored) {

            }

        });

        drawerSwitch = (SwitchCompat) binding.navView.getMenu().findItem(R.id.nav_notification).getActionView();
        drawerSwitch.setChecked(new PrefManager<Boolean>(this).get(SWITCH_KEY, true));

        drawerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                new PrefManager<Boolean>(this).set(SWITCH_KEY, true);
                OneSignal.setSubscription(true);
            } else {
                new PrefManager<Boolean>(this).set(SWITCH_KEY, false);
                OneSignal.setSubscription(false);
            }

        });


    }


    private void setNotificationStatus() {
        if (new PrefManager<Boolean>(this).get(SWITCH_KEY, true)) {
            Log.e("Notification", "OFF");
            new PrefManager<Boolean>(this).set(SWITCH_KEY, false);
        } else {
            Log.e("Notification", "ON");
            new PrefManager<Boolean>(this).set(SWITCH_KEY, true);
        }

        drawerSwitch.setChecked(new PrefManager<Boolean>(this).get(SWITCH_KEY, false));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_notification) {
            setNotificationStatus();
            return false;
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(MainActivity.this, FeedbackActivity.class));

        } else if (id == R.id.nav_pp) {

            if (privacyPolicyUrl != null)
                AppUtil.loadWebView(this, privacyPolicyUrl);


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

//        social
        else if (id == R.id.nav_facebook) {
            model.onFacebookClicked(this);
        } else if (id == R.id.nav_insta) {

            model.onInstagramClicked(this);

        } else if (id == R.id.nav_twitter) {

            model.onTwitterClicked(this);

        } else if (id == R.id.nav_whatsApp) {

            model.onWhatsAppClicked(this);

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
        model.bind(this);
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
            binding.appBarMainLayout.btnPlayStop.setImageResource(R.drawable.ic_stop);
            binding.appBarMainLayout.animationView.playAnimation();

        } else {
            binding.appBarMainLayout.btnPlayStop.setImageResource(R.drawable.ic_play);
            binding.appBarMainLayout.animationView.pauseAnimation();
        }


    }


    @Override
    public void onMetadataUpdated(String title, String albumArtUrl) {

        Log.e("Album art is:", albumArtUrl + "");

        /*if(!oldTitle.equalsIgnoreCase(title)){
            oldTitle = title;
            Log.e("artist is:", title);
            mainBinding.playerLayout.genreTextView.setText(title);
            String url;

            try {
                if (albumArtUrl.contains("http")) {
                    url = albumArtUrl.replace("\"", "").replace("\"", "");
                } else {
                    url = viewModel.radio.getValue().getImage();
                }


                Glide
                        .with(this)
                        .load(url)
                        .placeholder(R.drawable.placeholder)
                        .error(Glide.with(mainBinding.playerLayout.radioLogoImageView).load(viewModel.radio.getValue().getImage()))
                        .into(mainBinding.playerLayout.radioLogoImageView);

            } catch (Exception ignored) {
            }
        }*/


    }
}