package com.archaric.deliverypoint.OrderHistory;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.archaric.deliverypoint.EndPoint;
import com.archaric.deliverypoint.Fragments.AllCategoriesModel;
import com.archaric.deliverypoint.Fragments.SetPlaceOrderData;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.Utils;
import com.archaric.deliverypoint.location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.archaric.deliverypoint.Mapset.ZONE_ID;

public class MapsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback, com.google.android.gms.location.LocationListener {

    GoogleMap mGoogleMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    RelativeLayout parentRelative;

    MarkerOptions origin, destination;

    OrdersModel ordersModel;

    location deliveryBoyLocation;

    Marker markerDeliveryBoy;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted


                } else {
                    //Request Location Permission
                    checkLocationPermission();
                }
            } else {

            }


            if (mGoogleMap != null) {

                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
                mGoogleMap.getUiSettings().setCompassEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


                try {

                    Task location1 = mFusedLocationProviderClient.getLastLocation();
                    location1.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();


                            //  LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            LatLng latLng = new LatLng(ordersModel.getLocation().getLat(), ordersModel.getLocation().getLat());

                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here")
                                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_customer_location));
                            mGoogleMap.addMarker(markerOptions);


                            LatLng latLngOfRes = new LatLng(13.119298987828284, 80.29313532690344);
                            MarkerOptions markerOptionsOfRes = new MarkerOptions().position(latLngOfRes).title("Restaurant")
                                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_restaurant_location));
                            mGoogleMap.addMarker(markerOptionsOfRes);


                            LatLng latLngOfDBoy = new LatLng(13.122189, 80.293954);
                            MarkerOptions markerOptionsOfDBoy = new MarkerOptions().position(latLngOfDBoy).title("Delivery Boy Here")
                                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_bike));
                            markerDeliveryBoy = mGoogleMap.addMarker(markerOptionsOfDBoy);
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngOfDBoy));
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngOfDBoy, 16));

                            //Setting marker to draw route between these two points
                            origin = new MarkerOptions().position(latLng).title("Customer").snippet("origin");
                            destination = new MarkerOptions().position(latLngOfDBoy).title("Delivery Boy").snippet("destination");

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);


                            origin = new MarkerOptions().position(latLngOfRes).title("Restaurant").snippet("origin");
                            destination = new MarkerOptions().position(latLngOfDBoy).title("Delivery Boy").snippet("destination");

                            // Getting URL to the Google Directions API
                            String url1 = getDirectionsUrl(origin.getPosition(), destination.getPosition());

                            DownloadTask2 downloadTask2 = new DownloadTask2();

                            // Start downloading json data from Google Directions API
                            downloadTask2.execute(url1);


                        } else {
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (SecurityException e) {
                    Toast.makeText(getActivity(), "+SecurityException", Toast.LENGTH_SHORT).show();
                }


            }
        }
    };

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, 0, 0);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        parentRelative = view.findViewById(R.id.parentRelative);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        ordersModel = SetPlaceOrderData.getOrdersModel();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

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

    @Override
    public void onLocationChanged(@NonNull Location location) {



    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Setting mode
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDG0LlOYxZ1WTHdqMjWQVfTNznfO61vd5E";

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = getDefaultPolyLines(points);

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map

            if (points.size() != 0)
                mGoogleMap.addPolyline(lineOptions);
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask22 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.GREEN);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map

            if (points.size() != 0)
                mGoogleMap.addPolyline(lineOptions);
        }
    }

    private class DownloadTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask22 parserTask22 = new ParserTask22();
            parserTask22.execute(result);
        }
    }

    private void deliveryBoyLocation(OrdersModel ordersModel) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://delivery-8a843.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String zoneId = Utils.getStoredData(getActivity(), ZONE_ID);
        System.out.println(zoneId + "Zone Id at Special Offers");

        EndPoint endPoint = retrofit.create(EndPoint.class);
        endPoint.getDeliveryDetails(ordersModel.getAssignedto()).enqueue(new Callback<DeliveryModel>() {
            @Override
            public void onResponse(Call<DeliveryModel> call, Response<DeliveryModel> response) {
                if (response.isSuccessful()) {
                    DeliveryModel deliveryModel = response.body();
                    if (deliveryModel != null) {
                        deliveryBoyLocation.setLat(deliveryModel.getLocation().getLat());
                        deliveryBoyLocation.setLong(deliveryModel.getLocation().getLong());
                        deliveryBoyLocation.setRot(deliveryModel.getLocation().getRot());
                        System.out.println(deliveryBoyLocation);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryModel> call, Throwable t) {

            }
        });
    }

    private void updateMarker(LatLng latLang, Float rotation) {

        markerDeliveryBoy.setRotation(rotation);
        MarkerAnimation.animateMarker(markerDeliveryBoy, latLang, new LatLngInterpolator.Spherical());

//        Location prevLoc = ... ;
//        Location newLoc = ... ;
       // float bearing = prevLoc.bearingTo(newLoc) ;


//            //Log.e("INCOMING", polyline?.points!!.toString())
//            var points = polyline?.points!!
//
//                    points.reverse()
//
//            var i = -1.0f
//
//            var closestLatLng: LatLng = points.last()
//
//            points.forEach {
//                var d: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)
//                Location.distanceBetween(latLang.latitude, latLang.longitude, it.latitude, it.longitude, d)
//                var distance = d[0]
//
//                if(i == -1.0f || distance < i){
//                    closestLatLng = it
//                    i = distance
//                }
//            }
//
//            var index = points.indexOf(closestLatLng) + 1
//
//            Log.e("INDEX", index.toString())
//
//            try{
//                while (points[index] != null){
//                    points.removeAt(index)
//                }
//            } catch (e: Exception){ }
//
//            points.add(latLang)
//            points.reverse()
//            polyline?.points = points
//
//            polyline?.remove()
//            PolylineOptions polylineOptions = getDefaultPolyLines();
//            polyline = map.addPolyline(polylineOptions)
//
    }

    public static PolylineOptions getDefaultPolyLines(List<LatLng> points) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.rgb(198,41,225))
                .width(5.2f);
        for (LatLng point : points) polylineOptions.add(point);
        return polylineOptions;
    }

}