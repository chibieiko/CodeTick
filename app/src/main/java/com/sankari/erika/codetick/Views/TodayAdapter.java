package com.sankari.erika.codetick.Views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sankari.erika.codetick.Classes.Project;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by erika on 4/18/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private TodaySummary todaySummary;

    public TodayAdapter(TodaySummary todaySummary) {
        this.todaySummary = todaySummary;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @Override
    public TodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        System.out.println("onCreateViewHolder::ViewType: " + viewType);

        if (viewType == 0) {
            // Inflates the today_total layout
            View todayTotalView = inflater.inflate(R.layout.item_today_total, parent, false);

            // Returns a new holder instance
            return new ViewHolder(todayTotalView);
        } else {
            // Inflates the today_project layout
            View todayProjectView = inflater.inflate(R.layout.item_today_project, parent, false);

            // Returns a new holder instance
            return new ViewHolder(todayProjectView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("getItemViewType::position: " + position);

        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(TodayAdapter.ViewHolder holder, int position) {
        System.out.println("onBindViewHolder.position: " + position);
        System.out.println("onBindViewHolder.ViewType: " + holder.getItemViewType());

        if (holder.getItemViewType() == 0) {
            TextView todayTimeBox = holder.todayTime;
            String totalTime = "Total time today: " + String.format("%d h %d min",
                    TimeUnit.SECONDS.toHours(todaySummary.getTotalTime()),
                    TimeUnit.SECONDS.toMinutes(todaySummary.getTotalTime()) -
            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(todaySummary.getTotalTime())));


            todayTimeBox.setText(totalTime);
        } else {
            // Gets the data model based on position (-1 because todaySummary total time takes position 0).
            Project project = todaySummary.getProjectList().get(position - 1);

            holder.projectName.setText(project.getName());
            String time = project.getHours() + "h " + project.getMinutes() + "min";
            holder.projectTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        // Add one to count in today total time item.
        int count = todaySummary.getProjectList().size() + 1;
        System.out.println("Count: " + count);
        return count;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        protected TextView projectName;
        protected TextView projectTime;
        protected TextView todayTime;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projectName);
            projectTime = (TextView) itemView.findViewById(R.id.projectTime);
            todayTime = (TextView) itemView.findViewById(R.id.total_today_box);
        }
    }

    /*
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView projectName;
        private TextView projectTime;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ProjectViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projectName);
            projectTime = (TextView) itemView.findViewById(R.id.projectTime);
        }

        public void bindProject(Project project) {
            projectName.setText(project.getName());
            String time = project.getHours() + "h " + project.getMinutes() + "min";
            projectTime.setText(time);
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class TotalTimeViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private Button todayTime;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public TotalTimeViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            todayTime = (Button) itemView.findViewById(R.id.testButton);
        }

        public void setTotalTimeToday(String time) {
            todayTime.setText(time);
        }
    }*/
}
