package com.sankari.erika.codetick.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.sankari.erika.codetick.R;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("projectname");
        setTitle(name);

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
