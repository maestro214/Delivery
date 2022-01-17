package com.example.delivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.adapters.model.BasePostItem;
import com.example.delivery.adapters.model.PostItem;
import com.example.delivery.mappers.CarrierIdMapper;
import com.example.delivery.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

        mAdapter = new PostAdapter();
        mAdapter.setListener(new PostAdapter.Listener() {
            @Override
            public void onPostClicked(BasePostItem post) {
                openPostDetails(post);
            }

            @Override
            public void onRemovePostClicked(BasePostItem post) {
                removeSelectedPost(post);
            }
        });
        mPostRecyclerView = view.findViewById(R.id.main_recyclerview);
        mPostRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog octDialog = new CustomDialog(requireContext(), null);
                octDialog.setCanceledOnTouchOutside(true);
                octDialog.setCancelable(true);
                octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                octDialog.show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatas = new ArrayList<>();
        String id = mAuth.getCurrentUser().getUid();

        mStore.collection(FirebaseID.post).orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .whereEqualTo("documentId", id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Post post = snap.toObject(Post.class);
                                mDatas.add(post);

                                // TODO : Delivery의 진행상태를 가져와서 Post에 업데이트 할 것. 개별적으로 업데이트할 수도 있고, 진행상태의 collection을 사용자에 맞게 필터한 다음,
                                // posts와 진행상태들을 merge할 수도 있을 것임.
                                // Refer. https://firebase.google.com/docs/firestore/query-data/queries
                            }

                            submitList();
                        }
                    }
                });
    }

    // 이상하게 보일지 모르겠지만, 이게 더 좋은 접근방법.
    // 서버에서 사용되는 데이터의 구조는 아주 흔하게, 화면에서 요구되는 구조와 다르다. 따라서
    // 화면에 필요한 데이터는 별도로 만들어 사용해야 한다.
    private void submitList() {
        mAdapter.submitList(PostItem.toUiItems(mDatas));
    }

    private void removeSelectedPost(BasePostItem post) {
        mStore.collection(FirebaseID.post).document(post.getListId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("stat", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("stat", "Error deleting document", e);
                    }
                });

        int indexToRemove = indexToRemove(post);
        if (indexToRemove < 0) {
            throw new NoSuchElementException("##### 선택한 아이템이 존재하지 않습니다. listId : " + post.getListId());
        }

        // TODO : 삭제된 데이터를 실제 저장소에 반영.
        mDatas.remove(indexToRemove);
        mAdapter.submitList(PostItem.toUiItems(mDatas), indexToRemove);
    }

    private int indexToRemove(BasePostItem postItem) {
        int result = -1;
        for (Post item : mDatas) {
            result++;
            if (item.getListId().equals(postItem.getListId())) {
                return result;
            }
        }

        return result;
    }

    private void openPostDetails(BasePostItem post) {
        String fcom = CarrierIdMapper.mapBy(post.getCompany());

        String url = "https://tracker.delivery/#/" + fcom + "/" + post.getNumber();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}



