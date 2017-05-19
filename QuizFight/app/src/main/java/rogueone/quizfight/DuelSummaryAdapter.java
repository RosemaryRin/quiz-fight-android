package rogueone.quizfight;

import android.content.Context;
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

        ImageView iconView = (ImageView) rowView.findViewById(R.id.resultIcon);
        if (playerScore > opponentScore)
            iconView.setImageResource(R.drawable.victory);
        else if (playerScore == opponentScore)
            iconView.setImageResource(R.drawable.tie);
        else
            iconView.setImageResource(R.drawable.defeat);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        nameView.setText(values[position]);

        TextView scoreView = (TextView) rowView.findViewById(R.id.score);
        scoreView.setText(playerScore+" - "+opponentScore);

        return rowView;
    }
}
