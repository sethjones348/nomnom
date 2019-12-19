package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class AddEventActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private ImageView img;
    private AppDatabase db;
    private byte[] byteArray;
    private String eventID;
    private boolean isEdit;

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        Intent intent = getIntent();
        if(intent.getStringExtra("editedEvent") != null) {
            isEdit = true;
            eventID=intent.getStringExtra("editedEvent");
            Event event = db.eventDao().findByID(eventID);
            latitude = event.getLatitude();
            longitude = event.getLongitude();
            EditText titleInput = findViewById(R.id.titleInput);
            EditText deetsInput = findViewById(R.id.deetsInput);
            EditText locationInput = findViewById(R.id.locationInput);
            TimePicker startTimeInput = findViewById(R.id.start_time_picker);
            TimePicker endTimeInput = findViewById(R.id.end_time_picker);

            byteArray=intent.getByteArrayExtra("imageToEdit");
            //img.setImageBitmap(even);

            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            img.setImageBitmap(bmp);



            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            Date currentTime = Calendar.getInstance().getTime();
            Date startD=new Date();
            Date endD=new Date();
            startD.setTime(currentTime.getTime());
            endD.setTime(currentTime.getTime());
            try {
                startD = sdf.parse(event.getStartTime());
                endD = sdf.parse(event.getEndTime());
                startD.setMonth(currentTime.getMonth());
                startD.setDate(currentTime.getDate());
                startD.setYear(currentTime.getYear());
                endD.setMonth(currentTime.getMonth());
                endD.setDate(currentTime.getDate());
                endD.setYear(currentTime.getYear());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            titleInput.setText(event.getTitle());
            deetsInput.setText(event.getFood());
            locationInput.setText(event.getLocationDetails());
            startTimeInput.setHour(startD.getHours());
            startTimeInput.setMinute(startD.getMinutes());
            endTimeInput.setHour(endD.getHours());
            endTimeInput.setMinute(endD.getMinutes());




        }else{
            latitude = intent.getDoubleExtra("eventLatitude", 0.0);
            longitude = intent.getDoubleExtra("eventLongitude", 0.0);
        }
    }

    public static Intent createIntent(Context context, LatLng latLng) {
        Intent intent = new Intent(context, AddEventActivity.class);

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        intent.putExtra("eventLatitude", latitude);
        intent.putExtra("eventLongitude", longitude);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        // String startTime = startTimeInput.getCurrentHour() + ":" + startTimeInput.getCurrentMinute();
        // String endTime = endTimeInput.getCurrentHour() + ":" + endTimeInput.getCurrentMinute();
        //TODO fix time format
        String strStart = formatTime(startTimeInput.getHour(),startTimeInput.getMinute());
        String strEnd = formatTime(endTimeInput.getHour(),endTimeInput.getMinute());

        main.putExtra("title", title);
        main.putExtra("food", deets);
        main.putExtra("locationDetails", location);
        main.putExtra("startTime", strStart);
        main.putExtra("endTime", strEnd);
        main.putExtra("lat", latitude);
        main.putExtra("long", longitude);
        main.putExtra("data_change", true);
        main.putExtra("photo",byteArray);
        main.putExtra("isEdit", isEdit);
        main.putExtra("eventId", eventID);
        startActivity(main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        Bitmap bit = (Bitmap)intent.getExtras().get("data");
        img.setImageBitmap(bit);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG,100,stream);
        byteArray = stream.toByteArray();

    }

    public static Intent createIntent(Context context,String eventId) {
        Intent intent = new Intent(context, AddEventActivity.class);
        intent.putExtra("editedEvent", eventId);
        return intent;
    }

    private String formatTime(int hour, int minute) {
        Date dt = new Date(2019,hour,minute);
        dt.setHours(hour);
        dt.setMinutes(minute);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time1 = sdf.format(dt);
        Toast.makeText(AddEventActivity.this, "time "+time1, Toast.LENGTH_LONG).show();
        return time1;
    }

}
