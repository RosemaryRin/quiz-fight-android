package rogueone.quizfight.adapters;

/**
 * Created by Becks on 24/05/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import rogueone.quizfight.R;

public class FriendListAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final JSONArray friends;
    private final String TAG = "FriendListAdapter";

    public FriendListAdapter(Context context, JSONArray friends) {
        super(context, -1);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.length();
    }

    @Override
    public @NonNull View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friend_row, parent, false);

        ImageView iconView = (ImageView) rowView.findViewById(R.id.imageview_friendrow_icon);

        TextView nameView = (TextView) rowView.findViewById(R.id.textview_friendrow_name);

        try {
            JSONObject data = friends.getJSONObject(position);
            String friend_name = data.getString("name");
            String friend_id = data.getString("id");
            nameView.setText(friend_name);
            new PictureDownloadTask(iconView).execute(friend_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_friendrow_score);
        //TODO: show friend score

        return rowView;
    }

    private class PictureDownloadTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView iconView;

        PictureDownloadTask(ImageView iconView) {
            super();
            this.iconView = iconView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URL imageURL = null;
            try {
                imageURL = new URL("https://graph.facebook.com/" + params[0] + "/picture?type=small");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                Log.d(TAG, "Friends pic request");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
            Log.d(TAG, "DownloadPic - OnPostExecute");
        }
    }
}