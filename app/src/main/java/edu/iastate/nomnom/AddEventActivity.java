package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class AddEventActivity extends AppCompatActivity {

    private LatLng eventLocation;
    private ImageView img;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = AppDatabase.getAppDatabase(this);

        img=(ImageView)findViewById(R.id.photoPreview);
        Button b = (Button)findViewById(R.id.photoButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        eventLocation = new LatLng(getIntent().getDoubleExtra("eventLatitude", 0.0),
                                   getIntent().getDoubleExtra("eventLongitude", 0.0));
    }

    public static Intent createIntent(Context context, LatLng latLng) {
        Intent intent = new Intent(context, AddEventActivity.class);

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        intent.putExtra("eventLatitude", latitude);
        intent.putExtra("eventLongitude", longitude);
        return intent;
    }

    public void onClickAdd(View view) {
        Intent main = MainActivity.createIntent(this);

        EditText titleInput = findViewById(R.id.titleInput);
        EditText deetsInput = findViewById(R.id.deetsInput);
        EditText locationInput = findViewById(R.id.locationInput);
        TimePicker startTimeInput = findViewById(R.id.start_time_picker);
        TimePicker endTimeInput = findViewById(R.id.end_time_picker);

        String title = titleInput.getText().toString();
        String deets = deetsInput.getText().toString();
        String location = locationInput.getText().toString();
        String startTime = startTimeInput.getCurrentHour() + ":" + startTimeInput.getCurrentMinute();
        String endTime = endTimeInput.getCurrentHour() + ":" + endTimeInput.getCurrentMinute();
        //TODO fix time format

        main.putExtra("title", title);
        main.putExtra("food", deets);
        main.putExtra("locationDetails", location);
        main.putExtra("startTime", startTime);
        main.putExtra("endTime", endTime);
        main.putExtra("lat", eventLocation.latitude);
        main.putExtra("long", eventLocation.longitude);
        main.putExtra("data_change", true);

        startActivity(main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        Bitmap bit = (Bitmap)intent.getExtras().get("data");
        img.setImageBitmap(bit);
    }

}
