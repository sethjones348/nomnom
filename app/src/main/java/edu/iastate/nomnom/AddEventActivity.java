package edu.iastate.nomnom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, AddEventActivity.class);
        //intent.putExtra("newEvent", event);
        return intent;
    }
}
