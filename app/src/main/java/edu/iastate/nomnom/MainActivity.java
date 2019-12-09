package edu.iastate.nomnom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = " ";

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null) {
                            // Add a marker in ames and move the camera
                            double amesLat = 42.025316;
                            double amesLong = -93.646408;

                            LatLng ames = new LatLng(amesLat, amesLong);

                            double curLat = location.getLatitude();
                            double curLong = location.getLongitude();

                            LatLng curLocation = new LatLng(curLat, curLong);

                            mMap.addMarker(new MarkerOptions().position(curLocation).title("Current Location"));

                            mMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f)); //Range of 2.0 to 21.0

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(ames));

                            //Location.distanceBetween(curLat,curLong,bloomingtonAddress.get(0).getLatitude(),bloomingtonAddress.get(0).getLongitude(),distance);

                            //Toast.makeText(MapsActivity.this, "Distance from Bloomington, Indiana is " + distance[0]/1609.34 + " miles", Toast.LENGTH_LONG).show();
                        }
                    }

                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Add a marker in ames and move the camera
                double amesLat = 42.025316;
                double amesLong = -93.646408;

                LatLng ames = new LatLng(amesLat, amesLong);

                mMap.addMarker(new MarkerOptions().position(ames).title("Ames"));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(12.0f)); //Range of 2.0 to 21.0
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ames));
            }
        });

    }
}
