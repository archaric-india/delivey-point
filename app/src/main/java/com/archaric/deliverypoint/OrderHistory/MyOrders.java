package com.archaric.deliverypoint.OrderHistory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.R;

import static com.archaric.deliverypoint.IndividualRestaurant.ListOfItemsIndividualResAdapter.ITEM_ID_DATA;
import static com.archaric.deliverypoint.OrderHistory.OrderDetailsAdapter.DELIVERED_KEY;
import static com.archaric.deliverypoint.OrderHistory.OrderDetailsAdapter.DELIVERY_DATA;

public class MyOrders extends AppCompatActivity  {


    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_my_orders);

        Intent intent = getIntent();
        String keyToFragment = intent.getStringExtra(DELIVERED_KEY);



        FragmentTransaction transaction5 = getSupportFragmentManager().beginTransaction();
        if (!TextUtils.isEmpty(keyToFragment)){
            switch (keyToFragment){
                case "OrderTracking":
                        transaction5.replace(R.id.orderTracking,new OrderTracking());
                        transaction5.commit();
                    break;
                case "WriteAReview":
                    transaction5.replace(R.id.orderTracking,new WriteAReview());
                    transaction5.commit();
                    break;
                default:
                    transaction5.replace(R.id.orderTracking,new OrderDelivered());
                    transaction5.commit();
                    break;
            }

        }












    }
}