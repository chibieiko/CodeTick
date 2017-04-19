package com.sankari.erika.codetick.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sankari.erika.codetick.Classes.Project;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.R;

/**
 * Created by erika on 4/18/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private TodaySummary todaySummary;
    private Context context;

    public TodayAdapter(Context context, TodaySummary todaySummary) {
        this.todaySummary = todaySummary;
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @Override
    public TodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context tempContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(tempContext);

        // Inflates the custom layout
        View placeholderView = inflater.inflate(R.layout.item_today_stat, parent, false);

        // Returns a new holder instance
        ViewHolder viewHolder = new ViewHolder(placeholderView);

        return viewHolder;
    }

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(TodayAdapter.ViewHolder holder, int position) {
        // Gets the data model based on position.
        Project project = todaySummary.getProjectList().get(position);

        TextView textView = holder.holderTextView;
        textView.setText(project.getName());
    }

    @Override
    public int getItemCount() {
        return todaySummary.getProjectList().size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView holderTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            holderTextView = (TextView) itemView.findViewById(R.id.placeholder);
        }
    }
}
