package com.sankari.erika.codetick.Adapters;

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
 * Created by erika on 5/3/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private final String TAG = this.getClass().getName();
    private DaySummary daySummary;

    public DayAdapter(DaySummary daySummary) {
        this.daySummary = daySummary;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Debug.print(TAG, "onCreateViewHolder", "ViewType: " + viewType, 5);

        if (viewType == 0) {
            // Inflates the today_total layout
            View dayTotalView = inflater.inflate(R.layout.item_today_total, parent, false);

            // Returns a new holder instance
            return new ViewHolder(dayTotalView);
        } else if (viewType == 1) {
            // Inflates the today_project layout
            View dayProjectView = inflater.inflate(R.layout.item_today_project, parent, false);

            // Returns a new holder instance
            return new ViewHolder(dayProjectView);
        } else {
            View noData = inflater.inflate(R.layout.item_no_data, parent, false);

            return new ViewHolder(noData);
        }
    }

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

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(DayAdapter.ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", "position: " + position, 5);
        Debug.print(TAG, "onBindViewHolder", "viewType: " + holder.getItemViewType(), 5);

        switch (holder.getItemViewType()) {
            // Today total time.
            case 0:
                if (daySummary.getProjectList().size() > 0) {
                    String totalText = "Total ";
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
                holder.noData.setText("No coding data for this day...");
        }
    }

    @Override
    public int getItemCount() {
        // Add two to count for chart and total time.
        return daySummary.getProjectList().size() + 1;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView totalText;
        TextView total;
        TextView projectName;
        TextView projectTime;
        TextView noData;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
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

