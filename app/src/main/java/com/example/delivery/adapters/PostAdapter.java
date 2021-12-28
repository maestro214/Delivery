package com.example.delivery.adapters;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.convertcom.convertloc;
import com.example.delivery.models.ApiData;
import com.example.delivery.models.Post;
import com.example.delivery.convertcom.convertcom;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> datas;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();




    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }




    public PostAdapter(List<Post> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.company.setText(data.getCompany());
        holder.number.setText(data.getNumber());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView company;
        private TextView number;
        private WebView webview;
        private Button btn_addmap,btn_delete;
        private String carrier;
        private String track_id;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title);
            company = itemView.findViewById(R.id.item_post_company);
            number = itemView.findViewById(R.id.item_post_number);
            btn_addmap = itemView.findViewById(R.id.btn_addmap);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            btn_addmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    carrier = company.getText().toString();
                    track_id = number.getText().toString();
                    convertcom convertcom = new convertcom();
                    String carrier_id = convertcom.convertcompany(carrier);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://apis.tracker.delivery/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ApiService service = retrofit.create(ApiService.class);

                    Call<ApiData> call = service.getPosts(carrier_id,track_id);

                    call.enqueue(new Callback<ApiData>() {
                        @Override
                        public void onResponse(Call<ApiData> call, Response<ApiData> response) {
                            if(response.isSuccessful()){

                                ApiData apiData = response.body();
                                int test = apiData.getProgresses().size();

                                for(int i = 0; i < test; i++) {

                                  //  ArrayList locationname = new ArrayList();
                                    String locationname = apiData.getProgresses().get(i).getLocation().toString();

                                    convertloc convertloc = new convertloc();
                                    locationname = convertloc.convertlocation(locationname);

                                    //System.out.println(locationname);
                                    Geocoder geocoder = new Geocoder(v.getContext().getApplicationContext());
                                    List<Address> list = null;
                                    LatLng xy = null;
                                    try {
                                        list = geocoder.getFromLocationName(locationname, 10);
                                        double x = list.get(0).getLatitude();
                                        double y = list.get(0).getLongitude();
                                        xy = new LatLng(x, y);

                                        Log.d("test", xy.toString());


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }else{
                                Log.d("test","실패");
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiData> call, Throwable t) {
                            Log.d("test","onresponse: 실패 ");
                        }

                    });






                }
            });




            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    mStore.collection(FirebaseID.post).document(FirebaseID.documentId)
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d("stat", "DocumentSnapshot successfully deleted!");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("stat", "Error deleting document", e);
//                                }
//                            }



                    Toast.makeText(v.getContext(), "hi", Toast.LENGTH_SHORT).show();


                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        String com = company.getText().toString();
                        String num = number.getText().toString();

                        convertcom convertcom = new convertcom();
                        String fcom = convertcom.convertcompany(com);

                        //intent.putExtra("com",fcom);
                        //intent.putExtra("num",num);

                        //view.getContext().startActivity(intent);
                        String url = "https://tracker.delivery/#/"+fcom+"/"+num;

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        view.getContext().startActivity(intent);






                    }

                }



            });
        }
    }
}
