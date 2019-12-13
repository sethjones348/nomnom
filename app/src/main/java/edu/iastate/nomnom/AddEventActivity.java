package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class AddEventActivity extends AppCompatActivity {

    private LatLng eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


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
        startActivity(main);
    }

}
