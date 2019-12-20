package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity to display details about an event clicked on the map
 */
public class EventDetailsActivity extends AppCompatActivity {

    /**
     * an Event object create to keep track of all the details for an event
     */
    private Event event;

    /**
     * database object to get up to date details about the event
     */
    AppDatabase database;

    /**
     * a variable to record the image taken
     */
    private byte[] byteArray;

    /**
     * onCreate method that is called any time the activity is created. Recreates the view. It gets an intent from main acitvity with the event ID of the event that the user wants to view
     * @param savedInstanceState
     *      keeps track of the current state of the app to update the details up-to-date
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        String eventId = getIntent().getStringExtra("eventId");

        database = AppDatabase.getAppDatabase(this);

        event = database.eventDao().findByID(eventId);

        TextView title = findViewById(R.id.titleOutput);
        TextView food = findViewById(R.id.deetsOutput);
        TextView locationDetails = findViewById(R.id.locationOutput);
        TextView startTime = findViewById(R.id.startOutput);
        TextView endTime = findViewById(R.id.endOutput);

        title.setText(event.getTitle());
        food.setText(event.getFood());
        locationDetails.setText(event.getLocationDetails());
        startTime.setText(event.getStartTime());
        endTime.setText(event.getEndTime());


        byteArray= getIntent().getByteArrayExtra("image");
        ImageView img = findViewById(R.id.imagePreview);

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        img.setImageBitmap(bmp);
    }

    /**
     * method created to send the this event's ID to main in order to get it deleted, which is triggered by clicking on the delete button in this view
     * @param view
     *      The current view of the app
     */
    public void deleteEvent(View view) {
        Intent intent = MainActivity.createIntent(this, event.getEventId());
        startActivity(intent);
    }

    /**
     * method created to send the this event's ID to AddEventActivity in order to allow one to edit this event, which is triggered by clicking on the edit button in this view
            * @param view
     *      The current view of the app
     */
    public void editEvent(View view){
        Intent intent = AddEventActivity.createIntent(this,event.getEventId());
        intent.putExtra("isEdit", true);
        intent.putExtra("imageToEdit",byteArray);
        startActivity(intent);
    }

    /**
     * a method created to send the image of an event as byte array into an intent to this class
     * @param context
     *      the context refers to the method the information comes from, which in this case in AddEventActivity
     * @param bytes
     *      the byte array that needs to be stored in the intent
     * @return
     *      the intent that needs to be sent to this class
     */
    public static Intent createIntent(Context context, byte[] bytes) {
        Intent intent = new Intent(context, EventDetailsActivity.class);

        intent.putExtra("image",bytes);
        return intent;
    }

}
