package com.sankari.erika.codetick.Adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.sankari.erika.codetick.Classes.ActivitySummary;
import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Classes.TodayProject;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by erika on 4/30/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final String TAG = this.getClass().getName();
    private ActivitySummary activitySummary;

    public ActivityAdapter(ActivitySummary activitySummary) {
        this.activitySummary = activitySummary;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Debug.print(TAG, "onCreateViewHolder", "ViewType: " + viewType, 5);

        if (viewType == 0) {
            View activityTotalView = inflater.inflate(R.layout.item_activity_total, parent, false);

            // Returns a new holder instance
            return new ViewHolder(activityTotalView);
        } else if (viewType == 1) {
            View activityChartView = inflater.inflate(R.layout.item_activity_chart, parent, false);

            return new ViewHolder(activityChartView);
        } else {
            View activityDayView = inflater.inflate(R.layout.item_activity_list, parent, false);

            // Returns a new holder instance
            return new ViewHolder(activityDayView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Debug.print(TAG, "getItemViewType", "position: " + position, 5);

        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", "position: " + position, 5);
        Debug.print(TAG, "onBindViewHolder", "viewType: " + holder.getItemViewType(), 5);

        switch (holder.getItemViewType()) {
            // Activity total time and avg.
            case 0:
                System.out.println("CASE 0");
                holder.total_box_text.setText("Total ");
                holder.total_box.setText(Util.convertMillisToHoursAndMinutes(activitySummary.getTotal()));

                holder.avg_box_text.setText("Daily avg ");
                holder.avg_box.setText(Util.convertMillisToHoursAndMinutes(activitySummary.getAverage()));

                break;

            // Bar chart.
            case 1:
                System.out.println("CASE 1");
                /*PieChart todayPie = holder.todayPie;

                List<TodayProject> todayProjects = todaySummary.getTodayProjectList();
                List<PieEntry> pieEntries = new ArrayList<>();
                for (TodayProject todayProject : todayProjects) {
                    pieEntries.add(new PieEntry(todayProject.getPercent(), todayProject.getName()));
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                dataSet.setSliceSpace(2);

                // todo proper colors and many of them
                int[] colors = {
                        ContextCompat.getColor(todayPieView.getContext(), R.color.pink),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.blue),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.gold),
                };
                dataSet.setColors(colors);

                PieData pieData = new PieData(dataSet);
                pieData.setDrawValues(false);
                //pieData.setValueTextColor(Color.WHITE);

                todayPie.setData(pieData);
                todayPie.setDrawEntryLabels(false);
                todayPie.setUsePercentValues(true);
                todayPie.setCenterText("Projects");
                todayPie.setCenterTextSize(16);
                todayPie.setNoDataText("Maybe it's time to code something?");
                todayPie.setDescription(null);
                todayPie.setTouchEnabled(false);

                Legend legend = todayPie.getLegend();
                legend.setTextSize(16f);
                legend.setTextColor(ContextCompat.getColor(todayPieView.getContext(),
                        R.color.secondary_text));
                legend.setXEntrySpace(10f);
                legend.setYEntrySpace(5f);
                legend.setWordWrapEnabled(true);

                // Draws the pie chart.
                todayPie.invalidate();
*/
                break;

            // Day list.
            default:
                System.out.println("CASE DEFAULT");
                // Gets the data model based on position (-2 because todaySummary total time and
                // chart take positions 0 & 1).
                DaySummary daySummary = activitySummary.getDaySummaryList().get(position - 2);

                System.out.println("DATE: " + daySummary.getDate());
                Date date = new Date();
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat = new SimpleDateFormat("MMM dd");
                try {
                    date = dateFormatter.parse(daySummary.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.date.setText(dateFormat.format(date));
                holder.time.setText(Util.convertMillisToHoursAndMinutes(daySummary.getTotal()));
        }
    }

    @Override
    public int getItemCount() {
        // Add two to count for chart and total time.
        return activitySummary.getDaySummaryList().size() + 2;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView total_box_text;
        TextView total_box;
        TextView avg_box_text;
        TextView avg_box;
        TextView date;
        TextView time;
        BarChart bar_chart;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            total_box_text = (TextView) itemView.findViewById(R.id.activity_total_box_text);
            total_box = (TextView) itemView.findViewById(R.id.activity_total_box);
            avg_box_text = (TextView) itemView.findViewById(R.id.activity_avg_box_text);
            avg_box = (TextView) itemView.findViewById(R.id.activity_avg_box);
            date = (TextView) itemView.findViewById(R.id.activity_date);
            time = (TextView) itemView.findViewById(R.id.activity_time);
            bar_chart = (BarChart) itemView.findViewById(R.id.activity_bar_chart);
        }
    }
}
