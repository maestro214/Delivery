package com.example.delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity가 LoginActivity보다 앱이 시작될 때 로그인 상황에 따라 어느 화면으로 이동할지
 * 처리하기에 더 좋은 곳임.
 * Android 12는 모든 앱이 기본적으로 Splash screen을 가지도록 되어있기 때문에, Android 12의 경우는
 * Splash screen이 두개가 보임. @SuppressLint("CustomSplashScreen")가 붙어 있는 이유임.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startLoading();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMainScreen();
                finish();
            }
        }, 2000);
    }

    private void goToMainScreen() {
        Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}