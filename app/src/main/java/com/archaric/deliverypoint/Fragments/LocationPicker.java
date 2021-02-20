package com.archaric.deliverypoint.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.MainActivity;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Settings.ShareLocationAddress;
import com.archaric.deliverypoint.Utils;
import com.archaric.deliverypoint.ZoneModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Mapset.REQUEST_CODE_PERMISSIONS;
import static com.archaric.deliverypoint.Mapset.ZONE_ID;


public class LocationPicker extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback, com.google.android.gms.location.LocationListener {

    public static final String ADDRESS_DATA_TO_MAIN_PAGE = "AddressToMainPage" ;
    MapView mapView;
    GoogleMap mMap;
    Geocoder geo;
    Marker marker;
    private LinearLayout getMyCurrentLocation;
    RelativeLayout parentRelative;
    private ImageView imgMyLocation;
    private ImageButton back;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    TextView addressOfTheLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    ArrayList<AddressModel> addressModels;
    ShareLocationAddress shareLocationAddress;
    String s = "", zoneId;
    AutoCompleteTextView searchBar;
    ImageView close_search;
    ArrayAdapter<Address> addressArrayAdapter;
    ArrayList<Address> addressArrayList = new ArrayList<>();

    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

   public LocationPicker(ShareLocationAddress shareLocationAddress){
        this.shareLocationAddress = shareLocationAddress;
    }

    public LocationPicker(String s){
       this.s = s;
    }




    Double latt, lngg;

    public LocationPicker() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_picker, container, false);

        searchBar = view.findViewById(R.id.search_bar);
        close_search = view.findViewById(R.id.close_search);
        back = view.findViewById(R.id.close);
        mapView = view.findViewById(R.id.mapView);
        getMyCurrentLocation = view.findViewById(R.id.getMyCurrentLocation);
        imgMyLocation = view.findViewById(R.id.imgMyLocation);
        parentRelative = view.findViewById(R.id.parentRelative);
        addressOfTheLocation = view.findViewById(R.id.addressOfTheLocation);
        MapsInitializer.initialize(getActivity());
        Places.initialize(getActivity(),"AIzaSyBHBcHtcwhD1r-nE_BbnsJdpMIle6HtmVU");



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        checkLocationPermission();



        getZoneData();

        boolean background = false;
//        if (background) {
//             //   handleLocationUpdates();
//            } else {
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
//                return;
//            }

        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                searchBar.clearFocus();
            }
        });

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    back.setVisibility(View.VISIBLE);
                } else {
                    back.setVisibility(View.GONE);
                }
            }
        });

        placesClient = Places.createClient(getActivity());
        final AutocompleteSessionToken autocompleteSessionToken = AutocompleteSessionToken.newInstance();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s.toString())) {
                    close_search.setVisibility(View.VISIBLE);
                    searchLocation(s.toString());
                }


                if (TextUtils.isEmpty(s.toString())) {
                    close_search.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getMyCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println(addressModels.get(0).getCity());
                if (s.equals("GotoMain")){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(ADDRESS_DATA_TO_MAIN_PAGE,addressModels);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else if(s.equals("")){
                    getActivity().getSupportFragmentManager().beginTransaction().
                            remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.setLocationPickerFragment)).commit();
                    shareLocationAddress.locationDetails(addressModels);
                }
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                UiSettings uiSettings = mMap.getUiSettings();
//                uiSettings.setMyLocationButtonEnabled(true);
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    return;
//                }
//                mMap.setMyLocationEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        buildGoogleApiClient();
                     //   mMap.setMyLocationEnabled(true);


                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                } else {
                        buildGoogleApiClient();
                      //  mMap.setMyLocationEnabled(true);
                }


                if (mMap != null) {

                    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

                    try {
                        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);


                        Task location1 = mFusedLocationProviderClient.getLastLocation();
                        location1.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Location currentLocation = (Location) task.getResult();

                                try {
                                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                    if (geo == null)
                                        geo = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                    if (address.size() > 0) {
                                        if (marker != null){
                                            marker.remove();
                                        }
                                        marker =
                                                mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                         addressOfTheLocation.setText(
                                                   address.get(0).getFeatureName()
                                                + ", " + address.get(0).getSubLocality()
                                                           + ", " + address.get(0).getLocality()
                                                          // + "" + address.get(0).getAddressLine(0)
                                                           + ", "  + address.get(0).getAdminArea()
                                                           + ", " + address.get(0).getPostalCode());
                                        addressModels = new ArrayList<>();
                                        addressModels.add( new AddressModel(address.get(0).getFeatureName(),address.get(0).getSubLocality(),address.get(0).getLocality()
                                         ,address.get(0).getAdminArea(),address.get(0).getPostalCode(),address.get(0).getAddressLine(0),latLng.latitude,latLng.longitude));

                                         imgMyLocation.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (marker != null){
                                                     marker.remove();
                                                 }
                                                 marker =
                                                         mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                                                 mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                                 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                                 addressOfTheLocation.setText(
                                                         address.get(0).getFeatureName()
                                                                 + ", " + address.get(0).getSubLocality()
                                                                 + ", " + address.get(0).getLocality()
                                                                 // + "" + address.get(0).getAddressLine(0)
                                                                 + ", "  + address.get(0).getAdminArea()
                                                                 + ", " + address.get(0).getPostalCode());

                                                 addressModels = new ArrayList<>();
                                                 addressModels.add( new AddressModel(address.get(0).getFeatureName(),address.get(0).getSubLocality(),address.get(0).getLocality()
                                                         ,address.get(0).getAdminArea(),address.get(0).getPostalCode(),address.get(0).getAddressLine(0),latLng.latitude,latLng.longitude));
                                             }
                                         });
                                    }
                                } catch (IOException ex) {
                                    if (ex != null)
                                        Toast.makeText(getActivity(), "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } catch (SecurityException e) {
                        Toast.makeText(getActivity(), "+SecurityException", Toast.LENGTH_SHORT).show();
                    }


                }



                if (mMap != null) {
                    geo = new Geocoder(getActivity(), Locale.getDefault());

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            try {
                                if (geo == null)
                                    geo = new Geocoder(getActivity(), Locale.getDefault());
                                List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                if (address.size() > 0) {
                                    if (marker != null){
                                        marker.remove();
                                    }
                                    marker =
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(address.get(0).getCountryName()
                                            + address.get(0).getAddressLine(0)));
                                    addressOfTheLocation.setText(
                                            address.get(0).getFeatureName()
                                                    + ", " + address.get(0).getSubLocality()
                                                    + ", " + address.get(0).getLocality()
                                                    // + "" + address.get(0).getAddressLine(0)
                                                    + ", "  + address.get(0).getAdminArea()
                                                    + ", " + address.get(0).getPostalCode());

                                    addressModels = new ArrayList<>();
                                    addressModels.add( new AddressModel(address.get(0).getFeatureName(),address.get(0).getSubLocality(),address.get(0).getLocality()
                                            ,address.get(0).getAdminArea(),address.get(0).getPostalCode(),address.get(0).getAddressLine(0),latLng.latitude,latLng.longitude));

                                }
                            } catch (IOException ex) {
                                if (ex != null)
                                    Toast.makeText(getActivity(), "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            double lat =marker.getPosition().latitude;
                            double lng =marker.getPosition().longitude;

                            latt = lat;
                            lngg = lng;

                            return false;
                        }
                    });
                }
            }
        });

       // System.out.println(latt + lngg);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void showSnackBar() {
        Snackbar.make(parentRelative, "This Application needs Location Permission", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).show();

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showSnackBar();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        getActivity().finish();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationPermission();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void getZoneData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getCustomerZone(13.119897,80.265283).enqueue(new Callback<ZoneModel>() {
            @Override
            public void onResponse(Call<ZoneModel> call, Response<ZoneModel> response) {

                if (response.isSuccessful()) {
                    ZoneModel zoneModel = response.body();
                    if (zoneModel != null)
                        zoneId = zoneModel.getZone();
                    try {
                        System.out.println(zoneId + " ZoneId " + zoneModel.getSubzone().toString() + " SubZone");
                        Utils.storeData(getActivity(),zoneId,ZONE_ID);
                        System.out.println(Utils.getStoredData(getActivity(),ZONE_ID));
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ZoneModel> call, Throwable t) {

            }
        });
    }

    public void searchLocation(String s) {
        String location = s.toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 10);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size() != 0){
                System.out.println(addressList);
                addressArrayList.addAll(addressList);
                addressArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1,addressArrayList);
                searchBar.setAdapter(addressArrayAdapter);

                if (marker != null){
                    marker.remove();
                }
                Address address = addressList.get(0);


                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                addressOfTheLocation.setText(
                        address.getFeatureName()
                                + ", " + address.getSubLocality()
                                + ", " + address.getLocality()
                                // + "" + address.get(0).getAddressLine(0)
                                + ", "  + address.getAdminArea()
                                + ", " + address.getPostalCode());
                addressModels = new ArrayList<>();
                addressModels.add( new AddressModel(address.getFeatureName(),address.getSubLocality(),address.getLocality()
                        ,address.getAdminArea(),address.getPostalCode(),address.getAddressLine(0),latLng.latitude,latLng.longitude));

              //  Toast.makeText(getActivity(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
            }

        }
    }


}