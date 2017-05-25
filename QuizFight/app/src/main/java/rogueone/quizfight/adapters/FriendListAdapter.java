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

public class FriendListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] friends;

    public FriendListAdapter(Context context, String[] friends) {
        super(context, -1, friends);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friend_row, parent, false);

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_friendrow_name);
        nameView.setText(friends[position]);

        return rowView;
    }
}

