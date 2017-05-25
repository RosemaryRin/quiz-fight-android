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

import rogueone.quizfight.R;

public class TopRankedListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] players;

    public TopRankedListAdapter(Context context, String[] players) {
        super(context, -1, players);
        this.context = context;
        this.players = players;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.top_ranked_row, parent, false);

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_toprankedrow_name);
        nameView.setText(players[position]);

        return rowView;
    }
}

