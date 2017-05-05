package com.sankari.erika.codetick.Adapters;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.sankari.erika.codetick.Activities.DayActivity;
import com.sankari.erika.codetick.Classes.ActivitySummary;
import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erika on 4/30/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final String TAG = this.getClass().getName();
    private ActivitySummary activitySummary;
    private View chartView;

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
            chartView = inflater.inflate(R.layout.item_activity_chart, parent, false);

            return new ViewHolder(chartView);
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
                holder.total_box_text.setText("Total ");
                holder.total_box.setText(Util.convertSecondsToHoursAndMinutes(activitySummary.getTotal()));

                holder.avg_box_text.setText("Daily avg ");
                holder.avg_box.setText(Util.convertSecondsToHoursAndMinutes(activitySummary.getAverage()));

                break;

            // Bar chart.
            case 1:
                BarChart barChart = holder.bar_chart;

                List<DaySummary> dayList = activitySummary.getDaySummaryList();
                List<BarEntry> barEntries = new ArrayList<>();
                for (int i = 0; i < dayList.size(); i++) {
                    // Convert total time coded in a day to hours.
                    long total = dayList.get(i).getTotal();
                    double codingTimeMinutes = total / 60;
                    double codingTimeHours = codingTimeMinutes / 60;
                    barEntries.add(new BarEntry(dayList.size() - i - 1, (float) codingTimeHours));
                }

                int[] colors = {
                        ContextCompat.getColor(chartView.getContext(), R.color.colorPrimary)
                };

                BarDataSet dataSet = new BarDataSet(barEntries, "");
                dataSet.setColors(colors);

                ArrayList<String> xAxisLabels = new ArrayList<>();
                for (int i = 0; i < dayList.size(); i++) {
                    xAxisLabels.add(Util.convertStringToReadableDateString(
                            dayList.get(dayList.size() - i - 1).getDate(), "yyyy-MM-dd"));
                }

                BarData data = new BarData(dataSet);

                barChart.setData(data);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
                barChart.setFitBars(true);
                barChart.setScaleEnabled(false);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.setDescription(null);

                // Format left axis labels.
                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return (int) value + "h";
                    }
                });

                // Do not show right axis.
                YAxis rightAxis = barChart.getAxisRight();
                rightAxis.setEnabled(false);

                // Do not show legend.
                Legend legend = barChart.getLegend();
                legend.setEnabled(false);

                barChart.invalidate();

                break;

            // Day list.
            default:
                // Gets the data model based on position (-2 because summary total time and
                // chart take positions 0 & 1).
                DaySummary daySummary = activitySummary.getDaySummaryList().get(position - 2);
                String dateText;
                if (Util.checkIfToday(daySummary.getDate(), "yyyy-MM-dd")) {
                    dateText = "Today";
                } else if (Util.checkIfYesterday(daySummary.getDate(), "yyyy-MM-dd")) {
                    dateText = "Yesterday";
                } else {
                    dateText = Util.convertStringToReadableDateString(daySummary.getDate(), "yyyy-MM-dd");
                }

                holder.date.setText(dateText);
                holder.time.setText(Util.convertSecondsToHoursAndMinutes(daySummary.getTotal()));

                break;
        }
    }

    @Override
    public int getItemCount() {
        // Add two to count for chart and total time.
        return activitySummary.getDaySummaryList().size() + 2;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView total_box_text;
        TextView total_box;
        TextView avg_box_text;
        TextView avg_box;
        TextView date;
        TextView time;
        BarChart bar_chart;
        RelativeLayout list_item;

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
            list_item = (RelativeLayout) itemView.findViewById(R.id.activity_list_item);
            if (list_item != null) {
                list_item.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DayActivity.class);
            intent.putExtra("daySummary", activitySummary.getDaySummaryList().get(getAdapterPosition() - 2));
            v.getContext().startActivity(intent);
        }
    }
}
