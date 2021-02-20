package com.archaric.deliverypoint.IndividualRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.archaric.deliverypoint.IndividualRestaurant.MoreInfoIndividualRes.KEY_TO_GET_RES_RATINGS;

public class IndividualRestaurantRating extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    RatingAdapter ratingAdapter;
    LinearLayout backToHomePageOnTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_individual_restaurant_rating);
        init();
        FiftyPercentOfferModel arrivalDataHere = (FiftyPercentOfferModel)getIntent().getSerializableExtra(KEY_TO_GET_RES_RATINGS);
        if (!arrivalDataHere.toString().isEmpty()) {
            getReviewData(arrivalDataHere);
        }

        backToHomePageOnTitle.setOnClickListener(v -> onBackPressed());

    }

    private void init(){
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
        recyclerView = findViewById(R.id.rec);
        ratingAdapter = new RatingAdapter();
        backToHomePageOnTitle = findViewById(R.id.backToHomePageOnTitle);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getReviewData(FiftyPercentOfferModel arrivalDataHere) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getResRating(arrivalDataHere.getId()).enqueue(new Callback<OverAllRatingModel>() {
            @Override
            public void onResponse(@NotNull Call<OverAllRatingModel> call, @NotNull Response<OverAllRatingModel> response) {

                OverAllRatingModel overAllRatingModel = response.body();
                if (overAllRatingModel != null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    ratingAdapter.setRatingModels(overAllRatingModel);
                    recyclerView.setAdapter(ratingAdapter);
                }

            }

            @Override
            public void onFailure(@NotNull Call<OverAllRatingModel> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}