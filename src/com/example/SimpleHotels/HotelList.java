package com.example.SimpleHotels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HotelList extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button = (Button) findViewById(R.id.button);
    }

    public void goToList(View view) {
        Intent intent = new Intent(this, MyListActivity.class);
        startActivity(intent);
    }

}
