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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.IndividualRestaurant.IndividualResCategoryModel;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;

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
    ShimmerFrameLayout shimmer_layout;
    RelativeLayout addressNotFoundLayout;



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

        addressNotFoundLayout   = view.findViewById(R.id.addressNotFoundLayout);
        shimmer_layout  = view.findViewById(R.id.shimmer_layout);
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


        if (Utils.userData(getActivity()) != null) {
            if (Utils.userData(getActivity()).getId() != null) {
               getData();
            }
        }



        addAddress.setOnClickListener(v -> {
            if (Utils.userData(getActivity()) != null) {
                if (Utils.userData(getActivity()).getId() != null) {
                    Intent intent = new Intent(getActivity(),AddNewAddress.class);
                    intent.putExtra("AddAddress","Add a New Address");
                    startActivity(intent);
                }
            }else {
                Utils.toast(getActivity(),"Login Required!");
            }


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
                        if (ordersModels.size() != 0) {
                            getAddressRec.setVisibility(View.VISIBLE);
                            shimmer_layout.stopShimmer();
                            shimmer_layout.setVisibility(View.GONE);
                            addressesAdapter.setOrdersModelArrayList(ordersModels);
                            getAddressRec.setAdapter(addressesAdapter);
                            addressesAdapter.notifyDataSetChanged();
                        }else  {
                            addressNotFoundLayout.setVisibility(View.VISIBLE);
                            getAddressRec.setVisibility(View.GONE);
                            shimmer_layout.stopShimmer();
                            shimmer_layout.setVisibility(View.GONE);
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