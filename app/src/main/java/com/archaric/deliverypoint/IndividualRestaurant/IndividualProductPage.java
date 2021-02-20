package com.archaric.deliverypoint.IndividualRestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.archaric.deliverypoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.archaric.deliverypoint.IndividualRestaurant.ListOfItemsIndividualResAdapter.ITEM_ID_DATA;

public class IndividualProductPage extends AppCompatActivity implements View.OnClickListener, AddonsValues, AddonsKey {

    ImageView individual_Product_iv, minus, add;
    LinearLayout backToResPage, second;
    RelativeLayout addToCart;
    TextView resultCart, productPrice, productName, productCategory, totalOfProduct, specialRequest;
    RadioGroup radioGrp;
    RecyclerView recAddons;
    int i = 1;
    List<Integer> ids = new ArrayList<>();
    Map<String, Double> mapForGetValues = new HashMap<>();
    Double variationValue = 0.0, addonsValue = 0.0;
    AddonsAdapter addonsAdapter;
    ArrayList<Items> itemsArrayList = new ArrayList<>();

    String variations, hereSpecialRequest;
    List<String> addons = new ArrayList<>();
    float totalPriceToCart;

    Items item;

    String resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product_page);
        init();
        Items data = (Items) getIntent().getSerializableExtra(ITEM_ID_DATA);
        if (!data.toString().isEmpty()) {
            plotData(data);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("REFRESH"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recAddons.setLayoutManager(layoutManager);

        backToResPage.setOnClickListener(v -> onBackPressed());

        add.setOnClickListener(v -> {
            if (i<10){
                i = i+1;
                resultCart.setText(String.valueOf(i));
                refreshTotalValue();
            }else {
                Toast.makeText(IndividualProductPage.this, "Delivery Limit 10 Only", Toast.LENGTH_SHORT).show();
            }
        });

        minus.setOnClickListener(v -> {
            if (i > 1) {
                i = i - 1;
                resultCart.setText(String.valueOf(i));
                refreshTotalValue();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(specialRequest.getText().toString())){
                    hereSpecialRequest = "Nothing";
                }



               // System.out.println(data.getBid());
                item = new Items(data.getId(),
                        data.getName(),
                        data.getImage(),
                        totalPriceToCart,variations,
                        addons,
                        hereSpecialRequest,
                        resultCart.getText().toString(),data.getBid());

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();

                String json = sharedPrefs.getString("TAG", "[]");
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Type type = new TypeToken<List<Items>>() {}.getType();
                itemsArrayList = gson.fromJson(json, type);

                 checkCartData();

                if (resId.equals(data.getBid())){
                    itemsArrayList.add(item);
                    json = gson.toJson(itemsArrayList);
                    editor.putString("TAG", json);
                    System.out.println(json);
                    editor.commit();
                }else {
                    itemsArrayList.clear();
                    System.out.println("Cart Cleared!!! \n Restaurant Changed!");
                    itemsArrayList.add(item);
                    json = gson.toJson(itemsArrayList);
                    editor.putString("TAG", json);
                    System.out.println(json);
                    editor.commit();
                }


                Toast.makeText(IndividualProductPage.this, "Added to Cart!", Toast.LENGTH_SHORT).show();

            }
        });





    }

    private void init(){
        specialRequest = findViewById(R.id.specialRequest);
        individual_Product_iv = findViewById(R.id.individual_Product_iv);
        backToResPage = findViewById(R.id.backToResPage);
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        resultCart = findViewById(R.id.resultCart);
        productName = findViewById(R.id.productName);
        productCategory = findViewById(R.id.productCategory);
        productPrice = findViewById(R.id.productPrice);
        radioGrp = findViewById(R.id.radioGrp);
        second = findViewById(R.id.second);
        recAddons = findViewById(R.id.recAddons);
        totalOfProduct = findViewById(R.id.totalOfProduct);
        addonsAdapter = new AddonsAdapter();
        addToCart  = findViewById(R.id.addToCart);

    }

    private void plotData(Items items){
        totalPriceToCart = items.getPrice();
        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .dontAnimate().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);
        Glide.with(this)
                .load(items.getImage())
                .apply(reqOpt)
                .into(individual_Product_iv);

        if (items.getVariations() != null) {
            radioGrp.setVisibility(View.VISIBLE);
            Map<String, Double> map = new HashMap<>();
            map.putAll(items.getVariations());
            mapForGetValues.putAll(items.getVariations());
            addRadioButtons(map);

        }
        else {
            second.setVisibility(View.GONE);
            productPrice.setText("KD " + items.getPrice());
            variationValue = Double.valueOf(items.getPrice());
            refreshTotalValue();
        }

        if (items.getAddons() != null) {
            second.setVisibility(View.VISIBLE);
            addonsAdapter.setMapForGetValues(items.getAddons());
            recAddons.setAdapter(addonsAdapter);
            addonsAdapter.notifyDataSetChanged();

        }

        productName.setText(items.getName());
        productCategory.setText(items.getCategory());
        refreshTotalValue();
    }

    public void addRadioButtons( Map<String, Double> map) {
        radioGrp.setOrientation(LinearLayout.HORIZONTAL);
        int r = 0, id = 100;
        Set<String> keys = map.keySet();

            for(String key: keys) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(r + id);
                ids.add(r + id);
                rdbtn.setText(key);
                rdbtn.setOnClickListener(this);

                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 15, 15, 15);
                rdbtn.setLayoutParams(params);
                rdbtn.setPadding(20, 0, 20, 0);
                radioGrp.addView(rdbtn);
                r++;

        }

        RadioButton btn = findViewById(ids.get(0));
        btn.setChecked(true);
        variationValue =   map.get(btn.getText());
        productPrice.setText("KD " + String.valueOf(variationValue));
        System.out.println(variationValue + "variationValue");
        refreshTotalValue();

    }

    @Override
    public void onClick(View v) {
        variationValue =   mapForGetValues.get(((RadioButton) v).getText());

        //pass value to cart when click
        variations = ((RadioButton) v).getText().toString();
        System.out.println(((RadioButton) v).getText().toString());


        productPrice.setText("KD " +String.valueOf(variationValue));
        System.out.println(variationValue + "variationValue on Click");
         refreshTotalValue();
//        Intent intent = new Intent();
//        intent.setAction("REFRESH");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public void sendAddonValue(Double value) {
        addonsValue = value;
        refreshTotalValue();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (intent.getAction().equals("REFRESH")){
                refreshTotalValue();
            }
        }
    };

    private void refreshTotalValue() {
        double total = (variationValue + addonsValue) * Double.valueOf(resultCart.getText().toString()) ;
        System.out.println("Ingatha Add"+ (variationValue + addonsValue) * Double.valueOf(resultCart.getText().toString()));
        totalOfProduct.setText("Total KD " + String.valueOf(total));
        totalPriceToCart = Float.valueOf(String.valueOf(variationValue + addonsValue));
    }

    @Override
    public void sendAddonsKey(List<String> keys) {
        addons.addAll(keys);
    }

    private void checkCartData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("TAG", "[]");
        Type type = new TypeToken<List<Items>>() {}.getType();
        ArrayList<Items> arrayList = gson.fromJson(json, type);
        if(!arrayList.isEmpty()){
            System.out.println("Working....");
            try {
                resId = arrayList.get(0).getBid();
                System.out.println(arrayList.get(0).getBid());
            }catch (Exception e){
                e.printStackTrace();
            }


        }else {
            resId = "null";
        }

    }
}