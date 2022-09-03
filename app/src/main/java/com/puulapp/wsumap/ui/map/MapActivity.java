package com.puulapp.wsumap.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.puulapp.wsumap.DetailView;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.database.DBAdapter;
import com.puulapp.wsumap.database.SaveModel;
import com.puulapp.wsumap.databinding.ActivityMapBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private FloatingActionMenu fabMenu1;
    private com.github.clans.fab.FloatingActionButton fab_route1;
    private com.github.clans.fab.FloatingActionButton fab_share1;
    private com.github.clans.fab.FloatingActionButton fab_save1;
    private com.github.clans.fab.FloatingActionButton fab_info;
    private com.github.clans.fab.FloatingActionButton fab_view;
    private com.github.clans.fab.FloatingActionButton fab_my_loc;
    private com.github.clans.fab.FloatingActionButton fab_gandaba;
    private com.github.clans.fab.FloatingActionButton fab_otona;

    private List<Marker> markerList;
    private LatLng latLng;
    private MarkerOptions markerOptions;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private Location myLocation;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private double currentLatitude, currentLongitude;
    private LatLng myCoordinates;
    private boolean mapAnimated = false;
    private Marker myMarker;
    private static int  MAP_TYPE = GoogleMap.MAP_TYPE_SATELLITE;
    private double selected_lat, selected_lng, passed_latitude = -34, passed_longitude = 151;
    private String selected_name, selected_desc, selected_image, selected_campus, selected_key, passed_key;
    private boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            passed_latitude = Double.parseDouble(bundle.getString("lat"));
            passed_longitude = Double.parseDouble(bundle.getString("lng"));
            passed_key = bundle.getString("key");
        }

        initiate();
        action();

    }

    private void initiate() {

        fab_share1 = findViewById(R.id.fab_share1);
        fab_save1 = findViewById(R.id.fab_save1);
        fab_route1 = findViewById(R.id.fab_route1);
        fab_info = findViewById(R.id.fab_info);
        fabMenu1 = findViewById(R.id.fabMenu1);
        fab_my_loc = findViewById(R.id.fab_my_loc);
        fab_view = findViewById(R.id.fab_view);
        fab_gandaba = findViewById(R.id.fab_gandaba);
        fab_otona = findViewById(R.id.fab_otona);

        markerList = new ArrayList<>();
        buildLocationResult();
        setUpGClient();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    private void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void layerAlertDialog() {
        CharSequence[] choose = new CharSequence[]{"DEFAULT","SATELLITE"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Map type");
        builder.setItems(choose, (dialog, which) -> {
            if ("DEFAULT".contentEquals(choose[which])){
                MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
            }else if ("SATELLITE".contentEquals(choose[which])){
                MAP_TYPE = GoogleMap.MAP_TYPE_SATELLITE;
            }
            mMap.setMapType(MAP_TYPE);
        });
        builder.show();
    }

    private void action() {

        fab_route1.setOnClickListener(v -> {
            if (selected) {
                Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + myCoordinates.latitude + "," + myCoordinates.longitude + "&daddr=" +  selected_lat + "," + selected_lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Please select a marker to navigate", Toast.LENGTH_SHORT).show();
            }

            fabMenu1.close(true);
        });
        fab_save1.setOnClickListener(v -> {
            if (selected){
                if (new DBAdapter(this).check_exist(selected_key)) {
                    Toast.makeText(this, "Already Saved!", Toast.LENGTH_SHORT).show();
                } else {
                    SaveModel saveModel = new SaveModel(0, selected_image, selected_name, selected_desc, String.valueOf(selected_lat), String.valueOf(selected_lng), selected_key, selected_campus);
                    new DBAdapter(this).addNewItem(saveModel);
                }
            } else {
                Toast.makeText(this, "Please select a marker to save", Toast.LENGTH_SHORT).show();
            }
            fabMenu1.close(true);
        });
        fab_share1.setOnClickListener(v -> {
            if (selected) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = selected_image + "\n\n" + selected_desc + "\n\n" + "http://maps.google.com/maps?saddr=" +  selected_lat + "," + selected_lng;
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, selected_name + "(" + selected_campus + " Campus)");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } else {
                Toast.makeText(this, "Please select a marker to share", Toast.LENGTH_SHORT).show();
            }
            fabMenu1.close(true);
        });
        fab_info.setOnClickListener(v -> {
            if (selected) {
                Intent intent = new Intent(MapActivity.this, DetailView.class);
                intent.putExtra("key", selected_key);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a marker to view info", Toast.LENGTH_SHORT).show();
            }

            fabMenu1.close(true);
        });
        fab_view.setOnClickListener(v -> {
            layerAlertDialog();
            fabMenu1.close(true);
        });
        fab_my_loc.setOnClickListener(v -> {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 17f));
            fabMenu1.close(true);
        });
        fab_gandaba.setOnClickListener(v -> {
            LatLng latLng1 = new LatLng(Double.parseDouble("6.829969159424438"), Double.parseDouble("37.75164882536292"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17f));
            fabMenu1.close(true);
        });
        fab_otona.setOnClickListener(v -> {
            LatLng latLng1 = new LatLng(Double.parseDouble("6.860148693855833"), Double.parseDouble("37.7793925050146"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17f));
            fabMenu1.close(true);
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(passed_latitude, passed_longitude), 17.0f));
        FirebaseDatabase.getInstance().getReference()
                .child("items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String lat = "0", lng = "0", title = "";
                    if (snapshot.child("lat").getValue() != null) {
                        lat = snapshot.child("lat").getValue().toString();
                    }
                    double latitude = Double.parseDouble(lat);
                    if (snapshot.child("lng").getValue() != null) {
                        lng = snapshot.child("lng").getValue().toString();
                    }
                    double longitude = Double.parseDouble(lng);
                    if (snapshot.child("name").getValue() != null) {
                        title = snapshot.child("name").getValue().toString();
                    }

                    latLng = new LatLng(latitude, longitude);

                    markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pngfind", 40, 55))).title(title);

                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(String.valueOf(snapshot.getKey()));
                    markerList.add(marker);
                        LatLng ll = new LatLng(passed_latitude, passed_longitude);
                            if (marker.getPosition().equals(ll)){
                                marker.showInfoWindow();
                                if (!passed_key.equals("No")) {
                                    retrieveData(passed_key);
                                    selected_key = passed_key;
                                }
                            }
                    }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mMap.setOnMarkerClickListener(marker -> {
            if (myMarker != null && myMarker.equals(marker)) {
                selected = false;
            }else {

                if (marker.getTag() != null){
                    selected_lat = marker.getPosition().latitude;
                    selected_lng = marker.getPosition().longitude;
                    String key = String.valueOf(marker.getTag());
                    selected_key = key;
                    retrieveData(key);
                }

            }
            return false;
        });

    }

    private void retrieveData(String key) {
        FirebaseDatabase.getInstance().getReference()
                .child("items").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            selected_campus = snapshot.child("campus").getValue().toString();
                            selected_desc = snapshot.child("desc").getValue().toString();
                            selected_image = snapshot.child("photo").getValue().toString();
                            selected_name = snapshot.child("name").getValue().toString();

                            selected = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
        checkPermission();
    }

    private void checkPermission() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissions = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissions.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissions.toArray(new String[listPermissions.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }
        else{
            getMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MapActivity.this, "Please grant location permission to access map", Toast.LENGTH_LONG).show();
        } else {
            checkPermission();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        myLocation = location;

        if (myLocation != null){

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            myCoordinates = new LatLng(currentLatitude, currentLongitude);

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.street_view);

            if (myMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(currentLatitude, currentLongitude));
                markerOptions.title("You");
                markerOptions.icon(icon);
                myMarker = mMap.addMarker(markerOptions);
            } else{
                try {
                    MarkerAnimation.animateMarkerToICS(myMarker, new LatLng(currentLatitude, currentLongitude), new LatLngInterpolator.Spherical());
                }catch (Exception ex){
                    Log.d("locationChange : ", ""+ex.getMessage());
                }

            }
        }

    }

    private void buildLocationResult() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(1000);
                    locationRequest.setFastestInterval(1000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this::onLocationChanged);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(result1 -> {
                        final Status status = result1.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                int permissionLocation1 = ContextCompat
                                        .checkSelfPermission(MapActivity.this,
                                                Manifest.permission.ACCESS_FINE_LOCATION);
                                if (permissionLocation1 == PackageManager.PERMISSION_GRANTED) {

                                    myLocation = LocationServices.FusedLocationApi
                                            .getLastLocation(googleApiClient);

                                }
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(MapActivity.this,
                                            REQUEST_CHECK_SETTINGS_GPS);


                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }


                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied.
                                // However, we have no way
                                // to fix the
                                // settings so we won't show the dialog.
                                // finish();
                                break;

                        }
                    });

                }

            }
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}