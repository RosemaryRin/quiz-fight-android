package rogueone.quizfight.adapters;

/**
 * Created by Becks on 24/05/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;

import rogueone.quizfight.R;

public class LeaderboardAdapter extends ArrayAdapter<LeaderboardScore> {
    private final Context context;
    private final LeaderboardScoreBuffer scores;

    public LeaderboardAdapter(Context context, LeaderboardScoreBuffer scores) {
        super(context, -1);
        this.context = context;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.leaderboard_row, parent, false);

        TextView rankView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_rank);
        rankView.setText(scores.get(position).getDisplayRank());

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_name);
        nameView.setText(scores.get(position).getScoreHolderDisplayName());

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_score);
        scoreView.setText(scores.get(position).getDisplayScore());

        return rowView;
    }
}

