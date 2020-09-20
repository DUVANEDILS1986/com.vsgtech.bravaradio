package com.wecoders.singleradiopro.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.wecoders.singleradiopro.R;
import com.wecoders.singleradiopro.databinding.ActivityMainBinding;
import com.wecoders.singleradiopro.util.AdsUtil;
import com.wecoders.singleradiopro.util.AppUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivityViewModel model = new ViewModelProvider(this).get(MainActivityViewModel.class);
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
        //onNavigationItemSelected(binding.navView.getMenu().getItem(0)); //select home fragment by default


        model.getReportResponseLiveData().observe(this, response -> {
            try {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {

            }

        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_notification) {


        } else if (id == R.id.nav_feedback) {


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


        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}