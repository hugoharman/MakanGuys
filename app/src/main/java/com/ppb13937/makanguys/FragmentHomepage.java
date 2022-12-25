package com.ppb13937.makanguys;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.Resto;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHomepage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomepage extends Fragment {
    MakanGuysInterface makanGuysInterface;
    AdapterResto adapter;

    RecyclerView rv_mu;
    private Context mContext;
    ViewFlipper viewFlipper;

    //user data
    private FirebaseUser user;
    private String userID;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHomepage() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMenuUtama.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHomepage newInstance(String param1, String param2) {
        FragmentHomepage fragment = new FragmentHomepage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        // Set up view flipper
        int images[] = {R.drawable.tahugimbal, R.drawable.ayamgeprekkeju};
        viewFlipper = view.findViewById(R.id.flipper_dashboard);
        for (int image : images) {
            fliverImages(image);
        }

        // Set up greeting for user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        final TextView greetUser = view.findViewById(R.id.tv_nama);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.child(userID);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameFromDB = snapshot.child("name").getValue(String.class);
                if (usernameFromDB != null) {
                    String namaUser = usernameFromDB;
                    greetUser.setText("Selamat Datang, " + namaUser + "!");
                } else {
                    greetUser.setText("Selamat Datang!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Gagal Mengambil Data User", Toast.LENGTH_LONG).show();
            }
        });

        // Set up menu recycler view
        rv_mu = view.findViewById(R.id.rv_menu);
        LinearLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        rv_mu.setLayoutManager(layoutManager);

        getAllResto();

        return view;
    }


    private void getAllResto(){
        Call<List<Resto>> getResto = makanGuysInterface.getResto();
        getResto.enqueue(new Callback<List<Resto>>() {
            @Override
            public void onResponse(Call<List<Resto>> call, Response<List<Resto>> response) {
                ArrayList<Resto> listResto = (ArrayList<Resto>) response.body();

                adapter = new AdapterResto(listResto);
                rv_mu.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Resto>> call, Throwable t) {
                Log.e("error_resto: ", t.getMessage());
            }
        });
    }

    public void fliverImages(int images) {
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundResource(images);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(mContext, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(mContext, android.R.anim.slide_out_right);
    }
}