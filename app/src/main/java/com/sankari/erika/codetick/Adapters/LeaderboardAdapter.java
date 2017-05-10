package com.sankari.erika.codetick.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sankari.erika.codetick.Classes.LeaderboardItem;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Util;

import java.util.List;

/**
 * Handles leaderboard activity's recycler view items.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    /**
     * Holds leaderboard items.
     */
    private List<LeaderboardItem> leaderboardList;

    /**
     * Receives the leaderboard list.
     *
     * @param leaderboardList has leaderboard data
     */
    public LeaderboardAdapter(List<LeaderboardItem> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    /**
     * Inflates item layout from XML and returns the holder.
     *
     * @param parent view group parent
     * @param viewType view type displayed as integer
     * @return view holder with inflated view
     */
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LeaderboardAdapter.ViewHolder(inflater.inflate(R.layout.item_leaderboard, parent, false));
    }

    /**
     * Populates data into the item through view holder.
     *
     * @param holder view holder
     * @param position recycler view list position
     */
    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        LeaderboardItem leaderboardItem = leaderboardList.get(position);
        holder.name.setText(leaderboardItem.getName());
        String rankString = "" + leaderboardItem.getRank();
        holder.rank.setText(rankString);
        holder.total_text.setText(R.string.leaderboard_adapter_total_label);
        holder.total.setText(Util.convertSecondsToHoursAndMinutes(leaderboardItem.getTotal()));
        holder.avg.setText(Util.convertSecondsToHoursAndMinutes(leaderboardItem.getAverage()));
        holder.avg_text.setText(R.string.leaderboard_adapter_average_label);
        holder.language_text.setText(R.string.leaderboard_adapter_languages_label);
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

    /**
     * Returns how many items the recycler view contains.
     *
     * @return item count for recycler view
     */
    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    /**
     * Provides a direct reference to each of the views within a data item.
     * <p>
     * Used to cache the views within the item layout for fast access.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Leaderboard rank number.
         */
        TextView rank;

        /**
         * User's name.
         */
        TextView name;

        /**
         * Label for total time.
         */
        TextView total_text;

        /**
         * Total time coded.
         */
        TextView total;

        /**
         * Label for average time.
         */
        TextView avg_text;

        /**
         * Daily average time coded.
         */
        TextView avg;

        /**
         * Label for language list.
         */
        TextView language_text;

        /**
         * List of coding languages used.
         */
        TextView language;

        /**
         * Accepts entire item row and does the view lookups to find each subview.
         *
         * @param itemView entire item row
         */
        ViewHolder(View itemView) {
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