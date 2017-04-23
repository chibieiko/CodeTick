package com.sankari.erika.codetick.Adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.sankari.erika.codetick.Classes.TodayProject;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by erika on 4/18/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private final String TAG = this.getClass().getName();
    private TodaySummary todaySummary;
    private View todayPieView;

    public TodayAdapter(TodaySummary todaySummary) {
        this.todaySummary = todaySummary;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @Override
    public TodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Debug.print(TAG, "onCreateViewHolder", "ViewType: " + viewType, 5);

        if (viewType == 0) {
            // Inflates the today_total layout
            View todayTotalView = inflater.inflate(R.layout.item_today_total, parent, false);

            // Returns a new holder instance
            return new ViewHolder(todayTotalView);
        } else if (viewType == 1) {
            todayPieView = inflater.inflate(R.layout.item_today_chart, parent, false);

            return new ViewHolder(todayPieView);
        } else {
            // Inflates the today_project layout
            View todayProjectView = inflater.inflate(R.layout.item_today_project, parent, false);

            // Returns a new holder instance
            return new ViewHolder(todayProjectView);
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
    public void onBindViewHolder(TodayAdapter.ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", "position: " + position, 5);
        Debug.print(TAG, "onBindViewHolder", "viewType: " + holder.getItemViewType(), 5);

        switch (holder.getItemViewType()) {
            // Today total time.
            case 0:
                TextView todayTimeBox = holder.todayTime;
                if (todaySummary.getTodayProjectList().size() > 0) {
                    String totalTime = "Total: " + String.format("%dh %dmin",
                            TimeUnit.SECONDS.toHours(todaySummary.getTotalTime()),
                            TimeUnit.SECONDS.toMinutes(todaySummary.getTotalTime()) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(todaySummary.getTotalTime())));

                    todayTimeBox.setText(totalTime);
                }

                break;

            // Pie chart.
            case 1:
                PieChart todayPie = holder.todayPie;

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
                legend.setTextColor(ContextCompat.getColor(todayPieView.getContext(), R.color.secondary_text));

                // Draws the pie chart.
                todayPie.invalidate();

                break;

            // Project list.
            default:
                // Gets the data model based on position (-2 because todaySummary total time and
                // chart take positions 0 & 1).
                TodayProject todayProject = todaySummary.getTodayProjectList().get(position - 2);

                holder.projectName.setText(todayProject.getName());
                String time = todayProject.getHours() + "h " + todayProject.getMinutes() + "min";
                holder.projectTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        // Add two to count for chart and total time.
        return todaySummary.getTodayProjectList().size() + 2;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        protected TextView projectName;
        protected TextView projectTime;
        protected TextView todayTime;
        protected PieChart todayPie;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projectName);
            projectTime = (TextView) itemView.findViewById(R.id.projectTime);
            todayTime = (TextView) itemView.findViewById(R.id.total_today_box);
            todayPie = (PieChart) itemView.findViewById(R.id.today_pie_chart);
        }
    }
}