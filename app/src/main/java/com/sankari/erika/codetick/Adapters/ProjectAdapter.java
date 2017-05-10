package com.sankari.erika.codetick.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sankari.erika.codetick.Activities.ProjectActivity;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;

import java.util.List;

/**
 * Handles project fragment's recycler view items.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Holds project list items.
     */
    private List<ProjectListItem> projectList;

    /**
     * Receives the project list.
     *
     * @param projectList holds all project list items
     */
    public ProjectAdapter(List<ProjectListItem> projectList) {
        Debug.print(TAG, "ProjectAdapter", "CREATED, list: " + projectList, 6);
        this.projectList = projectList;
    }

    /**
     * Inflates item layout from XML and returns the holder.
     *
     * @param parent view group parent
     * @param viewType view type displayed as integer
     * @return view holder with inflated view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_projects, parent, false));
    }

    /**
     * Populates data into the item through view holder.
     *
     * @param holder view holder
     * @param position recycler view list position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //  Debug.print(TAG, "onBindViewHolder", projectList.get(position).getName(), 4);
        ProjectListItem projectListItem = projectList.get(position);
        holder.projectName.setText(projectListItem.getName());
    }

    /**
     * Returns how many items the recycler view contains.
     *
     * @return item count for recycler view
     */
    @Override
    public int getItemCount() {
        return projectList.size();
    }

    /**
     * Provides a direct reference to each of the views within a data item.
     * <p>
     * Used to cache the views within the item layout for fast access.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * Project's name.
         */
        TextView projectName;

        /**
         * Project item layout.
         */
        LinearLayout projects_item;

        /**
         * Accepts entire item row and does the view lookups to find each subview.
         *
         * @param itemView entire item row
         */
        ViewHolder(View itemView) {
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projects_name);
            projects_item = (LinearLayout) itemView.findViewById(R.id.projects_item);
            projects_item.setOnClickListener(this);
        }

        /**
         * Project item click listener.
         * <p>
         * Opens project activity with project name string.
         *
         * @param v view
         */
        @Override
        public void onClick(View v) {
            String projectName = projectList.get(getAdapterPosition()).getName();
            Intent intent = new Intent(v.getContext(), ProjectActivity.class);
            intent.putExtra("projectname", projectName);
            v.getContext().startActivity(intent);
        }
    }
}
