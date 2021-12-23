package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.net.URLEncoder;

public class WebviewActivity extends AppCompatActivity {
    WebView webview;
    WebSettings webSettings;
    private Intent intent;
    String company,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        intent = getIntent();
        company = intent.getStringExtra("com");
        number = intent.getStringExtra("num");

        Log.d("test2",company+number);
        webview = findViewById(R.id.wv);
        String url = "https://tracker.delivery/#/kr.cjlogistics/644001294780";
        webview.loadUrl(url); //연결할 url


        //intent.setData(Uri.parse(url));
        //startActivity(intent);




    }
}