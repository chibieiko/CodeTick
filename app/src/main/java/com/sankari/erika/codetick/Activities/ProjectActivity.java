package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.ProjectDetailsHandler;
import com.sankari.erika.codetick.Classes.Language;
import com.sankari.erika.codetick.Classes.ProjectDetails;
import com.sankari.erika.codetick.Classes.TodayProject;
import com.sankari.erika.codetick.Listeners.OnProjectDetailsLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity implements OnProjectDetailsLoadedListener{

    private TextView totalTime;
    private TextView projectAverageTime;
    private TextView bestday;
    private TextView bestdayTime;
    private TextView lastModified;
    private PieChart languagePie;

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
        lastModified = (TextView) findViewById(R.id.project_activity_last_modified);
        languagePie = (PieChart) findViewById(R.id.language_pie_chart);

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

        final String totalTimeText = "Total " + Util.convertMillisToHoursAndMinutes(projectDetails.getTotalTime());
        final String dailyAverageText = "Daily avg " + Util.convertMillisToHoursAndMinutes(projectDetails.getDailyAverage());
        final String bestdayTimeText = "Best day time " + Util.convertMillisToHoursAndMinutes(projectDetails.getBestDayTime());
        final String bestdayDate = "Best day: " + projectDetails.getBestDayDate();
        final String lastModifiedDate = "Last modified: " + projectDetails.getLastModified();

        List<Language> languages = projectDetails.getLanguages();
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Language language : languages) {
            pieEntries.add(new PieEntry(language.getPercent(), language.getName()));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setSliceSpace(2);

        // todo proper colors and many of them
        int[] colors = {
                ContextCompat.getColor(this, R.color.pink),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.gold),
        };

        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);
        //pieData.setValueTextColor(Color.WHITE);

        languagePie.setData(pieData);
        languagePie.setDrawEntryLabels(false);
        languagePie.setUsePercentValues(true);
        languagePie.setCenterText("Languages");
        languagePie.setCenterTextSize(16);
        languagePie.setNoDataText("Maybe it's time to code something?");
        languagePie.setDescription(null);
        languagePie.setTouchEnabled(false);

        Legend legend = languagePie.getLegend();
        legend.setTextSize(16f);
        legend.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                totalTime.setText(totalTimeText);
                projectAverageTime.setText(dailyAverageText);
                bestday.setText(bestdayDate);
                bestdayTime.setText(bestdayTimeText);
                lastModified.setText(lastModifiedDate);

                // Draws the pie chart.
                languagePie.invalidate();
            }
        });
    }

    @Override
    public void onProjectDetailsLoadError(String error) {
        System.out.println(error);
    }
}
