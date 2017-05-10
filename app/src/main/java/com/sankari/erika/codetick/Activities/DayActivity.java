package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.sankari.erika.codetick.Adapters.DayAdapter;
import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.CustomDividerItemDecoration;
import com.sankari.erika.codetick.Utils.Util;

/**
 * Showcases one day's coding activity.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class DayActivity extends AppCompatActivity {

    /**
     * Gets day information from intent and creates a recycler view.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Bundle extras = getIntent().getExtras();
        DaySummary daySummary = (DaySummary) extras.get("daySummary");

        String title;
        if (Util.checkIfToday(daySummary.getDate(), "yyyy-MM-dd")) {
            title = getString(R.string.date_today);
        } else if (Util.checkIfYesterday(daySummary.getDate(), "yyyy-MM-dd")) {
            title = getString(R.string.date_yesterday);
        } else {
            title = Util.convertStringToReadableDateString(daySummary.getDate(), "yyyy-MM-dd");
        }

        setTitle(title);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.day_recycler_view);
        DayAdapter dayAdapter = new DayAdapter(daySummary, this);
        recyclerView.setAdapter(dayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(
                ContextCompat.getDrawable(this, R.drawable.item_decorator), this, true));

        // For back arrow.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Returns to previous activity if back arrow is clicked.
     *
     * @param item back arrow menu item
     * @return true if back arrow is clicked, otherwise returns super call
     */
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
