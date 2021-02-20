package com.archaric.deliverypoint.IndividualRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archaric.deliverypoint.ChangeDrawerInterface;
import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.MainActivity;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.SendItemData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Fragments.FiftyPercentOffersAdapter.RES_DATA;

public class IndividualRestaurant extends AppCompatActivity implements SendItemData {

    public static final String MORE_INFO_KEY = "MoreINfoKey";
    ImageView restaurants_Iv, backToHomePage, resLogo, searchButtonOnTitle, search;
    TextView resName,categoryOfIndividualRes,estimatedDeliveryTimeOfRes,totalNumOfReviewRes,menusTV,reviewTV,resNameTop;
    RatingBar ratingBarOfRes;
    RecyclerView categoriesRecyclerView, categoriesContentRecyclerView, reviewRecyclerView;
    IndividualCategoryAdapter individualCategoryAdapter;
    ListOfItemsIndividualResAdapter listOfItemsIndividualResAdapter;
    RatingAdapter ratingAdapter;
    LinearLayout menusLayout, reviewLayout, backToHomePageOnTitle,moreInfoOfRes;
    View menusView, reviewView;
    CardView searchLayout, cardView, cardView2;
    Search fragment;
    ShimmerFrameLayout shimmerFrameLayout,shimmer_layout2, shimmer_layout3;
    RelativeLayout gotoCart;
    ChangeDrawerInterface changeDrawerInterface;



    public static final String RES_ID_KEY = "resIdKey";

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_individual_restaurant);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init();

        FiftyPercentOfferModel data = (FiftyPercentOfferModel)getIntent().getSerializableExtra(RES_DATA);
        if (!data.toString().isEmpty()) {
            id = data.getId();
           plotData(data);
           getData(data);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        categoriesRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagerLin = new LinearLayoutManager(this);
        categoriesContentRecyclerView.setLayoutManager(layoutManagerLin);

        LinearLayoutManager layoutManagerLin2 = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManagerLin2);


        backToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreInfoOfRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.toString().isEmpty()) {
                    Intent intent = new Intent(IndividualRestaurant.this, MoreInfoIndividualRes.class);
                    intent.putExtra(MORE_INFO_KEY, data);
                    startActivity(intent);
                }

            }
        });

        menusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((menusView.getVisibility() != View.VISIBLE)){
                    menusView.setVisibility(View.VISIBLE);
                    menusTV.setTextColor(getResources().getColor(R.color.purple));
                }

                if ((reviewView.getVisibility() == View.VISIBLE)){
                    reviewView.setVisibility(View.GONE);
                    reviewTV.setTextColor(getResources().getColor(R.color.black));
                }
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);

                if (!data.toString().isEmpty()) {
                    getData(data);
                }

            }
        });

        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((menusView.getVisibility() == View.VISIBLE)){
                    menusView.setVisibility(View.GONE);
                    menusTV.setTextColor(getResources().getColor(R.color.black));
                }

                if ((reviewView.getVisibility() != View.VISIBLE)){
                    reviewView.setVisibility(View.VISIBLE);
                    reviewTV.setTextColor(getResources().getColor(R.color.purple));
                }


                if (!data.toString().isEmpty()) {
                   getReviewData(data);
                }


            }
        });

        backToHomePageOnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IndividualRestaurant.this, MainActivity.class);
                intent.putExtra("setCart", "toCartFragment");
                startActivity(intent);

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init(){

        gotoCart  = findViewById(R.id.gotoCart);
        restaurants_Iv = findViewById(R.id.restaurants_Iv);
        backToHomePage = findViewById(R.id.backToHomePage);
        resLogo= findViewById(R.id.resLogo);
        resName= findViewById(R.id.resName);
        categoryOfIndividualRes= findViewById(R.id.categoryOfIndividualRes);
        estimatedDeliveryTimeOfRes= findViewById(R.id.estimatedDeliveryTimeOfRes);
        totalNumOfReviewRes= findViewById(R.id.totalNumOfReviewRes);
        moreInfoOfRes = findViewById(R.id.moreInfoOfRes);
        ratingBarOfRes  = findViewById(R.id.ratingBarOfRes);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        menusTV = findViewById(R.id.menusTV);
        menusLayout = findViewById(R.id.menusLayout);
        menusView = findViewById(R.id.menusView);
        reviewTV = findViewById(R.id.reviewTV);
        reviewLayout = findViewById(R.id.reviewLayout);
        reviewView = findViewById(R.id.reviewView);
        categoriesContentRecyclerView = findViewById(R.id.categoriesContentRecyclerView);
        resNameTop = findViewById(R.id.resNameTop);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);
        searchButtonOnTitle = findViewById(R.id.searchOnTitle);
        searchLayout = findViewById(R.id.searchLayout);
        search = findViewById(R.id.searchButton);
        cardView  = findViewById(R.id.cardView);
        cardView2  = findViewById(R.id.cardView2);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
        shimmer_layout2 = findViewById(R.id.shimmer_layout2);
        shimmer_layout3 = findViewById(R.id.shimmer_layout3);

        fragment = new Search();


        ratingAdapter = new RatingAdapter();
        individualCategoryAdapter = new IndividualCategoryAdapter(categoriesRecyclerView);
        listOfItemsIndividualResAdapter = new ListOfItemsIndividualResAdapter();

    }

    private void plotData(FiftyPercentOfferModel fiftyPercentOfferModel){


        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);

        Glide.with(this)
                .load(fiftyPercentOfferModel.getProfile())
                .apply(reqOpt)
                .into(restaurants_Iv);

        Glide.with(this)
                .load(fiftyPercentOfferModel.getLogo())
                .apply(reqOpt)
                .into(resLogo);

        resName.setText(fiftyPercentOfferModel.getName());
        categoryOfIndividualRes.setText(fiftyPercentOfferModel.getCategory());
        ratingBarOfRes.setRating(fiftyPercentOfferModel.getRating());
        totalNumOfReviewRes.setText("("+ fiftyPercentOfferModel.getRatinglenght() +")");
        estimatedDeliveryTimeOfRes.setText(fiftyPercentOfferModel.getTimevalue());
        resNameTop.setText(fiftyPercentOfferModel.getName());

    }

    private void getData(FiftyPercentOfferModel percentOfferModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getResCategory(percentOfferModel.getId()).enqueue(new Callback<List<IndividualResCategoryModel>>() {
            @Override
            public void onResponse(Call<List<IndividualResCategoryModel>> call, Response<List<IndividualResCategoryModel>> response) {
                ArrayList<IndividualResCategoryModel> resCategoryModels = (ArrayList<IndividualResCategoryModel>) response.body();
                if (resCategoryModels != null) {

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    categoriesRecyclerView.setVisibility(View.VISIBLE);
                    reviewRecyclerView.setVisibility(View.GONE);
                    categoriesContentRecyclerView.setVisibility(View.VISIBLE);
                    categoriesRecyclerView.setVisibility(View.VISIBLE);
                    individualCategoryAdapter.setIndividualResCategoryModels(resCategoryModels);
                    categoriesRecyclerView.setAdapter(individualCategoryAdapter);
                    individualCategoryAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<List<IndividualResCategoryModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getReviewData(FiftyPercentOfferModel offerModel){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getResRating(offerModel.getId()).enqueue(new Callback<OverAllRatingModel>() {
            @Override
            public void onResponse(Call<OverAllRatingModel> call, Response<OverAllRatingModel> response) {

                OverAllRatingModel overAllRatingModel = response.body();
                if (overAllRatingModel != null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmer_layout2.stopShimmer();
                    shimmer_layout2.setVisibility(View.GONE);
                    categoriesRecyclerView.setVisibility(View.GONE);
                    categoriesContentRecyclerView.setVisibility(View.GONE);
                    reviewRecyclerView.setVisibility(View.VISIBLE);
                    ratingAdapter.setRatingModels(overAllRatingModel);
                    reviewRecyclerView.setAdapter(ratingAdapter);


                }

            }

            @Override
            public void onFailure(Call<OverAllRatingModel> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


    @Override
    public void sendData(List<Items> items) {
        shimmer_layout2.stopShimmer();
        shimmer_layout2.setVisibility(View.GONE);
        categoriesContentRecyclerView.setVisibility(View.VISIBLE);
        listOfItemsIndividualResAdapter.setItemsArrayList(items);
        categoriesContentRecyclerView.setAdapter(listOfItemsIndividualResAdapter);
    }

    public void searchBoxShow(View view) {
        Bundle bundle = new Bundle();
        if (id != null) {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout,new Search(id));
            transaction.commit();
        }

    }
}