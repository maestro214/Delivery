package com.example.delivery.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.convertcom.convertcom;
import com.example.delivery.convertcom.convertloc;
import com.example.delivery.models.ApiData;
import com.example.delivery.models.Post;
import com.example.delivery.retrofit.ApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.geometry.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostOnMapAdapter extends RecyclerView.Adapter<PostOnMapAdapter.PostViewHolder> {

    private final List<Post> mPosts;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    public ItemClickListener mListener;

    public interface ItemClickListener {
        void onPostOnMapClicked(Post post);
    }

    public PostOnMapAdapter(List<Post> posts, ItemClickListener listener) {
        this.mPosts = posts;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_map, parent, false);
        return new PostViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = mPosts.get(position);
        holder.bind(post);

        holder.title.setText(post.getTitle());
        holder.listId.setText(post.getListId());
        holder.company.setText(post.getCompany());
        holder.number.setText(post.getNumber());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView listId;
        private final TextView company;
        private final TextView number;

        private final ItemClickListener mListener;

        private Context getContext() {
            return itemView.getContext();
        }

        public PostViewHolder(@NonNull View itemView, ItemClickListener listener) {
            super(itemView);
            this.mListener = listener;

            title = itemView.findViewById(R.id.item_post_title_map);
            listId = itemView.findViewById(R.id.listId_map);
            company = itemView.findViewById(R.id.item_post_company_map);
            number = itemView.findViewById(R.id.item_post_number_map);
        }

        public void bind(Post post) {
            title.setText(post.getTitle());
            listId.setText(post.getListId());
            company.setText(post.getCompany());
            number.setText(post.getNumber());

            itemView.setOnClickListener(v -> {
                mListener.onPostOnMapClicked(post);
            });
        }

        /*private void postItemClicked(Post post) {
            String carrier = post.getCompany(); //company.getText().toString();
            String trackId = post.getNumber(); //number.getText().toString();


            convertcom convertcom = new convertcom();
            String carrier_id = convertcom.convertcompany(carrier);

            List<LatLng> pointList = new ArrayList<>();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://apis.tracker.delivery/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService service = retrofit.create(ApiService.class);

            Call<ApiData> call = service.getPosts(carrier_id, trackId);

            call.enqueue(new Callback<ApiData>() {
                @Override
                public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                    if (response.isSuccessful()) {

                        ApiData apiData = response.body();
                        assert apiData != null;
                        int test = apiData.getProgresses().size();

                        for (int i = 0; i < test; i++) {


                            String locationname = apiData.getProgresses().get(i).getLocation().toString();

                            convertloc convertloc = new convertloc();
                            locationname = convertloc.convertlocation(locationname);

                            Geocoder geocoder = new Geocoder(getContext().getApplicationContext());
                            List<Address> list = null;

                            LatLng xy = null;
                            try {
                                list = geocoder.getFromLocationName(locationname, 10);
                                double x = list.get(0).getLatitude();
                                double y = list.get(0).getLongitude();
                                xy = new LatLng(x, y);

                                pointList.add(xy);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println(pointList.toString());
                        //Frag_map frag_map = new Frag_map(listId.getContext(), pointlist);
                        String ee = "hi";
                        mListener.onPostOnMapClicked(ee);
                    } else {
                        Log.d("test", "실패");
                    }
                }

                @Override
                public void onFailure(Call<ApiData> call, Throwable t) {
                    Log.d("test", "onresponse: 실패 ");
                }
            });
        }*/
    }
}
