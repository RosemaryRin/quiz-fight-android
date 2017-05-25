package rogueone.quizfight.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rogueone.quizfight.R;
import rogueone.quizfight.models.Duel;

public class DuelSummaryAdapter extends ArrayAdapter<Duel> {
    private Context context;
    private List<Duel> duels = new ArrayList<>();

    public DuelSummaryAdapter(@NonNull Context context, @NonNull List<Duel> duels) {
        super(context, -1, duels);
        this.context = context;
        this.duels = duels;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int playerScore = duels.get(position).getScore().getPlayerScore();
        int opponentScore = duels.get(position).getScore().getOpponentScore();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.duel_row, parent, false);

        ImageView iconView = (ImageView) rowView.findViewById(R.id.imageview_duelrow_resulticon);

        if (playerScore > opponentScore) {
            iconView.setImageResource(R.drawable.all_victory);
            iconView.setColorFilter(ContextCompat.getColor(context,R.color.won_duel));
        }
        else if (playerScore == opponentScore) {
            iconView.setImageResource(R.drawable.all_tie);
            iconView.setColorFilter(ContextCompat.getColor(context,R.color.tie_duel));
        }
        else {
            iconView.setImageResource(R.drawable.all_defeat);
            iconView.setColorFilter(ContextCompat.getColor(context,R.color.lost_duel));
        }

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_duelrow_name);
        nameView.setText(duels.get(position).getOpponent());

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_duelrow_score);
        scoreView.setText(playerScore + " - " + opponentScore);

        return rowView;
    }
}
