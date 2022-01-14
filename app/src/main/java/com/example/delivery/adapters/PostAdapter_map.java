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

import com.example.delivery.CustomDialog_map;
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

public class PostAdapter_map extends RecyclerView.Adapter<PostAdapter_map.PostViewHolder> {

    private List<Post> datas;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private OnItemClickListener mListener = null ;
    public CustomDialogListener_map customDialogListener_map ;
    CustomDialog_map dialog;


    public PostAdapter_map(){}

    public PostAdapter_map(Context context){
        this.context = context;
    }


    public interface CustomDialogListener_map {
        void itemViewclick(String i);

    }

    public PostAdapter_map(Context context,CustomDialogListener_map customDialogListener_map){
        this.context = context;
        this.customDialogListener_map = customDialogListener_map; }







    public PostAdapter_map(CustomDialog_map dialog){
        this.dialog = dialog;
    }



    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    public PostAdapter_map(Context context, List<Post> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_map, parent, false));



    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.listId.setText(data.getListId());
        holder.company.setText(data.getCompany());
        holder.number.setText(data.getNumber());


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
        private String carrier;
        private String track_id;




        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title_map);
            listId = itemView.findViewById(R.id.listId_map);
            company = itemView.findViewById(R.id.item_post_company_map);
            number = itemView.findViewById(R.id.item_post_number_map);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    carrier = company.getText().toString();
                    track_id = number.getText().toString();
                    convertcom convertcom = new convertcom();
                    String carrier_id = convertcom.convertcompany(carrier);



                    ArrayList<LatLng> pointlist = new ArrayList<>();

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


                                    String locationname = apiData.getProgresses().get(i).getLocation().toString();

                                    convertloc convertloc = new convertloc();
                                    locationname = convertloc.convertlocation(locationname);

                                    Geocoder geocoder = new Geocoder(view.getContext().getApplicationContext());
                                    List<Address> list = null;

                                    LatLng xy = null;
                                    try {
                                        list = geocoder.getFromLocationName(locationname, 10);
                                        double x = list.get(0).getLatitude();
                                        double y = list.get(0).getLongitude();
                                        xy = new LatLng(x, y);

                                        pointlist.add(xy);
//                                        CustomDialog_map customDialog_map = new CustomDialog_map(view.getContext());
//                                        customDialog_map.dismiss();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }
                                System.out.println(pointlist.toString());
                                //Frag_map frag_map = new Frag_map(listId.getContext(), pointlist);
                                String ee = "hi";
                                customDialogListener_map.itemViewclick(ee);

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
        }
    }
}
