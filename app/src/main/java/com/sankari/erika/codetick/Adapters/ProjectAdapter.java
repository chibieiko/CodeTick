package com.sankari.erika.codetick.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Debug.print(TAG, "onCreateViewHolder", "here", 4);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_projects, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Debug.print(TAG, "onBindViewHolder", projectList.get(position).getName(), 4);
        ProjectListItem projectListItem = projectList.get(position);
        holder.projectName.setText(projectListItem.getName());
    }

    @Override
    public int getItemCount() {
        System.out.println("ITEM COUNT: " + projectList.size());
        return projectList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView projectName;

        public ViewHolder(View itemView) {
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.projects_name);
            projectName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println("CLICKED");
            System.out.println("VALUE: " + String.valueOf(getAdapterPosition()));
            System.out.println(projectList.get(getAdapterPosition()).getName());

            String projectName = projectList.get(getAdapterPosition()).getName();
            Intent intent = new Intent(v.getContext(), ProjectActivity.class);
            intent.putExtra("projectname", projectName);
            v.getContext().startActivity(intent);
        }
    }
}
