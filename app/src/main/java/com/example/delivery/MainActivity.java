package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class MainActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;

    private Fragment fa,fb,fc;

    /*
    private Frag_home frag_home;
    private Frag_town frag_town;
    private Frag_near frag_near;
    private Frag_chat frag_chat;
    private Frag_account frag_account;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        fa = new Frag_list();

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fa).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override


            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_list:
                        if (fa == null) {
                            fa = new Frag_list();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fa).commit();
                        }
                        if (fa != null)
                            getSupportFragmentManager().beginTransaction().show(fa).commit();
                        if (fb != null)
                            getSupportFragmentManager().beginTransaction().hide(fb).commit();
                        if (fc != null)
                            getSupportFragmentManager().beginTransaction().hide(fc).commit();
                        break;

                    case R.id.action_map:
                        if (fb == null) {
                            fb = new Frag_map();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fb).commit();
                        }
                        if (fa != null)
                            getSupportFragmentManager().beginTransaction().hide(fa).commit();
                        if (fb != null)
                            getSupportFragmentManager().beginTransaction().show(fb).commit();
                        if (fc != null)
                            getSupportFragmentManager().beginTransaction().hide(fc).commit();
                        break;


                    case R.id.action_acoount:
                        if (fc == null) {
                            fc = new Frag_account();
                            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fc).commit();
                        }
                        if(fa != null) getSupportFragmentManager().beginTransaction().hide(fa).commit();
                        if(fb != null) getSupportFragmentManager().beginTransaction().hide(fb).commit();
                        if(fc != null) getSupportFragmentManager().beginTransaction().show(fc).commit();
                        break;

                }
                return true;
            }
        });


        }
    }






