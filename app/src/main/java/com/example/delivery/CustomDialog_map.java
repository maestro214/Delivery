package com.example.delivery;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.adapters.PostAdapter_map;
import com.example.delivery.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDialog_map extends Dialog {

    private Context context;
    private CustomDialogClickListener_map customDialogClickListener_map;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mPostRecyclerView;
    private PostAdapter_map mAdapter;
    private List<Post> mDatas;


    interface CustomDialogClickListener_map {
        void itemView(List<LatLng> pointlist);
    }

    public CustomDialog_map(@NonNull Context context) {
        super(context);
        this.context = context;
        this.customDialogClickListener_map = customDialogClickListener_map;
    }


    PostAdapter_map adapter_map = new PostAdapter_map(CustomDialog_map.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_map);

        mPostRecyclerView = findViewById(R.id.main_recyclerview_map);
        mDatas = new ArrayList<>();
        String id = mAuth.getCurrentUser().getUid();


        mStore.collection(FirebaseID.post).orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .whereEqualTo("documentId",id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String listId = String.valueOf(shot.get(FirebaseID.listId));
                                String company = String.valueOf(shot.get(FirebaseID.company));
                                String number = String.valueOf(shot.get(FirebaseID.number));
                                Post data = new Post(documentId, title, listId, company,number);
                                mDatas.add(data);

                            }
                            mAdapter = new PostAdapter_map(getContext(),mDatas);
                            //Log.d("mDatas",mDatas.toString());
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });



    }


}
