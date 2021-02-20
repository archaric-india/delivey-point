package com.archaric.deliverypoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.archaric.deliverypoint.CMenu.CMenu;
import com.archaric.deliverypoint.CMenu.CMenuKt;
import com.archaric.deliverypoint.CustomerSupport.CustomerService;
import com.archaric.deliverypoint.IndividualRestaurant.Items;
import com.archaric.deliverypoint.LoginSignUp.UserModel;
import com.archaric.deliverypoint.OrderHistory.ChangeAddressAtCart;
import com.archaric.deliverypoint.OrderHistory.OrdersModel;
import com.archaric.deliverypoint.Settings.Settings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.archaric.deliverypoint.LoginSignUp.SignUp.USER_MODEL;

public class MainActivity extends AppCompatActivity implements ChangeDrawerInterface {

    private AppBarConfiguration mAppBarConfiguration;
    private AppBarLayout appBarLayout;
    private TextView cursor_address;
    NavController navController;
    DrawerLayout drawer;
    Menu itemMenu;
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

    private BroadcastReceiver menuReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int id = CMenuKt.getCURRENT();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.string.contact_us){
                startActivity(new Intent(MainActivity.this, CustomerService.class));
            }
            if(id == R.string.menu_home){
                navController.navigate(R.id.nav_home);
            }
            if(id == R.string.order_history){
                navController.navigate(R.id.nav_order_history);
            }
            if(id == R.string.offers){
                navController.navigate(R.id.nav_offers);
            }
            if(id == R.string.share_app){

                /*Create an ACTION_SEND Intent*/
                Intent it = new Intent(Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = "Download Delivery Point";
                /*The type of the content is text, obviously.*/
                it.setType("text/plain");
                /*Applying information Subject and Body.*/
                it.putExtra(android.content.Intent.EXTRA_SUBJECT, "Delivery Point");
                it.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "Share Using"));
            }

            if (id == R.string.customer_support){
                startActivity(new Intent(getApplicationContext(),CustomerService.class));
                CMenuKt.setCURRENT(R.string.menu_home);
            }

            if (id == R.string.logout){
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(USER_MODEL, "");
                editor.commit();
                finish();
                startActivity(getIntent());

            }
            cMenu.update();
            //This is for maintaining the behavior of the Navigation view

            //NavigationUI.onNavDestinationSelected(item,navController);

            //This is for closing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START);
        }
    };

    public void setAddress(OrdersModel address){
        cursor_address.setText(getResources().getString(R.string.delivery_to) + " " + address.getArea());
    }
    ChangeAddressAtCart sheet;
    CMenu cMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        itemMenu = navigationView.getMenu();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarLayout = findViewById(R.id.app_bar);

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

        cursor_address = findViewById(R.id.cursor_address);

        LocalBroadcastManager.getInstance(this).registerReceiver(addressReceiver, new IntentFilter("ADDRESS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(menuReceiver, new IntentFilter("MENU"));

        String addressStr = Utils.getStoredData(getApplicationContext(), "ADDRESS");

        LinearLayout root = hView.findViewById(R.id.dcontainer);

        cMenu = new CMenu(getApplicationContext(), root);

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
            cursor_address.setText(getResources().getString(R.string.select_location));
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

        

        ImageView nav_user = hView.findViewById(R.id.settings);
        TextView userName = hView.findViewById(R.id.userName);
        if (Utils.userData(this)!= null) {
            userName.setText(Utils.userData(this).getName());
        }


        nav_user.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Settings.class));
            drawer.closeDrawer(GravityCompat.START);
        });



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_order_history, R.id.nav_offers, R.id.nav_contact_us)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setBackStackChangeListener();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


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

    public void setBackStackChangeListener(){
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_cart){
                    //appBarLayout.setExpanded(false,false);

                }else{
                    appBarLayout.setExpanded(true,true);
                }

                switch (destination.getId()){
                    case R.id.nav_home:
                        CMenuKt.setCURRENT(R.string.menu_home);
                        break;
                    case R.id.nav_offers:
                        CMenuKt.setCURRENT(R.string.offers);
                        break;
                    case R.id.nav_order_history:
                        CMenuKt.setCURRENT(R.string.order_history);
                        break;
                    default:
                        CMenuKt.setCURRENT(destination.getId());
                }
                cMenu.update();
            }
        });

    }

    private void doThisForOrderHistory(Menu menu, NavController navController, DrawerLayout drawer){
        MenuItem itemMenu = menu.findItem(R.id.nav_order_history);
        NavigationUI.onNavDestinationSelected(itemMenu,navController);
        //This is for closing the drawer after acting on it
        drawer.closeDrawer(GravityCompat.START);

    }

    private void doThisForOffers(Menu menu, NavController navController, DrawerLayout drawer){
        MenuItem itemMenu = menu.findItem(R.id.nav_offers);
        NavigationUI.onNavDestinationSelected(itemMenu,navController);
        //This is for closing the drawer after acting on it
        drawer.closeDrawer(GravityCompat.START);

    }

    private void doThisForOrderToHome(Menu menu, NavController navController, DrawerLayout drawer){
        MenuItem itemMenu = menu.findItem(R.id.nav_home);
        NavigationUI.onNavDestinationSelected(itemMenu,navController);
        //This is for closing the drawer after acting on it
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            navController.navigate(R.id.nav_cart);
            //This is for closing the drawer after acting on it
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void DrawerData(String s) {
        if (s.equals("toOrdersHistoryFragment")){
            doThisForOrderHistory(itemMenu,navController,drawer);
        }

        if (s.equals("toOffersFragment")){
            doThisForOffers(itemMenu,navController,drawer);
        }

        if (s.equals("toHomeFragment")){
            doThisForOrderToHome(itemMenu,navController,drawer);
        }

        if(s.equals("SEARCH")){
            navController.navigate(R.id.nav_search);
        }
    }






}