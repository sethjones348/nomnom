package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailsActivity extends AppCompatActivity {

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        int eventId = getIntent().getIntExtra("eventId", 0);

        //Psuedo code for what this activity will need to do
        /*
        this.event = Database(eventId)
         */
        //This toast is only here to test if the id was succesfully sent through the intent

        Toast.makeText(EventDetailsActivity.this, "eventId: " + eventId, Toast.LENGTH_LONG).show();

        TextView title = findViewById(R.id.titleOutput);
        TextView food = findViewById(R.id.deetsOutput);
        TextView locationDetails = findViewById(R.id.locationOutput);
        TextView startTime = findViewById(R.id.startOutput);
        TextView endTime = findViewById(R.id.endOutput);

        //Uncomment below once the psuedo code above is finished and this.event is initalized
//        title.setText(event.getTitle());
//        food.setText(event.getFood());
//        locationDetails.setText(event.getLocationDetails());
//        startTime.setText(event.getStartTime());
//        endTime.setText(event.getEndTime());
    }

    public static Intent createIntent(Context context, int eventId) {
        Intent intent = new Intent(context, EventDetailsActivity.class);

        intent.putExtra("eventId", eventId);
        return intent;
    }
}