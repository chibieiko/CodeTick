package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

/**
 * Showcases a projects coding details from past seven days.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectActivity extends AppCompatActivity implements OnProjectDetailsLoadedListener {

    /**
     * Total time coded.
     */
    private TextView totalTime;

    /**
     * Label for total time coded.
     */
    private TextView totalTimeText;

    /**
     * Average time coded.
     */
    private TextView projectAverageTime;

    /**
     * Label for average time coded.
     */
    private TextView projectAverageTimeText;

    /**
     * Date with longest coding time.
     */
    private TextView bestday;

    /**
     * Label for best day.
     */
    private TextView bestdayText;

    /**
     * Time coded on best day.
     */
    private TextView bestdayTime;

    /**
     * Label for best day time.
     */
    private TextView bestdayTimeText;

    /**
     * Project's name.
     */
    private TextView title;

    /**
     * Pie chart showcases languages that have been used in the project.
     */
    private PieChart languagePie;

    /**
     * Is displayed when there is no data for the project from the last 7 days.
     */
    private LinearLayout noData;

    /**
     * Text indicating that loading can take quite some time.
     */
    private LinearLayout loadingData;

    /**
     * Displayed when there is data for the project from the last 7 days.
     */
    private LinearLayout content;

    /**
     * A swipe refresh layout.
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Gets project details from Intent and sets them to UI.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("projectname");

        ApiHandler apiHandler = new ApiHandler(this);
        final ProjectDetailsHandler projectDetailsHandler = new ProjectDetailsHandler(apiHandler, name);
        projectDetailsHandler.setProjectDetailsLoadedListener(this);

        loadingData = (LinearLayout) findViewById(R.id.crunching_project_details);
        loadingData.setVisibility(View.VISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.project_activity_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingData.setVisibility(View.VISIBLE);
                projectDetailsHandler.getProjectDetails();
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        projectDetailsHandler.getProjectDetails();

        content = (LinearLayout) findViewById(R.id.project_detail_content);
        content.setVisibility(View.GONE);
        noData = (LinearLayout) findViewById(R.id.hide_project_details);
        totalTime = (TextView) findViewById(R.id.project_activity_total_time);
        totalTimeText = (TextView) findViewById(R.id.project_activity_total_time_text);
        projectAverageTime = (TextView) findViewById(R.id.project_activity_average_time);
        projectAverageTimeText = (TextView) findViewById(R.id.project_activity_average_time_text);
        bestday = (TextView) findViewById(R.id.project_activity_best_day);
        bestdayText = (TextView) findViewById(R.id.project_activity_best_day_text);
        bestdayTime = (TextView) findViewById(R.id.project_activity_best_day_time);
        bestdayTimeText = (TextView) findViewById(R.id.project_activity_best_day_time_text);
        languagePie = (PieChart) findViewById(R.id.language_pie_chart);
        title = (TextView) findViewById(R.id.project_activity_title);

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

    /**
     * Sets project details to UI.
     * <p>
     * If there is no data, then displays a no data card.
     *
     * @param projectDetails contains project's information
     */
    @Override
    public void onProjectDetailsSuccessfullyLoaded(ProjectDetails projectDetails) {
        if (projectDetails.getLanguages().size() > 0) {
            final String total_time = "" + Util.convertSecondsToHoursAndMinutes(projectDetails.getTotalTime());
            final String total_time_text = getString(R.string.project_activity_total_time);
            final String daily_average = "" + Util.convertSecondsToHoursAndMinutes(projectDetails.getDailyAverage());
            final String daily_average_text = getString(R.string.project_activity_average);
            final String bestday_time_text = getString(R.string.project_activity_best_day_total);
            final String bestday_time = Util.convertSecondsToHoursAndMinutes(projectDetails.getBestDayTime());
            final String bestday_date_text = getString(R.string.project_activity_best_day);
            String temp_bestday;
            if (Util.checkIfToday(projectDetails.getBestDayDate(), "MM/dd/yyyy")) {
                temp_bestday = getString(R.string.date_today);
            } else if (Util.checkIfYesterday(projectDetails.getBestDayDate(), "MM/dd/yyyy")) {
                temp_bestday = getString(R.string.date_yesterday);
            } else {
                temp_bestday = Util.convertStringToReadableDateString(projectDetails.getBestDayDate(), "MM/dd/yyyy");
            }

            final String bestday_date = temp_bestday;
            final String titleText = projectDetails.getName();

            List<Language> languages = projectDetails.getLanguages();
            List<PieEntry> pieEntries = new ArrayList<>();
            for (Language language : languages) {
                PieEntry temp = new PieEntry(language.getPercent(), language.getName());
                temp.setLabel(temp.getLabel() + " " + language.getPercent() + "%");
                pieEntries.add(temp);
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "");
            dataSet.setSliceSpace(2);

            int[] colors = {
                    ContextCompat.getColor(languagePie.getContext(), R.color.violet),
                    ContextCompat.getColor(languagePie.getContext(), R.color.red),
                    ContextCompat.getColor(languagePie.getContext(), R.color.teal),
                    ContextCompat.getColor(languagePie.getContext(), R.color.blue),
                    ContextCompat.getColor(languagePie.getContext(), R.color.light_green),
                    ContextCompat.getColor(languagePie.getContext(), R.color.orange),
                    ContextCompat.getColor(languagePie.getContext(), R.color.brown),
                    ContextCompat.getColor(languagePie.getContext(), R.color.gray),
                    ContextCompat.getColor(languagePie.getContext(), R.color.purple),
                    ContextCompat.getColor(languagePie.getContext(), R.color.pink),
                    ContextCompat.getColor(languagePie.getContext(), R.color.light_blue),
                    ContextCompat.getColor(languagePie.getContext(), R.color.cyan),
                    ContextCompat.getColor(languagePie.getContext(), R.color.dark_orange)
            };

            dataSet.setColors(colors);

            PieData pieData = new PieData(dataSet);
            pieData.setDrawValues(false);

            languagePie.setData(pieData);
            languagePie.setDrawEntryLabels(false);
            languagePie.setUsePercentValues(true);
            languagePie.setCenterText(getString(R.string.project_activity_language_pie_text));
            languagePie.setCenterTextSize(16);
            languagePie.setCenterTextColor(R.color.primary_text);
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
                    loadingData.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                    totalTimeText.setText(total_time_text);
                    totalTime.setText(total_time);
                    projectAverageTimeText.setText(daily_average_text);
                    projectAverageTime.setText(daily_average);
                    bestdayText.setText(bestday_date_text);
                    bestday.setText(bestday_date);
                    bestdayTimeText.setText(bestday_time_text);
                    bestdayTime.setText(bestday_time);
                    title.setText(titleText);

                    // Draws the pie chart.
                    languagePie.invalidate();

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingData.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        }
    }

    /**
     * Shows snackbar with error.
     * <p>
     * Only called if there is an error fetching project details from Wakatime's server.
     *
     * @param error describes the error
     */
    @Override
    public void onProjectDetailsLoadError(String error) {
        final String message = error;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Snackbar.make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
