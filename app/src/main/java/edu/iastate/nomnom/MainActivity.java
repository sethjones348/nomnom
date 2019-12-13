package edu.iastate.nomnom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String TAG = " ";

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;

    private boolean addEvent;

    private LatLng addEventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addEvent = false;

        addEventLocation = null;

        findViewById(R.id.cancel).setVisibility(View.GONE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Don't have permission to use location.");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle_mine));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        double campusLat = 42.025316;
        double campusLong = -93.646408;

        LatLng campus = new LatLng(campusLat, campusLong);

        //zoom the map into campus upon opening
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f)); //Range of 2.0 to 21.0
        mMap.moveCamera(CameraUpdateFactory.newLatLng(campus));


        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null) {

                            // Add a marker at the user's current location
                            double curLat = location.getLatitude();
                            double curLong = location.getLongitude();

                            LatLng curLocation = new LatLng(curLat, curLong);

                            mMap.addMarker(new MarkerOptions().position(curLocation).title("Current Location"));
                        }
                    }

                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Do nothing
            }
        });

    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    public void onAddEventClicked(View view) {
        Button add_event = findViewById(R.id.add_event);
        Button cancel_button = findViewById(R.id.cancel);

        add_event.setVisibility(View.GONE);
        cancel_button.setVisibility(View.VISIBLE);
                
        addEvent = true;
        Toast.makeText(MainActivity.this, "Tap where you want to add an event or cancel", Toast.LENGTH_LONG).show();
    }

    public void onCancelClicked(View view){
        addEvent = false;

        view.setVisibility(View.GONE);
        findViewById(R.id.add_event).setVisibility(View.VISIBLE);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(addEvent) {
            addEventLocation = latLng;
            addEvent = false;

            Intent myIntent = AddEventActivity.createIntent(this.getApplicationContext(), addEventLocation);
            startActivity(myIntent);
        }
    }
}
