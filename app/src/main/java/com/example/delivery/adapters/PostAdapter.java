package com.example.delivery.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.CustomDialog;
import com.example.delivery.FirebaseID;
import com.example.delivery.Frag_map;
import com.example.delivery.PostAdapterClickListener;
import com.example.delivery.R;
import com.example.delivery.convertcom.convertloc;
import com.example.delivery.models.ApiData;
import com.example.delivery.models.Post;
import com.example.delivery.convertcom.convertcom;
import com.example.delivery.retrofit.ApiService;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> datas;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private OnItemClickListener mListener = null ;
    private PostAdapterClickListener postAdapterClickListener;
    private NaverMap naverMap;






    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    public PostAdapter(Context context,List<Post> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false));




    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.listId.setText(data.getListId());
        holder.company.setText(data.getCompany());
        holder.number.setText(data.getNumber());




        convertcom convertcom = new convertcom();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.tracker.delivery/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ApiData> call = service.getPosts(convertcom.convertcompany(data.getCompany()),data.getNumber());

        call.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(Call<ApiData> call, Response<ApiData> response) {
                if (response.isSuccessful()) {

                    ApiData apiData = response.body();

                        String deliverystate = apiData.getState().getText().toString();
//                        holder.state.setText(deliverystate);
                    if(deliverystate.equals("배송완료")){
                        holder.state.setText("");
                        holder.state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.complete,0,0,0);


                    }else if(deliverystate.equals("배달완료")) {
                        holder.state.setText("");
                        holder.state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.complete,0,0,0);

                    }else if(deliverystate.equals("배달중")){
                        holder.state.setText("배송중");

                    }else if(deliverystate.equals("배송중")){
                        holder.state.setText("배송중");

                    }

                }
            }

            @Override
            public void onFailure(Call<ApiData> call, Throwable t) {
                holder.state.setText("배송중");

            }
        });
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView listId;
        private TextView company;
        private TextView number;
        private TextView btn_x;
        private TextView state;
        private WebView webview;
        private Button btn_addmap,btn_delete;
        private String carrier;
        private String track_id;



        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title);
            listId = itemView.findViewById(R.id.listId);
            company = itemView.findViewById(R.id.item_post_company);
            number = itemView.findViewById(R.id.item_post_number);
            state = itemView.findViewById(R.id.state);


            btn_x = itemView.findViewById(R.id.btn_x);



            btn_x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStore.collection(FirebaseID.post).document(listId.getText().toString())
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
                    datas.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                    notifyDataSetChanged();

                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        String com = company.getText().toString();
                        String num = number.getText().toString();

                        convertcom convertcom = new convertcom();
                        String fcom = convertcom.convertcompany(com);

                        String url = "https://tracker.delivery/#/" + fcom + "/" + num;

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
