package com.sankari.erika.codetick.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.R;

public class DayActivity extends AppCompatActivity {

    private DaySummary daySummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Bundle extras = getIntent().getExtras();
        daySummary = (DaySummary) extras.get("daySummary");

        System.out.println("DAYACTITVITY DATE" + daySummary.getDate());

        // For back arrow.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Return to previous.
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
