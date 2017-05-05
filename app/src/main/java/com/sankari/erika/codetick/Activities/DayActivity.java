package com.sankari.erika.codetick.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.sankari.erika.codetick.Adapters.DayAdapter;
import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Util;

public class DayActivity extends AppCompatActivity {

    private DaySummary daySummary;
    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Bundle extras = getIntent().getExtras();
        daySummary = (DaySummary) extras.get("daySummary");

        String title;
        if (Util.checkIfToday(daySummary.getDate(), "yyyy-MM-dd")) {
            title = "Today";
        } else if (Util.checkIfYesterday(daySummary.getDate(), "yyyy-MM-dd")) {
            title = "Yesterday";
        } else {
            title = Util.convertStringToReadableDateString(daySummary.getDate(), "yyyy-MM-dd");
        }

        setTitle(title);

        recyclerView = (RecyclerView) findViewById(R.id.day_recycler_view);
        dayAdapter = new DayAdapter(daySummary);
        recyclerView.setAdapter(dayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration
                (recyclerView.getContext(), DividerItemDecoration.VERTICAL));

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
