package com.archaric.deliverypoint.IndividualRestaurant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.R;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Search extends Fragment {

    ImageView buttonCloseSearch;
    EditText search_bar;
    RecyclerView recSearch;
    ListOfItemsIndividualResAdapter listOfItemsIndividualResAdapter;

    String id;

    public Search(String id) {
        this.id = id;
    }

    public Search() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        buttonCloseSearch = view.findViewById(R.id.buttonCloseSearch);
        search_bar = view.findViewById(R.id.search_bar);
        recSearch  = view.findViewById(R.id.recSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recSearch.setLayoutManager(layoutManager);
        listOfItemsIndividualResAdapter = new ListOfItemsIndividualResAdapter();

        System.out.println(id + "Search");


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonCloseSearch.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout)).commit());

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getData(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void getData(String valueOf) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
       // System.out.println(valueOf + id);
        endPoint.getSearchItems(id,valueOf).enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Items> itemsNewArrayList = (ArrayList<Items>) response.body();
                    if (itemsNewArrayList.size() != 0) {

                        if (TextUtils.isEmpty(valueOf.toString())){
                            recSearch.setAdapter(null);
                        }else {
                            listOfItemsIndividualResAdapter.setItemsArrayList(itemsNewArrayList);
                            recSearch.setAdapter(listOfItemsIndividualResAdapter);
                            listOfItemsIndividualResAdapter.notifyDataSetChanged();
                        }



                    }
                }

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }




}