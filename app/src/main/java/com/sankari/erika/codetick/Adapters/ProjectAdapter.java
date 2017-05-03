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
 * Created by erika on 4/23/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private final String TAG = this.getClass().getName();
    private List<ProjectListItem> projectList;

    public ProjectAdapter(List<ProjectListItem> projectList) {
        Debug.print(TAG, "ProjectAdapter", "CREATED, list: " + projectList, 4);
        this.projectList = projectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_projects, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  Debug.print(TAG, "onBindViewHolder", projectList.get(position).getName(), 4);
        ProjectListItem projectListItem = projectList.get(position);
        holder.projectName.setText(projectListItem.getName());
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView projectName;
        LinearLayout projects_item;

        public ViewHolder(View itemView) {
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projects_name);
            projects_item = (LinearLayout) itemView.findViewById(R.id.projects_item);
            projects_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String projectName = projectList.get(getAdapterPosition()).getName();
            Intent intent = new Intent(v.getContext(), ProjectActivity.class);
            intent.putExtra("projectname", projectName);
            v.getContext().startActivity(intent);
        }
    }
}
