package com.archaric.deliverypoint.OrderHistory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.AddressModel;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Settings.AddNewAddress;
import com.archaric.deliverypoint.Settings.SettingsContent;
import com.archaric.deliverypoint.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Settings.Settings.SETTINGS_CONTENT_KEY;


public class ChangeAddressAtCart extends BottomSheetDialogFragment {

    ImageView buttonCloseChange;
    ChangeAddressAtCartAdapter changeAddressAtCartAdapter;
    RecyclerView addressRec;
    TextView addAddress;


    public ChangeAddressAtCart() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_address_at_cart, container, false);

        addAddress = view.findViewById(R.id.addAddress);
        buttonCloseChange = view.findViewById(R.id.buttonCloseChange);
        addressRec = view.findViewById(R.id.addressRec);
        changeAddressAtCartAdapter = new ChangeAddressAtCartAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addressRec.setLayoutManager(layoutManager);

        buttonCloseChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewAddress.class);
                intent.putExtra("AddAddress","Add a New Address");
                startActivity(intent);

            }
        });


//        buttonCloseChange.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().
//                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.addressChanger)).commit());

        getData();



    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        if(Utils.userData(getActivity()) != null){
            endPoint.getUserAddress(Utils.userData(getActivity()).getId()).enqueue(new Callback<List<OrdersModel>>() {
                @Override
                public void onResponse(Call<List<OrdersModel>> call, Response<List<OrdersModel>> response) {
                    ArrayList<OrdersModel> ordersModels = (ArrayList<OrdersModel>) response.body();
                    if (response.isSuccessful()){
                        if (ordersModels != null) {
                            changeAddressAtCartAdapter.setOrdersModels(ordersModels, getContext());
                            addressRec.setAdapter(changeAddressAtCartAdapter);
                            changeAddressAtCartAdapter.notifyDataSetChanged();
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