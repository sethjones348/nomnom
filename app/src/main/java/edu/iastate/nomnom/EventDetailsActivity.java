package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailsActivity extends AppCompatActivity {

    private Event event;

    AppDatabase database;
    private byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        String eventId = getIntent().getStringExtra("eventId");

        database = AppDatabase.getAppDatabase(this);

        event = database.eventDao().findByID(eventId);

        Toast.makeText(EventDetailsActivity.this, "eventId: " + eventId, Toast.LENGTH_LONG).show();

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

    public static Intent createIntent(Context context, String eventId) {
        Intent intent = new Intent(context, EventDetailsActivity.class);

        intent.putExtra("eventId", eventId);
        return intent;
    }

    public void deleteEvent(View view) {
        Intent intent = MainActivity.createIntent(this, event.getEventId());
        startActivity(intent);
    }

    public void editEvent(View view){
        Intent intent = AddEventActivity.createIntent(this,event.getEventId());
        intent.putExtra("isEdit", true);
        intent.putExtra("imageToEdit",byteArray);
        startActivity(intent);
    }

    public static Intent createIntent(Context context, byte[] bytes) {
        Intent intent = new Intent(context, EventDetailsActivity.class);

        intent.putExtra("image",bytes);
        return intent;
    }

}
