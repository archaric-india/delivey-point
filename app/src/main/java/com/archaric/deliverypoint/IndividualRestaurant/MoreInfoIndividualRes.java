package com.archaric.deliverypoint.IndividualRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.TextView;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel;
import com.archaric.deliverypoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.archaric.deliverypoint.IndividualRestaurant.IndividualRestaurant.MORE_INFO_KEY;

public class MoreInfoIndividualRes extends AppCompatActivity {

    TextView resName_MoreInfo, categoryOfIndividualRes, reviewTime_MoreInfo,
            priceFiftyPerOff, totalNumOfReviewRes_MoreInfo, address_MoreInfo,
            overAllRating_MoreInfo, overAllValue_MoreInfo, ORRateValue, QFRateValue, DRateValue, VOMRateValue, resCat, reviewNote, reviewTime;

    RatingBar rBar_MoreInfo,overAllRatingBar_MoreInfo, ratingBarOfOR, ratingBarOfQF, ratingBarOfD, ratingBarOfVOM,ratingBarOfResReviews;

    ImageView restaurants_Iv_MoreInfo,resLogo_MoreInfo,backToHomePage_MoreInfo, itemImageReview;

    LinearLayout reviewLayoutForOne, seeAllLayout;

    ShimmerFrameLayout shimmer_layout;

    CardView cardView;

    public static final String KEY_TO_GET_RES_RATINGS = "indResRating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_more_info_individual_res);

        init();

        FiftyPercentOfferModel arrivalData = (FiftyPercentOfferModel)getIntent().getSerializableExtra(MORE_INFO_KEY);
        if (!arrivalData.toString().isEmpty()) {
            ratingData(arrivalData);
            individualCat(arrivalData);
            plotData(arrivalData);
            oneItemRatingData(arrivalData);
        }

        backToHomePage_MoreInfo.setOnClickListener(v -> onBackPressed());

        seeAllLayout.setOnClickListener(v -> {
            if (!arrivalData.toString().isEmpty()) {
                Intent intent = new Intent(MoreInfoIndividualRes.this, IndividualRestaurantRating.class);
                intent.putExtra(KEY_TO_GET_RES_RATINGS, arrivalData);
                startActivity(intent);
            }
        });
    }

    private void init(){
        resName_MoreInfo = findViewById(R.id.resName_MoreInfo);
                categoryOfIndividualRes= findViewById(R.id.categoryOfIndividualRes);
        reviewTime_MoreInfo= findViewById(R.id.reviewTime_MoreInfo);
                priceFiftyPerOff= findViewById(R.id.priceFiftyPerOff);
        rBar_MoreInfo = findViewById(R.id.ratingBarOfRes);
                totalNumOfReviewRes_MoreInfo= findViewById(R.id.totalNumOfReviewRes);
        address_MoreInfo= findViewById(R.id.address_MoreInfo);
                overAllRating_MoreInfo= findViewById(R.id.overAllRating_MoreInfo);
        overAllValue_MoreInfo= findViewById(R.id.overAllValue_MoreInfo);
                ORRateValue= findViewById(R.id.ORRateValue);
        QFRateValue= findViewById(R.id.QFRateValue);
                DRateValue= findViewById(R.id.DRateValue);
        VOMRateValue= findViewById(R.id.VOMRateValue);
        resLogo_MoreInfo= findViewById(R.id.resLogo_MoreInfo);
                restaurants_Iv_MoreInfo= findViewById(R.id.restaurants_Iv_MoreInfo);
        overAllRatingBar_MoreInfo= findViewById(R.id.overAllRatingBar_MoreInfo);
        backToHomePage_MoreInfo = findViewById(R.id.backToHomePage_MoreInfo);
        ratingBarOfOR= findViewById(R.id.ratingBarOfOR);
        ratingBarOfQF= findViewById(R.id.ratingBarOfQF);
        ratingBarOfD= findViewById(R.id.ratingBarOfD);
        ratingBarOfVOM= findViewById(R.id.ratingBarOfVOM);
        resCat = findViewById(R.id.resCat);

        ratingBarOfResReviews = findViewById(R.id.ratingBarOfResReviews);
        reviewNote = findViewById(R.id.reviewNote);
        reviewTime = findViewById(R.id.reviewTime);
        itemImageReview = findViewById(R.id.itemImageReview);
        reviewLayoutForOne = findViewById(R.id.reviewLayoutForOne);
        seeAllLayout = findViewById(R.id.seeAllLayout);

        shimmer_layout = findViewById(R.id.shimmer_layout);
                cardView = findViewById(R.id.card);
    }

    private void plotData(FiftyPercentOfferModel model){

        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);

        Glide.with(this)
                .load(model.getProfile())
                .apply(reqOpt)
                .into(restaurants_Iv_MoreInfo);

        Glide.with(this)
                .load(model.getLogo())
                .apply(reqOpt)
                .into(resLogo_MoreInfo);

        resName_MoreInfo.setText(model.getName());
        categoryOfIndividualRes.setText(model.getCategory());
        reviewTime_MoreInfo.setText(model.getTimevalue());
        totalNumOfReviewRes_MoreInfo.setText("("+model.getRatinglenght()+")");
        address_MoreInfo.setText(model.getAddress());
        overAllRating_MoreInfo.setText(String.valueOf(model.getRating()));
        rBar_MoreInfo.setRating(model.getRating());
        address_MoreInfo.setText(model.getAddress());
        overAllRatingBar_MoreInfo.setRating(model.getRating());
        overAllValue_MoreInfo.setText("("+model.getRatinglenght()+")");



    }

    private void individualCat(FiftyPercentOfferModel offerModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getResCategory(offerModel.getId()).enqueue(new Callback<List<IndividualResCategoryModel>>() {
            @Override
            public void onResponse(Call<List<IndividualResCategoryModel>> call, Response<List<IndividualResCategoryModel>> response) {
                ArrayList<IndividualResCategoryModel> list = (ArrayList<IndividualResCategoryModel>) response.body();

                if (list != null) {
                    StringBuilder cat =new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {


                        cat.append( list.get(i).getCategory()).append(",");
                        System.out.println(cat);

                    }

                    resCat.setText(cat);
                }
            }

            @Override
            public void onFailure(Call<List<IndividualResCategoryModel>> call, @NotNull Throwable t) {
                    t.printStackTrace();
            }
        });

    }

    private void ratingData(FiftyPercentOfferModel percentOfferModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getResRating(percentOfferModel.getId()).enqueue(new Callback<OverAllRatingModel>() {
            @Override
            public void onResponse(Call<OverAllRatingModel> call, Response<OverAllRatingModel> response) {
                OverAllRatingModel allRatingModel = response.body();
                if (allRatingModel != null) {
                    plotRatingData(allRatingModel);
                }
            }

            @Override
            public void onFailure(@NotNull Call<OverAllRatingModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void oneItemRatingData(FiftyPercentOfferModel offerModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getResRating(offerModel.getId()).enqueue(new Callback<OverAllRatingModel>() {
            @Override
            public void onResponse(Call<OverAllRatingModel> call, Response<OverAllRatingModel> response) {

                OverAllRatingModel overAllRatingModel = response.body();
                if (overAllRatingModel.getRatings().size() != 0) {
                    reviewLayoutForOne.setVisibility(View.VISIBLE);
                    try {
                        ratingBarOfResReviews.setRating(overAllRatingModel.getRatings().get(0).getValue());
                        reviewNote.setText(overAllRatingModel.getRatings().get(0).getNote());
                        DateFormat formatter = new SimpleDateFormat("d MMM yyyy");

                        long milliSeconds= Long.parseLong(overAllRatingModel.getRatings().get(0).getDate());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);

                        reviewTime.setText(formatter.format(calendar.getTime()));

                        RequestOptions reqOpt = RequestOptions
                                .fitCenterTransform()
                                .dontAnimate().dontTransform()
                                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                                .priority(Priority.IMMEDIATE)
                                .encodeFormat(Bitmap.CompressFormat.PNG)
                                .format(DecodeFormat.DEFAULT);
                        Glide.with(itemImageReview.getContext())
                                .load(overAllRatingModel.getRatings().get(0).getImage())
                                .apply(reqOpt)
                                .into(itemImageReview);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    reviewLayoutForOne.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<OverAllRatingModel> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void plotRatingData(OverAllRatingModel allRatingModel){
        ratingBarOfOR.setRating(allRatingModel.getOr());
        ratingBarOfQF.setRating(allRatingModel.getQof());
        ratingBarOfD.setRating(allRatingModel.getD());
        ratingBarOfVOM.setRating(allRatingModel.getVfm());
        ORRateValue.setText(String.valueOf(allRatingModel.getOr()));
        QFRateValue.setText(String.valueOf(allRatingModel.getQof()));
        DRateValue.setText(String.valueOf(allRatingModel.getD()));
        VOMRateValue.setText(String.valueOf(allRatingModel.getVfm()));
        shimmer_layout.stopShimmer();
        shimmer_layout.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }
}