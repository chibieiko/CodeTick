package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.ProjectDetailsHandler;
import com.sankari.erika.codetick.Classes.ProjectDetails;
import com.sankari.erika.codetick.Listeners.OnProjectDetailsLoadedListener;
import com.sankari.erika.codetick.R;

public class ProjectActivity extends AppCompatActivity implements OnProjectDetailsLoadedListener{

    private TextView totalTime;
    private TextView projectAverageTime;
    private TextView bestday;
    private TextView bestdayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("projectname");
        setTitle(name);

        ApiHandler apiHandler = new ApiHandler(this);
        ProjectDetailsHandler projectDetailsHandler = new ProjectDetailsHandler(apiHandler, name);
        projectDetailsHandler.setProjectDetailsLoadedListener(this);
        projectDetailsHandler.getProjectDetails();

        totalTime = (TextView) findViewById(R.id.project_activity_total_time);
        projectAverageTime = (TextView) findViewById(R.id.project_activity_average_time);
        bestday = (TextView) findViewById(R.id.project_activity_best_day);
        bestdayTime = (TextView) findViewById(R.id.project_activity_best_day_time);

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

    @Override
    public void onProjectDetailsSuccessfullyLoaded(ProjectDetails projectDetails) {
        //totalTime.setText(projectDetails.getTotalTime());
        //projectAverageTime.setText(projectDetails.getDailyAverage());
        System.out.println("got something");
    }

    @Override
    public void onProjectDetailsLoadError(String error) {
        System.out.println(error);
    }
}
