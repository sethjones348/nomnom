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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;

/**
 * The class which represents the main page of the app, it is a map by Google
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener, Observer<ArrayList<Event>> {

    /**
     * Field that keeps track of error messages when methods could be succesful or fail
     */
    private static final String TAG = " ";

    /**
     * A google play services field which allows the app to access the user's location
     */
    private FusedLocationProviderClient mFusedLocationProviderClient;

    /**
     * A Google Map object which allows the main page of NomNom to be a map by Google
     */
    private GoogleMap mMap;

    /**
     * A boolean that keeps track of if the user is trying to add an event or not, if true, then tapping the screen takaes the user to the AddEventActivity
     */
    private boolean addEvent;

    /**
     * A LatLng which contains the location where the user wants to add an event at
     */
    private LatLng addEventLocation;

    /**
     * A ViewModel which contains all of the events that are currently in the SQLite database
     */
    private EventList eventList;

    /**
     * Represents the firebase app that is being utilized
     */
    FirebaseApp fbApp;

    /**
     * Represents the FireStore which is being utilized in the app
     */
    FirebaseFirestore fb;

    /**
     * Represents the firebase storage that is being utilized in the app
     */
    FirebaseStorage storage;

    /**
     * A string which allows the app to utilize shared preferences to see if the user is opening the app for the first time
     */
    final String PREFS_NAME = "appPrefs";

    /**
     * Represents the SQLite database which the users phone will be housing
     */
    private AppDatabase db;

    /**
     * Acts as a constructor for the main activity. It is called everytime the page is visited, and it initializes
     * all of the variables and objects which require initializing.
     *
     * @param savedInstanceState the state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbApp = FirebaseApp.initializeApp(this);

        fb = FirebaseFirestore.getInstance(fbApp);

        storage = FirebaseStorage.getInstance();

        db = AppDatabase.getAppDatabase(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        Intent intent = getIntent();

        if (intent != null) {
            DocumentReference newEventRef = fb.collection("events").document();
            String firebaseID = newEventRef.getId();

            if (intent.getBooleanExtra("data_change", false)) {
                String title = intent.getStringExtra("title");
                String food = intent.getStringExtra("food");
                String deets = intent.getStringExtra("locationDetails");
                String startTime = intent.getStringExtra("startTime");
                String endTime = intent.getStringExtra("endTime");
                double latitude = intent.getDoubleExtra("lat", 0);
                double longitude = intent.getDoubleExtra("long", 0);
                byte[] byteArray = intent.getByteArrayExtra("photo");
                StorageReference imageRef = uploadImage(firebaseID, byteArray);

                final Event newEvent = new Event(firebaseID, title, food, latitude, longitude, deets, startTime, endTime, imageRef.toString());

                System.out.println("Data pushed");
                intent.putExtra("data_change", false);

                //add to SQLite database
                if ((getIntent().getBooleanExtra("isEdit", false))) {
                    String eventId = intent.getStringExtra("eventId");
                    DocumentReference oldEventRef = fb.collection("events").document(eventId);
                    db.eventDao().update(newEvent);
                    oldEventRef.set(newEvent);
                }
                else {
                    newEventRef.set(newEvent);
                    db.eventDao().insertEvent(newEvent);
                }
            }
            if(intent.getStringExtra("deletedEvent") != null){
                String deletedEventId = intent.getStringExtra("deletedEvent");
                Event deletedEvent = db.eventDao().findByID(deletedEventId);

                DocumentReference deleteRef = fb.collection("events").document(deletedEventId);
                deleteRef.delete();

                db.eventDao().delete(deletedEvent);
            }
        }

        if (settings.getBoolean("first_open", true)) {

            firebasePull();
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
        firebasePull();
    }

    /**
     *Called when the map is ready to be used
     * @param googleMap the google map object which will be assigned to the mMap instance variable
     */
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
    }

    private void refresh(){
        removeOutdated();
        eventList.eventList.setValue((ArrayList) db.eventDao().getAll());
    }

    /**
     * Creates an intent which will return the user to the main activity
     * @param context
     * @return returns the intent that needs to be passed in to startActivity as a parameter
     *         from the activity that is being traveled from
     */
    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    /**
     * Creates an intent which contains the ID of the event that is deleted in EventDetailsActivity,
     * it also returns the user to the main page when deleting an event
     * @param context will be the context of the activity being traveled from
     * @param eventId the event ID of the event which was deleted in EventDetailsActivity
     * @return returns the intent which is created
     */
    public static Intent createIntent(Context context, String eventId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("deletedEvent", eventId);
        return intent;
    }

    /**
     * An onClick method for the add_event button. This sets the boolean addEvent flag to true, causing the app to
     * wait for human input on the map, it then displays a toast giving the user instructions on how to add an event
     *
     * It also conceals the add_event button and reveals the cancel button
     * @param view the button which this onClick method is associated with
     */
    public void onAddEventClicked(View view) {
        Button add_event = findViewById(R.id.add_event);
        Button cancel_button = findViewById(R.id.cancel);

        add_event.setVisibility(View.GONE);
        cancel_button.setVisibility(View.VISIBLE);

        addEvent = true;
        Toast.makeText(MainActivity.this, "Tap where you want to add an event or cancel", Toast.LENGTH_LONG).show();
    }

    /**
     * An onClick method for the cancel button. It works by setting the boolean addEvent to false, and it
     * conceals the cancel button and reveals the add_event button once more
     *
     * @param view the button which this onClick method is associated with
     */
    public void onCancelClicked(View view){
        addEvent = false;

        view.setVisibility(View.GONE);
        findViewById(R.id.add_event).setVisibility(View.VISIBLE);
    }

    /**
     * Is always listening for the touch on the map of the user, but only carries out functionality
     * if the boolean addEvent has the value true
     * @param latLng the location where the user taps
     */
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

    /**
     * An onClick listener for the info windows that appear when the user taps on a marker. It retrieves the url
     * associated with the event that the marker is representing. If that is successful, then the method will
     * create an intent for event details and then send the user to the event details along with the
     * corresponding event Id of the event which the marker is representing
     *
     * @param marker the marker that is representing an event in the event list whose tag is the event
     *               id of said event
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://nom-nom-dc909.appspot.com/images/" + (marker.getTag()));
        final String eventId = (String) marker.getTag();
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                sendBytes(bytes, eventId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void sendBytes(byte[] bytes, String eventId){
        Intent intent1 = EventDetailsActivity.createIntent(this.getApplicationContext(),bytes);
        intent1.putExtra("eventId",eventId);

        startActivity(intent1);
    }


    /**
     * This method is called anytime the MutableLiveData field of the EventList ViewModel is changed.
     * It calls a helper method, placeMarkers(), which places a marker at the location of each event in the EventList
     *
     * @param events the MutableLiveData object in the EventList ViewModel which contains all of the events
     *               in the SQLite database
     */
    @Override
    public void onChanged(ArrayList<Event> events) {
        if(mMap != null) {
            placeMarkers();
        }
    }

    /**
     * Private method which refreshes the SQLite database with the data from Firebase
     */
    private void firebasePull() {
        fb.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<Event> tmp = new ArrayList<>();

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Event e = document.toObject(Event.class);
                        tmp.add(e);
                        if (sqlVersionExists(e.getEventId())) {
                            db.eventDao().update(e);
                        }
                        else {
                            db.eventDao().insertEvent(e);
                        }
                    }
                }
                for (Event e : db.eventDao().getAll()) {
                    if (!fireBaseVersionExists(e.getEventId(), tmp)) {
                        db.eventDao().delete(e);
                    }
                }
                refresh();

            }
        });
    }
    private void removeOutdated(){
        Date currentTime = Calendar.getInstance().getTime();
        for(Event e: eventList.getEventList().getValue()){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            try {
                Date temp = sdf.parse(e.getEndTime());
                Date end =(Date)currentTime.clone();
                end.setHours(temp.getHours());
                end.setMinutes(temp.getMinutes());
                end.setSeconds(temp.getSeconds());
                if(end.getTime()<currentTime.getTime()){
                    DocumentReference deleteRef = fb.collection("events").document(e.getEventId());
                    deleteRef.delete();
                    db.eventDao().delete(e);
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }


    private boolean fireBaseVersionExists(String id, ArrayList<Event> fireBaseEvents){
        for(Event e : fireBaseEvents){
            if(e.getEventId().equals(id))
                return true;
        }
        return false;
    }

    private boolean sqlVersionExists(String id) {
        return db.eventDao().findByID(id) != null;
    }

    private StorageReference uploadImage(String filename, byte[] bytes) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + filename);
        UploadTask uploadTask = imageRef.putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        return imageRef;
    }

    /**
     * An onClick method for the button Refresh. Anytime this button is pushed, the app will update the
     * SQLite database to match the data of the Firebase database
     *
     * @param view the Refresh button associated with this onClick method
     */
    public void onRefreshClicked(View view) {
        firebasePull();
    }
}
