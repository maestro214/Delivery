package com.example.delivery;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.models.Post;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Frag_list extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mPostRecyclerView;

    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_frag, container, false);

        mPostRecyclerView = view.findViewById(R.id.main_recyclerview);

       view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog octDialog = new CustomDialog(getContext());

                octDialog.setCanceledOnTouchOutside(true);
                octDialog.setCancelable(true);
                octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                octDialog.show();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();


        CollectionReference productRef = mStore.collection("post");
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                   @Override
                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                       for(QueryDocumentSnapshot document : task.getResult()) {
                                                           Map<String, Object> str = document.getData();
                                                           String id = String.valueOf(str.get(FirebaseID.documentId));
                                                           FirebaseUser user = mAuth.getCurrentUser();
                                                           String id2 = user.getUid();


                                                                mStore.collection(FirebaseID.post).whereEqualTo("documentId",id2)
                                                                        .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                                                                if (queryDocumentSnapshots != null) {
                                                                                    mDatas.clear();
                                                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                                                                        Map<String, Object> shot = snap.getData();
                                                                                        String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                                                                        String title = String.valueOf(shot.get(FirebaseID.title));
                                                                                        String company = String.valueOf(shot.get(FirebaseID.company));
                                                                                        String number = String.valueOf(shot.get(FirebaseID.number));
                                                                                        Post data = new Post(documentId, title, company,number);
                                                                                        mDatas.add(data);
                                                                                    }
                                                                                    mAdapter = new PostAdapter(mDatas);
                                                                                    //Log.d("mDatas",mDatas.toString());
                                                                                    mPostRecyclerView.setAdapter(mAdapter);
                                                                                }
                                                                            }

                                                                        });

                                                           Log.d("id2",id+"와"+id2);

                                                       }

                                                       }
                                               });


    }
}
