package com.sankari.erika.codetick.Adapters;

import android.content.Context;
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
 * Handles activity fragment's recycler view items.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Holds activity summary data.
     */
    private ActivitySummary activitySummary;

    /**
     * Bar chart view showcasing coding activity of last two weeks.
     */
    private View chartView;

    /**
     * Context for extracting string resources.
     */
    private Context context;

    /**
     * Receives the activity summary.
     *
     * @param activitySummary has activity summary data
     */
    public ActivityAdapter(ActivitySummary activitySummary, Context context) {
        this.activitySummary = activitySummary;
        this.context = context;
    }

    /**
     * Inflates item layout from XML based on view type and returns the holder.
     *
     * @param parent view group parent
     * @param viewType view type displayed as integer
     * @return view holder with inflated view
     */
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Debug.print(TAG, "onCreateViewHolder", "ViewType: " + viewType, 5);

        if (viewType == 0) {
            View activityTotalView = inflater.inflate(R.layout.item_activity_total, parent, false);

            return new ViewHolder(activityTotalView);
        } else if (viewType == 1) {
            chartView = inflater.inflate(R.layout.item_activity_chart, parent, false);

            return new ViewHolder(chartView);
        } else if (viewType == 2) {
            View activityDayView = inflater.inflate(R.layout.item_activity_list, parent, false);

            return new ViewHolder(activityDayView);
        } else {
            View noDataView = inflater.inflate(R.layout.item_no_data, parent, false);

            return new ViewHolder(noDataView);
        }
    }

    /**
     * Returns a different view type depending on the position and data.
     * <p>
     * Enables custom recycler view with different item XMLs.
     *
     * @param position recycler view list position
     * @return view type
     */
    @Override
    public int getItemViewType(int position) {
        Debug.print(TAG, "getItemViewType", "position: " + position, 5);
        if (activitySummary.getDaySummaryList().size() > 0) {
            if (position == 0) {
                return 0;
            } else if (position == 1) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }

    /**
     * Populates data into the item through view holder.
     *
     * @param holder view holder
     * @param position recycler view list position
     */
    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", "position: " + position, 5);
        Debug.print(TAG, "onBindViewHolder", "viewType: " + holder.getItemViewType(), 5);

        switch (holder.getItemViewType()) {
            // Activity total time and avg.
            case 0:
                holder.total_box_text.setText(R.string.activity_adapter_total_label);
                holder.total_box.setText(Util.convertSecondsToHoursAndMinutes(activitySummary.getTotal()));

                holder.avg_box_text.setText(R.string.activity_adapter_average_label);
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
            case 2:
                // Gets the data model based on position (-2 because summary total time and
                // chart take positions 0 & 1).
                DaySummary daySummary = activitySummary.getDaySummaryList().get(position - 2);
                String dateText;
                if (Util.checkIfToday(daySummary.getDate(), "yyyy-MM-dd")) {
                    dateText = context.getString(R.string.date_today);
                } else if (Util.checkIfYesterday(daySummary.getDate(), "yyyy-MM-dd")) {
                    dateText = context.getString(R.string.date_yesterday);
                } else {
                    dateText = Util.convertStringToReadableDateString(daySummary.getDate(), "yyyy-MM-dd");
                }

                holder.date.setText(dateText);
                holder.time.setText(Util.convertSecondsToHoursAndMinutes(daySummary.getTotal()));

                break;

            // No data.
            default:
                holder.noActivityData.setText(R.string.today_adapter_no_data);
        }
    }

    /**
     * Returns how many items the recycler view contains.
     *
     * @return item count for recycler view
     */
    @Override
    public int getItemCount() {
        if (activitySummary.getDaySummaryList().size() > 0) {
            // Add two to count for chart and total time.
            return activitySummary.getDaySummaryList().size() + 2;
        } else if (activitySummary.getTotal() == -1) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Provides a direct reference to each of the views within a data item.
     * <p>
     * Used to cache the views within the item layout for fast access.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row.

        /**
         * Label for total time.
         */
        TextView total_box_text;

        /**
         * Total time coded.
         */
        TextView total_box;

        /**
         * Label for average time.
         */
        TextView avg_box_text;

        /**
         * Daily average.
         */
        TextView avg_box;

        /**
         * Date.
         */
        TextView date;

        /**
         * Time coded on a specific day.
         */
        TextView time;

        /**
         * Displays two weeks coding time.
         */
        BarChart bar_chart;

        /**
         * Day list item layout.
         */
        RelativeLayout list_item;

        /**
         * Displayed if there is no data from the past two weeks.
         */
        TextView noActivityData;

        /**
         * Accepts entire item row and does the view lookups to find each subview.
         *
         * @param itemView entire item row
         */
        ViewHolder(View itemView) {
            super(itemView);

            total_box_text = (TextView) itemView.findViewById(R.id.activity_total_box_text);
            total_box = (TextView) itemView.findViewById(R.id.activity_total_box);
            avg_box_text = (TextView) itemView.findViewById(R.id.activity_avg_box_text);
            avg_box = (TextView) itemView.findViewById(R.id.activity_avg_box);
            date = (TextView) itemView.findViewById(R.id.activity_date);
            time = (TextView) itemView.findViewById(R.id.activity_time);
            bar_chart = (BarChart) itemView.findViewById(R.id.activity_bar_chart);
            noActivityData = (TextView) itemView.findViewById(R.id.no_data);
            list_item = (RelativeLayout) itemView.findViewById(R.id.activity_list_item);
            if (list_item != null) {
                list_item.setOnClickListener(this);
            }
        }

        /**
         * Day list click listener.
         * <p>
         * Opens day activity with day summary object.
         *
         * @param v view
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DayActivity.class);
            intent.putExtra("daySummary", activitySummary.getDaySummaryList().get(getAdapterPosition() - 2));
            v.getContext().startActivity(intent);
        }
    }
}
