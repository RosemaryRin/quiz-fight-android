package rogueone.quizfight.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.leaderboard.LeaderboardScore;

import java.util.List;

import rogueone.quizfight.R;

public class LeaderboardAdapter extends ArrayAdapter<LeaderboardScore> {
    private final Context context;
    private final List<LeaderboardScore> entries;

    public LeaderboardAdapter(@NonNull Context context, @NonNull List<LeaderboardScore> entries) {
        super(context, -1, entries);
        this.context = context;
        this.entries = entries;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.leaderboard_row, parent, false);

        TextView rankView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_rank);
        rankView.setText(entries.get(position).getDisplayRank());

        ImageView iconView = (ImageView) rowView.findViewById(R.id.imageview_toprankedrow_icon);
        ImageManager mgr = ImageManager.create(getContext());
        mgr.loadImage(iconView, entries.get(position).getScoreHolderHiResImageUri());

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_name);
        nameView.setText(entries.get(position).getScoreHolderDisplayName());

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_score);
        scoreView.setText(entries.get(position).getDisplayScore());

        return rowView;
    }
}

