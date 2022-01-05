package com.example.delivery;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.delivery.adapters.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomDialog  extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView mEdit_title, tvNegative, tvPositive,mEdit_number;
    private Spinner spinner_company;

    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();


    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        tvPositive = findViewById(R.id.option_codetype_dialog_positive);
        tvNegative = findViewById(R.id.option_codetype_dialog_negative);
        mEdit_title = findViewById(R.id.edit_title);
        spinner_company = findViewById(R.id.spinner_company);
        mEdit_number = findViewById(R.id.edit_number);




        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postId = mStore.collection(FirebaseID.post).document().getId();
                Map<String, Object> data = new HashMap<>();

                data.put(FirebaseID.listId,postId);
                data.put(FirebaseID.documentId,mAuth.getCurrentUser().getUid());
                data.put(FirebaseID.title, mEdit_title.getText().toString());
                data.put(FirebaseID.company,spinner_company.getSelectedItem().toString());
                data.put(FirebaseID.number, mEdit_number.getText().toString());
                data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());

                dismiss();
            }
        });

        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
