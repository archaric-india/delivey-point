package com.archaric.deliverypoint.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.IndividualRestaurant.IndividualResCategoryModel;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Settings.Settings.SETTINGS_CONTENT_KEY;


public class MyAddresses extends Fragment  {

   // implements PopupMenu.OnMenuItemClickListener

    ImageView moreOptions;
    LinearLayout backToHomePageOnTitle;
    AddressDataInterface addressDataInterface;
    TextView addAddress;
    RecyclerView getAddressRec;
    AddressesAdapter addressesAdapter;



    public MyAddresses() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_addresses, container, false);

        backToHomePageOnTitle = view.findViewById(R.id.backToHomePageOnTitle);
        addAddress = view.findViewById(R.id.addAddress);
        getAddressRec =  view.findViewById(R.id.getAddressRec);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        getAddressRec.setLayoutManager(layoutManager);
        addressesAdapter = new AddressesAdapter();


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getData();


        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AddNewAddress.class);
            intent.putExtra("AddAddress","Add a New Address");
            startActivity(intent);

        });



//      moreOptions.setOnClickListener(v -> {
//
//          PopupMenu popup = new PopupMenu(getActivity(), v, Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
//          popup.inflate(R.menu.popup_menu_add_edit);
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//              popup.setGravity(Gravity.END);
//              popup.setOnMenuItemClickListener(MyAddresses.this::onMenuItemClick);
//              popup.setGravity(Gravity.RIGHT);
//          }
//
//          popup.show();
//
//      });

      backToHomePageOnTitle.setOnClickListener(v ->{
         getActivity().onBackPressed();
              });
    }




//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit:
//
//                Intent intent = new Intent(getActivity(),AddNewAddress.class);
//                intent.putExtra("AddAddress","Edit Address");
//                startActivity(intent);
//
//                return true;
//            case R.id.remove:
//
//                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
//                    switch (which){
//                        case DialogInterface.BUTTON_POSITIVE:
//                            dialog.dismiss();
//                            break;
//
//                        case DialogInterface.BUTTON_NEGATIVE:
//                            dialog.dismiss();
//                            break;
//                    }
//                };
//
//                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("Are you sure you want to remove this address?").setPositiveButton("Yes", dialogClickListener)
//                        .setNegativeButton("No", dialogClickListener).show();
//
//                return true;
//            default:
//                return false;
//        }
//    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        if (Utils.userData(getActivity()) != null) {
            endPoint.getUserAddress(Utils.userData(getActivity()).getId()).enqueue(new Callback<List<OrdersModel>>() {
                @Override
                public void onResponse(Call<List<OrdersModel>> call, Response<List<OrdersModel>> response) {
                    ArrayList<OrdersModel> ordersModels = (ArrayList<OrdersModel>) response.body();
                    if (response.isSuccessful()){
                        if (ordersModels != null) {
                            addressesAdapter.setOrdersModelArrayList(ordersModels);
                            getAddressRec.setAdapter(addressesAdapter);
                            addressesAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrdersModel>> call, Throwable t) {

                }
            });
        }


    }
}