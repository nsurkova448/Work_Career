package com.example.workcareer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null) {
                    Intent intent = new Intent(SplashActivity.this, NachatActivity.class );
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashActivity.this, HubActivity.class );
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_DELAY);
    }
}