package com.sankari.erika.codetick.Adapters;

import android.content.Context;
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
import com.sankari.erika.codetick.Utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles today fragment's recycler view items.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Holds today summary data.
     */
    private TodaySummary todaySummary;

    /**
     * Context for extracting string resources.
     */
    private Context context;

    /**
     * Pie chart containing days coding time per project.
     */
    private View todayPieView;

    /**
     * Receives the today summary.
     *
     * @param todaySummary has today summary data.
     */
    public TodayAdapter(TodaySummary todaySummary, Context context) {
        this.todaySummary = todaySummary;
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
    public TodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Debug.print(TAG, "onCreateViewHolder", "ViewType: " + viewType, 5);

        if (viewType == 0) {
            // Inflates the today_total layout.
            View todayTotalView = inflater.inflate(R.layout.item_today_total, parent, false);

            return new ViewHolder(todayTotalView);
        } else if (viewType == 1) {
            todayPieView = inflater.inflate(R.layout.item_today_chart, parent, false);

            return new ViewHolder(todayPieView);
        } else if (viewType == 2) {
            // Inflates the today_project layout.
            View todayProjectView = inflater.inflate(R.layout.item_today_project, parent, false);

            return new ViewHolder(todayProjectView);
        } else {
            // Inflates the no_data layout.
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

        if (todaySummary.getTodayProjectList().size() > 0) {
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
    public void onBindViewHolder(TodayAdapter.ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", "position: " + position, 5);
        Debug.print(TAG, "onBindViewHolder", "viewType: " + holder.getItemViewType(), 5);

        switch (holder.getItemViewType()) {
            // Today total time.
            case 0:
                TextView todayTimeBox = holder.todayTime;
                TextView todayTimeBoxText = holder.todayTimeText;

                String totalText = context.getString(R.string.today_adapter_total_label);
                String total = "" + Util.convertSecondsToHoursAndMinutes(todaySummary.getTotalTime());

                todayTimeBox.setText(total);
                todayTimeBoxText.setText(totalText);

                break;

            // Pie chart.
            case 1:
                PieChart todayPie = holder.todayPie;

                List<TodayProject> todayProjects = todaySummary.getTodayProjectList();
                List<PieEntry> pieEntries = new ArrayList<>();
                for (TodayProject todayProject : todayProjects) {
                    PieEntry temp = new PieEntry(todayProject.getPercent(), todayProject.getName());
                    temp.setLabel(temp.getLabel() + " " + todayProject.getPercent() + "%");
                    pieEntries.add(temp);
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                dataSet.setSliceSpace(2);

                int[] colors = {
                        ContextCompat.getColor(todayPieView.getContext(), R.color.violet),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.red),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.teal),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.blue),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.light_green),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.orange),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.brown),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.gray),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.purple),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.pink),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.light_blue),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.cyan),
                        ContextCompat.getColor(todayPieView.getContext(), R.color.dark_orange)
                };
                dataSet.setColors(colors);

                PieData pieData = new PieData(dataSet);
                pieData.setDrawValues(false);

                todayPie.setData(pieData);
                todayPie.setDrawEntryLabels(false);
                todayPie.setUsePercentValues(true);
                todayPie.setCenterText(context.getString(R.string.today_adapter_chart_label));
                todayPie.setCenterTextSize(16);
                todayPie.setCenterTextColor(R.color.primary_text);
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

                break;

            // Project list.
            case 2:
                // Gets the data model based on position (-2 because todaySummary total time and
                // chart take positions 0 & 1).
                TodayProject todayProject = todaySummary.getTodayProjectList().get(position - 2);

                holder.projectName.setText(todayProject.getName());
                String time = todayProject.getHours() + "h " + todayProject.getMinutes() + "min";
                holder.projectTime.setText(time);

                break;

            // No data.
            default:
                holder.noTodayData.setText(R.string.today_adapter_no_data);
        }
    }

    /**
     * Returns how many items the recycler view contains.
     *
     * @return item count for recycler view
     */
    @Override
    public int getItemCount() {
        if (todaySummary.getTodayProjectList().size() > 0) {
            // Add two to count for chart and total time.
            return todaySummary.getTodayProjectList().size() + 2;
        } else if (todaySummary.getTotalTime() == -1) {
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
    class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row.

        /**
         * Project's name.
         */
        TextView projectName;

        /**
         * Time used on project.
         */
        TextView projectTime;

        /**
         * Total time coded today.
         */
        TextView todayTime;

        /**
         * Label for total time today.
         */
        TextView todayTimeText;

        /**
         * Pie chart displaying projects that have been coded today.
         */
        PieChart todayPie;

        /**
         * Displayed when user hasn't coded today.
         */
        TextView noTodayData;

        /**
         * Accepts entire item row and does the view lookups to find each subview.
         *
         * @param itemView entire item row
         */
        ViewHolder(View itemView) {
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projectName);
            projectTime = (TextView) itemView.findViewById(R.id.projectTime);
            todayTime = (TextView) itemView.findViewById(R.id.total_today_box);
            todayTimeText = (TextView) itemView.findViewById(R.id.total_today_box_text);
            todayPie = (PieChart) itemView.findViewById(R.id.today_pie_chart);
            noTodayData = (TextView) itemView.findViewById(R.id.no_data);
        }
    }
}
