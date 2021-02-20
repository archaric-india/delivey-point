package com.archaric.deliverypoint.IndividualRestaurant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.archaric.deliverypoint.R;


public class ListOfItemsIndividualRes extends Fragment {




    public ListOfItemsIndividualRes() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_list_of_items_individual_res, container, false);







        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        if (bundle != null) {
            String id = bundle.getString("KEY");
            if (id != null) {
                Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
            }
        }
    }
}