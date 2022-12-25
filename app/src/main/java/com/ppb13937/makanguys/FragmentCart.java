package com.ppb13937.makanguys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;
import com.ppb13937.makanguys.apiclient.MenuMakanan;
import com.ppb13937.makanguys.apiclient.Resto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCart extends Fragment {
    public static ArrayList<Cart> listCart;
    private static MakanGuysInterface makanGuysInterface;
    private static TextView totalPembayaran;

    AdapterCart adapter;
    public static int totalHarga = 0;
    LinearLayout ll_order;
    LinearLayout cardview_cart;
    Button btn_checkout;
    TextView namaResto;
    RecyclerView rv_cart;
    private ArrayList<String> array_gambar;
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

    public FragmentCart() {
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
    public static FragmentCart newInstance(String param1, String param2) {
        FragmentCart fragment = new FragmentCart();
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
        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cardview_cart = view.findViewById(R.id.payment_summary_cardview);
        rv_cart = view.findViewById(R.id.rvKeranjang);
        ll_order = view.findViewById(R.id.ll_order);
        namaResto = view.findViewById(R.id.tv_namaresto);
        rv_cart.setLayoutManager(new LinearLayoutManager(mContext));
        totalPembayaran = view.findViewById(R.id.total_payment);
        loadCart(mContext);
        getAllCart();
        if(listCart == null || listCart.size() == 0){
           cardview_cart.setVisibility(View.INVISIBLE);
           namaResto.setVisibility(View.INVISIBLE);
        }else{
            Log.d("listCart", String.valueOf(listCart.size()));
            int idResto = listCart.get(0).getIDResto();
            Call<List<Resto>> getResto = makanGuysInterface.getRestoByID(idResto);
            getResto.enqueue(new Callback<List<Resto>>() {
                @Override
                public void onResponse(Call<List<Resto>> call, Response<List<Resto>> response) {
                    ArrayList<Resto> listResto = (ArrayList<Resto>) response.body();
                    String nama = listResto.get(0).getName();
                    namaResto.setText(nama);
                }

                @Override
                public void onFailure(Call<List<Resto>> call, Throwable t) {

                }
            });
            ArrayList<Integer> items = new ArrayList<>();
            ArrayList<Integer> amountItems = new ArrayList<>();

            for (int i = 0; i < listCart.size(); i++) {
                int itemId = listCart.get(i).getItemID();
                int quantity = listCart.get(i).getAmount();
                items.add(itemId);
                amountItems.add(quantity);
            }

            Log.d("listHistory", "Item "  + " = " + items);

            long tanggalOrder = System.currentTimeMillis();
            ll_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    saveOrderHistory(idResto, items, amountItems, tanggalOrder, mContext);
                    CartHelper.clearCart(mContext);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("fragment", "history");
                    startActivity(intent);
                }
            });
            FragmentCart.totalHarga = 0;
            FragmentCart.updateTotalPembayaran(mContext);

        }

        return view;
    }



    private void getAllCart(){
        adapter = new AdapterCart(listCart);
        rv_cart.setAdapter(adapter);
    }

    public static void loadCart(Context pls){
        Log.d("hi","load cart");
        listCart = CartHelper.loadCart(pls);
        if (listCart == null || listCart.size() == 0){
            listCart = new ArrayList<>();
        }
        else{
            Log.d("hi","data loaded!");
        }
    }

    private void saveOrderHistory(int idResto, ArrayList<Integer> items, ArrayList<Integer> jumlahItem, long tanggalOrder,Context context) {
      OrderHistoryDatabase.saveOrderHistory(idResto, items, jumlahItem, tanggalOrder,totalHarga, context);
    }
    public static void updateTotalPembayaran(Context context) {
        loadCart(context);
        for (int i = 0; i < listCart.size(); i++) {
            int itemId = listCart.get(i).getItemID();
            int quantity = listCart.get(i).getAmount();
            //get price from API
            try {
                Call<List<MenuMakanan>> getMenu = makanGuysInterface.getMenu(itemId);
                getMenu.enqueue(new Callback<List<MenuMakanan>>() {
                    @Override
                    public void onResponse(Call<List<MenuMakanan>> call, Response<List<MenuMakanan>> response) {
                        ArrayList<MenuMakanan> listMenu = (ArrayList<MenuMakanan>) response.body();
                        String price = listMenu.get(0).getPrice();
                        int total = Integer.parseInt(price) * quantity;
                        Log.d("quantity", String.valueOf(quantity));
                        totalHarga += total;
                        Log.d("totalHarga", String.valueOf(totalHarga));
                        totalPembayaran.setText(String.valueOf(totalHarga));
                    }

                    @Override
                    public void onFailure(Call<List<MenuMakanan>> call, Throwable t) {

                    }
                });
            }
            catch(Exception e){
                Log.d("error", e.getMessage());
            }
        }
    }

}