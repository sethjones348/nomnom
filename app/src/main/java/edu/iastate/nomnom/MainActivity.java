package edu.iastate.nomnom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, Observer<ArrayList<Event>> {

    private static final String TAG = " ";

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;

    private boolean addEvent;

    private LatLng addEventLocation;

    private EventList eventList;

    private final FirebaseApp fbApp = FirebaseApp.initializeApp(this);

    private final FirebaseFirestore fb = FirebaseFirestore.getInstance(fbApp);

    final String PREFS_NAME = "appPrefs";

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFirebaseChangeListener();

        db = AppDatabase.getAppDatabase(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        Intent intent = getIntent();

        if (intent != null) {
            DocumentReference newEventRef = fb.collection("events").document();
            String firebaseID = newEventRef.getId();

            if (intent.getBooleanExtra("data_change", false)) {
                //TODO add the new or updated event
                String title = intent.getStringExtra("title");
                String food = intent.getStringExtra("food");
                String deets = intent.getStringExtra("locationDetails");
                String startTime = intent.getStringExtra("startTime");
                String endTime = intent.getStringExtra("endTime");
                double latitude = intent.getDoubleExtra("lat", 0);
                double longitude = intent.getDoubleExtra("long", 0);

                //TODO push to firebase and get firebaseID (I think the code below does this properly)

                final Event newEvent = new Event(firebaseID, title, food, latitude, longitude, deets, startTime, endTime);
                Toast.makeText(this, "ID " + firebaseID, Toast.LENGTH_SHORT).show();

                newEventRef.set(newEvent);
                System.out.println("Data pushed");

                //add to SQLite database
                //db.eventDao().insertEvent(newEvent);
            }
            if(intent.getStringExtra("deletedEvent") != null){
                String deletedEventId = intent.getStringExtra("deletedEvent");
                Event deletedEvent = db.eventDao().findByID(deletedEventId);

                DocumentReference deleteRef = fb.collection("events").document(deletedEventId);
                deleteRef.delete();

                //db.eventDao().delete(deletedEvent);
            }
        }

        if (settings.getBoolean("first_open", true)) {

            updateSQL(firebasePull());

            settings.edit().putBoolean("first_open", false).apply();
        }

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

        refresh();
    }

    private void refresh(){
        eventList.eventList.setValue((ArrayList) db.eventDao().getAll());
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    public static Intent createIntent(Context context, String eventId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("deletedEvent", eventId);
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
            myIntent.putExtra("eventLatitude", addEventLocation.latitude);
            myIntent.putExtra("eventLongitude", addEventLocation.longitude);

            startActivity(myIntent);
        }
    }

    private void placeMarkers(){
        for(final Event e: eventList.eventList.getValue()){
            double latitude = e.getLatitude();
            double longitude = e.getLongitude();
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location).title(e.getTitle() + ": " + e.getFood())).setTag(e.getEventId());
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = EventDetailsActivity.createIntent(this.getApplicationContext(), (String) marker.getTag());

        startActivity(intent);
    }

    @Override
    public void onChanged(ArrayList<Event> events) {
        if(mMap != null) {
            placeMarkers();
        }
    }

    private void updateSQL(ArrayList<Event> events) {
        //TODO put new events in SQLite
        for (Event e : events) {
            if (sqlVersionExists(e.getEventId())) {
                db.eventDao().update(e);
            }
            else {
                db.eventDao().insertEvent(e);
            }
        }
        for (Event e : db.eventDao().getAll()) {
            if (!events.contains(e)) {
                db.eventDao().delete(e);
            }
        }
    }

    private ArrayList<Event> firebasePull() {
        final ArrayList<Event> events = new ArrayList<>();
        fb.collection("some collection").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Event> types = documentSnapshots.toObjects(Event.class);
                            // Add all to your list
                            events.addAll(types);
                            Log.d(TAG, "onSuccess: " + events);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
        return events;
    }

    private void setFirebaseChangeListener() {
            //Everything except the stuff inside the case statements was taken from firebase documentation, so it is probably good.
            //The problem is that the changes seem to be empty
            fb.collection("events")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            System.out.println("onEvent called");
                            if (e != null) {
                                Log.w(TAG, "Data retrieval failed", e);
                                return;
                            }

                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                System.out.println("Case statement: ");
                                System.out.println(dc.getDocument().getData());
                                String title = (String) dc.getDocument().get("title");
                                String food = (String) dc.getDocument().get("food");
                                String deets = (String) dc.getDocument().get("locationDetails");
                                String startTime = (String) dc.getDocument().get("startTime");
                                String endTime = (String) dc.getDocument().get("endTime");
                                double latitude = (double) dc.getDocument().get("latitude");
                                double longitude = (double) dc.getDocument().get("longitude");


                                String firebaseID = dc.getDocument().getId();
                                //StorageReference imgRef = (StorageReference) dc.getDocument().getData().get("imgRef");

                                Event newEvent = new Event(firebaseID, title, food, latitude, longitude, deets, startTime, endTime);

                                System.out.println("Event for live data: " + newEvent.toString());


                                switch (dc.getType()) {
                                    case ADDED:
                                        //eventList.eventList.getValue() will never be null
                                        //ArrayList<Event> newEventList = eventList.eventList.getValue();

                                        //Toast.makeText(getApplicationContext(), "LatitudeFB " + newEvent.getLatitude(), Toast.LENGTH_SHORT).show();

                                        if (!sqlVersionExists(newEvent.getEventId()))
                                            db.eventDao().insertEvent(newEvent);

                                        break;
                                    case MODIFIED:
                                        db.eventDao().update(newEvent);
                                        break;
                                    case REMOVED:
                                        db.eventDao().delete(newEvent);
                                        break;
                                }
                            }

                        }
                    });
    }

    private boolean sqlVersionExists(String id) {
        return db.eventDao().findByID(id) != null;
    }

    public void onRefreshClicked(View view) {
        refresh();
    }
}
