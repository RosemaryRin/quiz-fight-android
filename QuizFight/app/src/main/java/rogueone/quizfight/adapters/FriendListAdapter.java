package rogueone.quizfight.adapters;

/**
 * Created by Becks on 24/05/17.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rogueone.quizfight.R;

public class FriendListAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final JSONArray friends;

    public FriendListAdapter(Context context, JSONArray friends) {
        super(context, -1);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friend_row, parent, false);

        ImageView iconView = (ImageView) rowView.findViewById(R.id.imageview_friendrow_icon);
        //TODO: show personal icon

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_friendrow_name);

        String friend_name = "";

        try {
            friend_name = friends.getJSONObject(1).getString("first_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nameView.setText(friend_name);

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_friendrow_score);
        //TODO: show friend score

        return rowView;
    }
}

