package com.sankari.erika.codetick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Util;

/**
 * Handles day activity's recycler view items.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Holds day summary data.
     */
    private DaySummary daySummary;

    /**
     * Context for extracting string resources.
     */
    private Context context;


    /**
     * Receives the day summary.
     *
     * @param daySummary has day summary data
     */
    public DayAdapter(DaySummary daySummary, Context context) {
        this.daySummary = daySummary;
        this.context = context;
    }

    // Usually involves inflating a layout from XML and returning the holder.

    /**
     * Inflates item layout from XML based on view type and returns the holder.
     *
     * @param parent view group parent
     * @param viewType view type displayed as integer
     * @return view holder with inflated view
     */
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Debug.print(TAG, "onCreateViewHolder", "ViewType: " + viewType, 5);

        if (viewType == 0) {
            // Inflates the today_total layout.
            View dayTotalView = inflater.inflate(R.layout.item_today_total, parent, false);

            return new ViewHolder(dayTotalView);
        } else if (viewType == 1) {
            // Inflates the today_project layout.
            View dayProjectView = inflater.inflate(R.layout.item_today_project, parent, false);

            return new ViewHolder(dayProjectView);
        } else {
            // Inflates the no_data layout.
            View noData = inflater.inflate(R.layout.item_no_data, parent, false);

            return new ViewHolder(noData);
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

        if (daySummary.getProjectList().size() <= 0) {
            return 2;
        }

        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Populates data into the item through view holder.
     *
     * @param holder view holder
     * @param position recycler view list position
     */
    @Override
    public void onBindViewHolder(DayAdapter.ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", "position: " + position, 5);
        Debug.print(TAG, "onBindViewHolder", "viewType: " + holder.getItemViewType(), 5);

        switch (holder.getItemViewType()) {
            // Today total time.
            case 0:
                if (daySummary.getProjectList().size() > 0) {
                    String totalText = context.getString(R.string.day_adapter_total_label);
                    String total = "" + Util.convertSecondsToHoursAndMinutes(daySummary.getTotal());

                    holder.totalText.setText(totalText);
                    holder.total.setText(total);
                }

                break;

            // Project list.
            case 1:
                // Gets the data model based on position.
                ProjectListItem projectListItem = daySummary.getProjectList().get(position - 1);

                holder.projectName.setText(projectListItem.getName());
                String time = Util.convertSecondsToHoursAndMinutes(projectListItem.getTime());
                holder.projectTime.setText(time);

                break;

            case 2:
                holder.noData.setText(R.string.day_adapter_no_coding_data);
        }
    }

    /**
     * Returns how many items the recycler view contains.
     *
     * @return item count for recycler view
     */
    @Override
    public int getItemCount() {
        // Add two to count for chart and total time.
        return daySummary.getProjectList().size() + 1;
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
         * Label for total time.
         */
        TextView totalText;

        /**
         * Total time coded.
         */
        TextView total;

        /**
         * Project's name.
         */
        TextView projectName;

        /**
         * Time used for the project.
         */
        TextView projectTime;

        /**
         * Displayed if there is no data for the given day.
         */
        TextView noData;

        /**
         * Accepts entire item row and does the view lookups to find each subview.
         *
         * @param itemView entire item row
         */
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projectName);
            projectTime = (TextView) itemView.findViewById(R.id.projectTime);
            totalText = (TextView) itemView.findViewById(R.id.total_today_box_text);
            total = (TextView) itemView.findViewById(R.id.total_today_box);
            noData = (TextView) itemView.findViewById(R.id.no_data);
        }
    }
}

