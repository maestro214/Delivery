package com.example.delivery;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Fragment fa, fb, fc;

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

        // 프레그먼트를 관리하는 로직에 상당히 많은 버그가 존재함.
        // Jetpack Navigation Component를 이미 라이브러리에 추가했으므로, 이걸 사용하거나
        // 아니면, Fragment의 동작원리를 철저히 공부해서 코드를 넣어야 함.
        // 네비게이션이 안드로이드에서 제일 난이도가 높은 부분 중의 하나임. 특히나 Fragment를 show/hide하는 방식은
        // Lifecycle에 대한 처리에 대해 좀 더 신경을 써줘야 함.
        // 골치가 덜 아프게, Fragment의 원리를 이해한 후 Navigation Component를 사용하기를 권장 함.
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fa).commit();

        // TODO : 네이게이션 로직을 뷰에서 분리하여 별도의 클래스에서 처리하도록 처리.
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
                            fb = new PostOnMapFragment();
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
                            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fc).commit();
                        }
                        if (fa != null)
                            getSupportFragmentManager().beginTransaction().hide(fa).commit();
                        if (fb != null)
                            getSupportFragmentManager().beginTransaction().hide(fb).commit();
                        if (fc != null)
                            getSupportFragmentManager().beginTransaction().show(fc).commit();
                        break;

                }
                return true;
            }
        });

    }
}






