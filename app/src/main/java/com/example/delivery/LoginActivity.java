package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private EditText mEmail;
    private EditText mPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);

        findViewById(R.id.login_signup).setOnClickListener(this);
        findViewById(R.id.login_success).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String text = intent.getStringExtra("re");
        if(text!=null) {
       //     Toast.makeText(this,"로그아웃 성공",Toast.LENGTH_SHORT).show();
        }else {
        FirebaseUser user = mAuth.getCurrentUser();



        mStore.collection("user").whereEqualTo("documentId",user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> str = document.getData();
                        String nickname = String.valueOf(str.get(FirebaseID.nickname));
                        System.out.println(nickname);
                        if(user!= null) {
                            Toast.makeText(LoginActivity.this, "자동 로그인 :  " + nickname+"님 환영합니다", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    }
                }

            }
        }



        });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_signup:
                startActivity(new Intent(this,SignupActivity.class));
                break;

            case R.id.login_success:
                mAuth.signInWithEmailAndPassword(mEmail.getText().toString(),mPassword.getText().toString())
                        .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                if(task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null) {
                                        Toast.makeText(LoginActivity.this, "로그인 성공:" + user.getUid(), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                                    }
                                }else{
                                    Toast.makeText(LoginActivity.this,"Login error.",Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

                break;
        }

    }
}