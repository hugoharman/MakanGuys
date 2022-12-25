package com.ppb13937.makanguys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment implements OrderHistoryDatabase.OrderHistoryCallback {

    public static ArrayList<History> listHistory;
    MakanGuysInterface makanGuysInterface;
    AdapterHistory adapter;
    public boolean isHistoryLoaded = false;
    RecyclerView rv_history;
    AdapterHomepage adapterHomepage;
    private Context mContext;
    TextView rating;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHistory() {
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
    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
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
    public void onOrderHistoryLoaded(ArrayList<History> historyList) {
        listHistory = historyList;
        Log.d("History", "onOrderHistoryLoaded: " + listHistory.size());
        // Use the listHistory variable here
        isHistoryLoaded = true;

    }

    @Override
    public void onOrderHistoryLoadFailed(String error) {
        Log.d("FragmentHistory", error);
        // Show an error message or do something else
        isHistoryLoaded = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        OrderHistoryDatabase database = new OrderHistoryDatabase();
        database.loadOrderHistory(this);
        //make sure loadorder history first
        if(!isHistoryLoaded && listHistory == null){
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("fragment", "history");
            startActivity(intent);
        }

        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rv_history = view.findViewById(R.id.rv_history);
        rv_history.setLayoutManager(new LinearLayoutManager(mContext));
        if (listHistory != null) {
            Log.d("listHistory", "onCreateView: " + listHistory.size());
            adapter = new AdapterHistory(listHistory);
            rv_history.setAdapter(adapter);
        }

        return view;
    }





}