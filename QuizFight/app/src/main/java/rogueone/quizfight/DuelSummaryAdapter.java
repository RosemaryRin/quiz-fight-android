package rogueone.quizfight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        int playerScore = duels.get(position).getScore().getPlayerScore();
        int opponentScore = duels.get(position).getScore().getOpponentScore();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.duel_row, parent, false);

        ImageView iconView = (ImageView) rowView.findViewById(R.id.resultIcon);
        if (playerScore > opponentScore)
            iconView.setImageResource(R.drawable.victory);
        else if (playerScore == opponentScore)
            iconView.setImageResource(R.drawable.tie);
        else
            iconView.setImageResource(R.drawable.defeat);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        nameView.setText(duels.get(position).getOpponent());

        TextView scoreView = (TextView) rowView.findViewById(R.id.score);
        scoreView.setText(playerScore + " - " + opponentScore);

        return rowView;
    }
}
