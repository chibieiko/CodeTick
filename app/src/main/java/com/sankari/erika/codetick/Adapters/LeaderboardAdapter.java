package com.sankari.erika.codetick.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sankari.erika.codetick.Activities.ProjectActivity;
import com.sankari.erika.codetick.Classes.LeaderboardItem;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Util;

import java.util.List;

/**
 * Created by erika on 5/3/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final String TAG = this.getClass().getName();
    private List<LeaderboardItem> leaderboardList;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LeaderboardAdapter.ViewHolder(inflater.inflate(R.layout.item_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        //  Debug.print(TAG, "onBindViewHolder", projectList.get(position).getName(), 4);
        LeaderboardItem leaderboardItem = leaderboardList.get(position);
        holder.name.setText(leaderboardItem.getName());
        String rankString = "" + leaderboardItem.getRank();
        holder.rank.setText(rankString);
        holder.total_text.setText("Total ");
        holder.total.setText(Util.convertSecondsToHoursAndMinutes(leaderboardItem.getTotal()));
        holder.avg.setText(Util.convertSecondsToHoursAndMinutes(leaderboardItem.getAverage()));
        holder.avg_text.setText("Daily avg ");
        holder.language_text.setText("Languages: ");
        String allLanguages = "";
        List<String> languages = leaderboardItem.getLanguages();
        for (int i = 0; i < languages.size(); i++) {
            if (i != languages.size() - 1) {
                allLanguages += languages.get(i) + ", ";
            } else {
                allLanguages += languages.get(i);
            }
        }

        holder.language.setText(allLanguages);
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        TextView name;
        TextView total_text;
        TextView total;
        TextView avg_text;
        TextView avg;
        TextView language_text;
        TextView language;

        public ViewHolder(View itemView) {
            super(itemView);

            rank = (TextView) itemView.findViewById(R.id.leaderboard_rank);
            name = (TextView) itemView.findViewById(R.id.leaderboard_name);
            total_text = (TextView) itemView.findViewById(R.id.leaderboard_total_text);
            total = (TextView) itemView.findViewById(R.id.leaderboard_total);
            avg_text = (TextView) itemView.findViewById(R.id.leaderboard_avg_text);
            avg = (TextView) itemView.findViewById(R.id.leaderboard_avg);
            language_text = (TextView) itemView.findViewById(R.id.leaderboard_language_text);
            language = (TextView) itemView.findViewById(R.id.leaderboard_language);
        }
    }
}