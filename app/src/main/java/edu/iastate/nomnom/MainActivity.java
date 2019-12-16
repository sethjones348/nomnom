package edu.iastate.nomnom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, Observer<ArrayList<Event>> {

    private static final String TAG = " ";

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;

    private boolean addEvent;

    private LatLng addEventLocation;

    private EventList eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventList = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(EventList.class);

        eventList.eventList.observe(this, this);

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
        mMap.setOnInfoWindowClickListener(this);

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

        /* ToDo FireBaseStuff - Zach attack
        update the eventList object here
        and then delete the test cases when you are done
         */

        ArrayList<Event> events = new ArrayList<>();

        LatLng event1Loc = new LatLng(42.0271229, -93.6428123);

        LatLng event2Loc = new LatLng(42.0254624, -93.6497928);

        LatLng event3Loc = new LatLng(42.0293523, -93.6497287);

        Event event1 = new Event("Homecoming Week", "Chic fil a", event1Loc,"Outside of the library", "10:00 am", "12:00 pmm");
        Event event2 = new Event("senior Week","Chic fil a", event2Loc,"Outside of the library", "10:00 am", "12:00 pmm");
        Event event3 = new Event("yeee Week","Chic fil a", event3Loc,"Outside of the library", "10:00 am", "12:00 pmm");

        event1.setEventId(0);
        event2.setEventId(1);
        event3.setEventId(2);

        events.add(event1);
        events.add(event2);
        events.add(event3);

        eventList.eventList.setValue(events);
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

    private void placeMarkers(){
        for(final Event e: eventList.eventList.getValue()){
            mMap.addMarker(new MarkerOptions().position(e.getLocation()).title(e.getTitle() + ": " + e.getFood())).setTag(e.getEventId());
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = EventDetailsActivity.createIntent(this.getApplicationContext(), (int) marker.getTag());

        startActivity(intent);
    }

    @Override
    public void onChanged(ArrayList<Event> events) {
        if(mMap != null) {
            placeMarkers();
        }
    }
}
