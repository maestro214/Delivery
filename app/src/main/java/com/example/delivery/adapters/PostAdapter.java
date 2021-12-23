package com.example.delivery.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.delivery.FirebaseID;
import com.example.delivery.Frag_list;
import com.example.delivery.Frag_map;
import com.example.delivery.MainActivity;
import com.example.delivery.MapPoint;
import com.example.delivery.R;
import com.example.delivery.SignupActivity;
import com.example.delivery.WebviewActivity;
import com.example.delivery.models.ApiPost;
import com.example.delivery.models.Post;
import com.example.delivery.convertcom.convertcom;
import com.example.delivery.retrofit.DeliveryService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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


//                    String com = company.getText().toString();
//                    String num = number.getText().toString();
//
//                    //Log.d("test",com+num);
//                    convertcom convertcom = new convertcom();
//                    String fcom = convertcom.convertcompany(com);
//                    Gson gson = new GsonBuilder().create();
//
//                    String apiuri = "https://apis.tracker.delivery/carriers/"+fcom+"/tracks/"+num;
//
//                    try {
//                        URL url = new URL(apiuri);
//
//                        InputStream is = url.openStream();
//                        InputStreamReader isr = new InputStreamReader(is);
//                        BufferedReader reader = new BufferedReader(isr);
//
//                        StringBuffer buffer = new StringBuffer();
//                        String line = reader.readLine();
//                        Log.d("line",line);
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }

//                    Intent intent = new Intent(v.getContext(), Frag_map.class);
//                    v.getContext().startActivity(intent);









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
