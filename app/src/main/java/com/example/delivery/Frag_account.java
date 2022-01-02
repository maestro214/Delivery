package com.example.delivery;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class Frag_account extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private String zizital;
    Button logout, delete;
    TextView my_nickname, my_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acoount_frag, container, false);


        my_nickname = view.findViewById(R.id.my_nickname);
        my_email = view.findViewById(R.id.my_email);

        logout = view.findViewById(R.id.btn_logout);
        delete = view.findViewById(R.id.btn_delete);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id2 = user.getUid();
        if (user != null) {
            mStore.collection("user").whereEqualTo("documentId",mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> str = document.getData();
                                    String nickname = String.valueOf(str.get(FirebaseID.nickname));
                                    my_nickname.setText(nickname);
                                    Log.d("idid",nickname);




                            String email = user.getEmail();


                            my_email.setText(email);

                        }
                    } else {
                        // No user is signed in
                    }
                }
            });



            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    intent.putExtra("re", "true");
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(),"회원탈퇴 성공",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    CollectionReference productRef = mStore.collection("user");
                    productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                               @Override
                                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                   if (task.isSuccessful()) {
                                                                       for (QueryDocumentSnapshot document : task.getResult()) {
                                                                           Map<String, Object> str = document.getData();
                                                                           String documentid = String.valueOf(str.get(FirebaseID.documentId));
                                                                            Log.d("id",documentid);
                                                                           mStore.collection("user").document(documentid)
                                                                                   .delete()
                                                                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                       @Override
                                                                                       public void onSuccess(Void aVoid) {


                                                                                           Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                                                                           intent.putExtra("re", "true");
                                                                                           intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                           startActivity(intent);

                                                                                       }
                                                                                   })
                                                                                   .addOnFailureListener(new OnFailureListener() {
                                                                                       @Override
                                                                                       public void onFailure(@NonNull Exception e) {
                                                                                           Toast.makeText(getContext(),"회원탈퇴 실패",Toast.LENGTH_SHORT).show();
                                                                                       }
                                                                                   });
                                                                       }
                                                                   } else {
                                                                       // No user is signed in
                                                                   }
                                                               }
                                                           });



                }
            });



        }return view;
    }
}
