package com.archaric.deliverypoint;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.archaric.deliverypoint.CMenu.CMenu;
import com.archaric.deliverypoint.CMenu.CMenuKt;
import com.archaric.deliverypoint.CustomerSupport.CustomerService;
import com.archaric.deliverypoint.Fragments.AddressModel;
import com.archaric.deliverypoint.Fragments.SetPlaceOrderData;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.ChangeAddressAtCart;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.OrderHistory.PlaceOrder;
import com.archaric.deliverypoint.OrderHistory.SendPlaceOrderData;
import com.archaric.deliverypoint.Settings.Settings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import static com.archaric.deliverypoint.Fragments.LocationPicker.ADDRESS_DATA_TO_MAIN_PAGE;
import static com.archaric.deliverypoint.LoginSignUp.SignUp.USER_MODEL;
import static com.archaric.deliverypoint.R.*;

public class MainActivity extends AppCompatActivity implements ChangeDrawerInterface, SendPlaceOrderData {

    private AppBarConfiguration mAppBarConfiguration;
    private AppBarLayout appBarLayout;
    private TextView cursor_address;
    NavController navController;
    DrawerLayout drawer;
    Menu itemMenu;
    OrdersModel ordersModel;
    private BroadcastReceiver addressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String json = intent.getStringExtra("ADDRESS") ;
            Gson gson = new Gson();
            //onBackPressed();
            if(sheet != null){
                sheet.dismiss();
            }

            //String json = sharedPrefs.getString("TAG", "[]");
            Type type = new TypeToken<OrdersModel>() {}.getType();
            OrdersModel address = gson.fromJson(json, type);
            setAddress(address);
        }
    };


    public void setAddress(OrdersModel address){
        cursor_address.setText(getResources().getString(string.delivery_to) + " " + address.getArea());
    }
    ChangeAddressAtCart sheet;
    CMenu cMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = sharedPrefs.getString("Lang", "en");
        System.out.println(json + "HERE");
        if (json.equals("en")){
            setAppLocale("en");
        }else {
            setAppLocale("ar");
        }
        setContentView(layout.activity_main);
        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(id.drawer_layout);
        NavigationView navigationView = findViewById(id.nav_view);
        itemMenu = navigationView.getMenu();




     //   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();

        Intent intent = getIntent();
        ArrayList<AddressModel> addressModels = (ArrayList<AddressModel>) intent.getSerializableExtra(ADDRESS_DATA_TO_MAIN_PAGE);
        if (addressModels != null) {
            Toast.makeText(this, addressModels.get(0).getLocality(), Toast.LENGTH_SHORT).show();
        }




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarLayout = findViewById(id.app_bar);

        appBarLayout.addOnOffsetChangedListener(new AppBArChageListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Intent i = new Intent("APPBAR");
                if(state == State.COLLAPSED){
                    i.putExtra("APPBAR", false);
                }
                if(state == State.EXPANDED){
                    i.putExtra("APPBAR", true);
                }
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
            }
        });

        View hView =  navigationView.getHeaderView(0);

        cursor_address = findViewById(id.cursor_address);

        LocalBroadcastManager.getInstance(this).registerReceiver(addressReceiver, new IntentFilter("ADDRESS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(menuReceiver, new IntentFilter("MENU"));

        String addressStr = Utils.getStoredData(getApplicationContext(), "ADDRESS");

        LinearLayout root = hView.findViewById(id.dcontainer);

        cMenu = new CMenu(getBaseContext(), root, setAppLocale(json));


        View nav_host_fragment = findViewById(id.nav_host_fragment);
        if (nav_host_fragment == null) {
            System.out.println("Here................................................");
        }

        //getSupportFragmentManager().beginTransaction().replace(hView.findViewById(R.id.dcontainer).getId(), new CMenu()).commit();

        if(!addressStr.isEmpty()){
            Gson gson = new Gson();

            //String json = sharedPrefs.getString("TAG", "[]");
            Type type = new TypeToken<OrdersModel>() {}.getType();
            OrdersModel address = gson.fromJson(addressStr, type);

            //itemsArrayList.add(item);
            //json = gson.toJson(itemsArrayList);
            setAddress(address);
        } else {
            cursor_address.setText(getResources().getString(string.select_location));
        }


        cursor_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheet = new ChangeAddressAtCart();
                sheet.show(getSupportFragmentManager(), null);
            }
        });




        //spinner = findViewById(R.id.spinner_address_name);



        //ArrayList<String> typesOf = new ArrayList<>(Arrays.asList("Delivery to Kuwait", "Select Location"));
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,typesOf);
        //spinner.setAdapter(arrayAdapter);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
//                    ((TextView) parent.getChildAt(0)).setTextSize(15);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        

        ImageView nav_user = hView.findViewById(id.settings);
        TextView userName = hView.findViewById(id.userName);
       TextView creditPoints = hView.findViewById(id.creditPoints);
        if (Utils.userData(this) != null) {
            userName.setText(Utils.userData(this).getName());
            creditPoints.setText(String.valueOf(Utils.userData(this).getWallet()));
        }


        nav_user.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Settings.class));
            drawer.closeDrawer(GravityCompat.START);
        });



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                id.nav_home, id.nav_order_history, id.nav_offers, id.nav_contact_us)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


//        Intent intent = getIntent();
//        String arrivalData = intent.getStringExtra("setCart");
//        if (arrivalData != null){
//            if (arrivalData.equals("toCartFragment")){
//                navController.navigate(id.nav_order_history);
//            }
//        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id == R.id.nav_contact_us){
                    startActivity(new Intent(MainActivity.this, CustomerService.class));
                }

                if (id == R.id.nav_logout){
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(USER_MODEL, "");
                    editor.commit();
                    finish();
                    startActivity(getIntent());

                }
                //This is for maintaining the behavior of the Navigation view
                 NavigationUI.onNavDestinationSelected(item,navController);
                //This is for closing the drawer after acting on it
                 drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }





//    private void doThisForCart(Menu menu, NavController navController, DrawerLayout drawer){
//
//        MenuItem itemMenu = menu.findItem(R.id.nav_home);
//        NavigationUI.onNavDestinationSelected(itemMenu,navController);
//        //This is for closing the drawer after acting on it
//        drawer.closeDrawer(GravityCompat.START);
//
//    }
    private void doThisForOrderHistory( NavController navController){

           navController.navigate(id.nav_order_history);

    }

    private void doThisForOffers(NavController navController){

        navController.navigate(id.nav_offers);

    }

    private void doThisForOrderToHome(NavController navController){
        navController.navigate(id.nav_home);
    }

    private void doThisForSetPaymentMethod(NavController navController){
        navController.navigate(id.nav_place_order);
    }

    private void doThisForFiftyPerOffMethod(NavController navController) {
        navController.navigate(id.nav_fifty_off_large);
    }

    private void doThisForResAroundYouLargeMethod(NavController navController) {
        navController.navigate(id.nav_res_around_you_large);
    }

    private void doThisForNewlyJoinedLargeMethod(NavController navController) {
        navController.navigate(id.nav_newly_joined_large);
    }


    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == id.cart) {
            navController.navigate(id.nav_cart);
            //This is for closing the drawer after acting on it
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void DrawerData(String s) {
        if (s.equals("toOrdersHistoryFragment")){
            doThisForOrderHistory(navController);
        }

        if (s.equals("toOffersFragment")){
            doThisForOffers(navController);
        }

        if (s.equals("toHomeFragment")){
            doThisForOrderToHome(navController);
        }

        if (s.equals("setPaymentMethod")){
            doThisForSetPaymentMethod(navController);
        }

        if (s.equals("toFiftyPerOffFragment")){
            doThisForFiftyPerOffMethod(navController);
        }

        if (s.equals("toResAroundYouLargeFragment")){
            doThisForResAroundYouLargeMethod(navController);
        }

        if (s.equals("toResAroundYouLargeFragment")){
            doThisForResAroundYouLargeMethod(navController);
        }

        if (s.equals("toNewlyJoinedLargeFragment")){
            doThisForNewlyJoinedLargeMethod(navController);
        }











    }



    private BroadcastReceiver menuReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cMenu.update();
            int id = CMenuKt.getCURRENT();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == string.contact_us){
                startActivity(new Intent(MainActivity.this, CustomerService.class));
            }
            if(id == string.menu_home){
                navController.navigate(R.id.nav_home);
            }
            if(id == string.order_history){
              //  setView();
                navController.navigate(R.id.nav_order_history);
            }
            if(id == string.offers){
                navController.navigate(R.id.nav_offers);
            }
            if(id == string.share_app){
                navController.navigate(R.id.nav_share_app);
            }

            if (id == string.logout){
                if (Utils.userData(MainActivity.this) != null){
                    if (Utils.userData(MainActivity.this).getId() != null) {

//                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
//                            switch (which){
//                                case DialogInterface.BUTTON_POSITIVE:
//
//                                    break;
//
//                                case DialogInterface.BUTTON_NEGATIVE:
//                                    dialog.dismiss();
//                                    break;
//                            }
//                        };
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setMessage("Are you sure you want to Logout?").setPositiveButton("Yes", dialogClickListener)
//                                .setNegativeButton("No", dialogClickListener).show();


                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString(USER_MODEL, "");
                        editor.commit();
                        finish();
                        startActivity(getIntent());


                    }
                }else {
                    Utils.toast(MainActivity.this,"Need to Login!");
                }


            }
            //This is for maintaining the behavior of the Navigation view

            //NavigationUI.onNavDestinationSelected(item,navController);

            //This is for closing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START);
        }
    };


    private void  setView (){
        itemMenu.add(0, R.string.menu_home, 0, "menu name");
        itemMenu.add(0, string.order_history, 0, "menu name");
//        NavigationView navigationView = findViewById(id.nav_view);
      //  Menu menuDa = navigationView.getMenu();
        MenuItem itemMenu1 = itemMenu.findItem(string.order_history);
        NavigationUI.onNavDestinationSelected(itemMenu1,navController);
    }


    @Override
    public void toPlaceOrderData(String s, OrdersModel ordersModel) {
        if (s.equals("setPaymentMethod")) {
            this.ordersModel = ordersModel;
            SetPlaceOrderData.setOrdersModel(this.ordersModel);
            doThisForSetPaymentMethod(navController);
        }
    }

    public Resources setAppLocale(String localeKey){
        Resources resources = getBaseContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            conf.setLocale(new Locale(localeKey.toLowerCase()));
        }else {
            conf.locale = new Locale(localeKey.toLowerCase());
        }
        resources.updateConfiguration(conf, dm);
        return  resources;
    }
}