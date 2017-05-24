package rogueone.quizfight;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DuelSummaryAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final int[][] scores;

    public DuelSummaryAdapter(Context context, String[] values, int[][] scores) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int playerScore = scores[position][0];
        int opponentScore = scores[position][1];

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
        nameView.setText(values[position]);

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_duelrow_score);
        scoreView.setText(playerScore+" - "+opponentScore);

        return rowView;
    }
}
