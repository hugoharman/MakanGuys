package com.ppb13937.makanguys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderHistoryDatabase {
    private static final String TAG = OrderHistoryDatabase.class.getSimpleName();

    public static void saveOrderHistory(int idResto, ArrayList<Integer> items, ArrayList<Integer> jumlahItem, long tanggalOrder, int hargaTotal, Context mContext) {
        History history = new History(idResto, items, jumlahItem, tanggalOrder, hargaTotal);
        FirebaseDatabase.getInstance().getReference("order_history")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(String.valueOf(tanggalOrder))
                .setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Order history saved", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.putExtra("fragment", "history");
                                    mContext.startActivity(intent);
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(mContext, "Order history failed to save", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    public interface OrderHistoryCallback {
        void onOrderHistoryLoaded(ArrayList<History> historyList);
        void onOrderHistoryLoadFailed(String error);
    }

    public void loadOrderHistory(final OrderHistoryCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("order_history")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<History> historyList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    long tanggalOrder = Long.parseLong(key);
                    int jumlahItem = 0;
                    int idResto = 0;
                    int totalHarga = 0;
                    ArrayList<Integer> listItems = new ArrayList<>();
                    ArrayList<Integer> amountItems = new ArrayList<>();
                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                        String key2 = childSnapshot.getKey();
                        Object value2 = childSnapshot.getValue();
                        switch (key2) {
                            case "idResto":
                                idResto = Integer.parseInt(value2.toString());
                                break;
                            case "items":
                                listItems = new Gson().fromJson(value2.toString(), new TypeToken<ArrayList<Integer>>(){}.getType());
                                break;
                            case "jumlahItems":
                                amountItems = new Gson().fromJson(value2.toString(), new TypeToken<ArrayList<Integer>>(){}.getType());
                                break;
                            case "totalHarga":
                                totalHarga = Integer.parseInt(value2.toString());
                                break;
                        }
                    }
                    History history = new History(idResto, listItems, amountItems, tanggalOrder, totalHarga);
                    historyList.add(history);
                }
                callback.onOrderHistoryLoaded(historyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onOrderHistoryLoadFailed(databaseError.getMessage());
            }
        });
    }
}
