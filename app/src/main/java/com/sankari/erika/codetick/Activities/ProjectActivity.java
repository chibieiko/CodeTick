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
import com.sankari.erika.codetick.Listeners.OnProjectDetailsLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity implements OnProjectDetailsLoadedListener{

    private TextView totalTime;
    private TextView totalTimeText;
    private TextView projectAverageTime;
    private TextView projectAverageTimeText;
    private TextView bestday;
    private TextView bestdayText;
    private TextView bestdayTime;
    private TextView bestdayTimeText;
    private PieChart languagePie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("projectname");
        setTitle(name + " last 7 days");

        ApiHandler apiHandler = new ApiHandler(this);
        ProjectDetailsHandler projectDetailsHandler = new ProjectDetailsHandler(apiHandler, name);
        projectDetailsHandler.setProjectDetailsLoadedListener(this);
        projectDetailsHandler.getProjectDetails();

        totalTime = (TextView) findViewById(R.id.project_activity_total_time);
        totalTimeText = (TextView) findViewById(R.id.project_activity_total_time_text);
        projectAverageTime = (TextView) findViewById(R.id.project_activity_average_time);
        projectAverageTimeText = (TextView) findViewById(R.id.project_activity_average_time_text);
        bestday = (TextView) findViewById(R.id.project_activity_best_day);
        bestdayText = (TextView) findViewById(R.id.project_activity_best_day_text);
        bestdayTime = (TextView) findViewById(R.id.project_activity_best_day_time);
        bestdayTimeText = (TextView) findViewById(R.id.project_activity_best_day_time_text);
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
        final String total_time = "" + Util.convertMillisToHoursAndMinutes(projectDetails.getTotalTime());
        final String total_time_text = "Total ";
        final String daily_average = "" + Util.convertMillisToHoursAndMinutes(projectDetails.getDailyAverage());
        final String daily_average_text = "Daily average ";
        final String bestday_time_text = "Best day total ";
        final String bestday_time = Util.convertMillisToHoursAndMinutes(projectDetails.getBestDayTime());
        final String bestday_date_text = "Best day ";
        final String bestday_date = projectDetails.getBestDayDate();

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
        languagePie.setDescription(null);
        languagePie.setTouchEnabled(false);

        Legend legend = languagePie.getLegend();
        legend.setTextSize(16f);
        legend.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(5f);
        legend.setWordWrapEnabled(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                totalTimeText.setText(total_time_text);
                totalTime.setText(total_time);
                projectAverageTimeText.setText(daily_average_text);
                projectAverageTime.setText(daily_average);
                bestdayText.setText(bestday_date_text);
                bestday.setText(bestday_date);
                bestdayTimeText.setText(bestday_time_text);
                bestdayTime.setText(bestday_time);

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
