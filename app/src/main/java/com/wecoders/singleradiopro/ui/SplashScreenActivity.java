package com.wecoders.singleradiopro.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.wecoders.singleradiopro.R;
import com.wecoders.singleradiopro.data.preferences.PrefManager;
import com.wecoders.singleradiopro.ui.main.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String DARK_MODE_KEY = "DARK_MODE_KEY";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            String version = pInfo.versionName;
            TextView textView = findViewById(R.id.versionTextView);
            textView.setText("V " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (new PrefManager<Boolean>(this).get(DARK_MODE_KEY, true))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);

    }
}