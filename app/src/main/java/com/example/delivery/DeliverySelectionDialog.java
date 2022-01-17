package com.example.delivery;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.adapters.model.BasePostItem;
import com.example.delivery.adapters.model.PostOnMapItem;
import com.example.delivery.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliverySelectionDialog extends Dialog {

    public interface Listener {
        void onPostSelected(BasePostItem post);
    }

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mPostRecyclerView;
    private PostAdapter mAdapter;
    private final Listener mListener;
    private final List<Post> mPosts = new ArrayList<>();

    public DeliverySelectionDialog(@NonNull Context context, Listener listener) {
        super(context);
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_map);

        mAdapter = new PostAdapter();
        mAdapter.setListener(new PostAdapter.Listener() {
            @Override
            public void onPostClicked(BasePostItem post) {
                dismiss();
                mListener.onPostSelected(post);
            }

            @Override
            public void onRemovePostClicked(BasePostItem post) {

            }
        });
        mPostRecyclerView = findViewById(R.id.main_recyclerview_map);
        mPostRecyclerView.setAdapter(mAdapter);

        String uId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        fetchPostsByUId(uId);
    }

    // TODO : 데이터를 가져오는 로직을 UseCase로 옮기고 DeliverySelectionDialog를 호출하는 쪽에서는 UseCase를 사용한다.
    // 호출하는 쪽에서 데이터를 가져와서 그 결과를 DeliverySelectionDialog에 전달한다.
    private void fetchPostsByUId(String uId) {
        mStore.collection(FirebaseID.post).orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .whereEqualTo(FirebaseID.documentId, uId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots == null) return;

                        mPosts.clear();
                        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                            Post post = snap.toObject(Post.class);
                            mPosts.add(post);
                        }

                        submitList();
                    }
                });
    }

    private void submitList() {
        mAdapter.submitList(PostOnMapItem.toUiItems(mPosts));
    }
}
