package rogueone.quizfight.adapters;

/**
 * Created by Becks on 24/05/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

    public String getFacebookId(int position) {
        String facebookId = "";
        try {
            JSONObject friend = friends.getJSONObject(position);
            facebookId = friend.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facebookId;
    }

    @Override
    public @NonNull View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.friend_row, parent, false);
        final ImageView iconView = (ImageView) rowView.findViewById(R.id.imageview_friendrow_icon);
        final TextView nameView = (TextView) rowView.findViewById(R.id.textview_friendrow_name);
        ProgressBar friendProgressBar = (ProgressBar) rowView.findViewById(R.id.friendProgressBar);
        friendProgressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        String friend_id;

        try {
            JSONObject data = friends.getJSONObject(position);
            String friend_name = data.getString("name");
            friend_id = data.getString("id");
            nameView.setText(friend_name);
            new PictureDownloadTask(iconView, friendProgressBar).execute(friend_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView scoreView = (TextView) rowView.findViewById(R.id.textview_friendrow_score);
        //TODO: show friend score

        return rowView;
    }

    private class PictureDownloadTask extends AsyncTask<String, Integer, Bitmap> {

        private ImageView iconView;
        private ProgressBar friendProgressBar;

        PictureDownloadTask(ImageView iconView, ProgressBar progressBar) {
            super();
            this.iconView = iconView;
            this.friendProgressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            friendProgressBar.setVisibility(View.VISIBLE);
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
                Log.d(TAG, "Friends pic request");
                int increment;
                byte[] data;
                InputStream in = null;
                int response;
                HttpURLConnection httpConn = (HttpURLConnection) imageURL.openConnection();
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
                int length = httpConn.getContentLength();

                data = new byte[length];
                increment = length / 100;
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                int count = -1;
                int progress = 0;

                while ((count = in.read(data, 0, increment)) != -1) {
                    progress += count;
                    publishProgress((progress * 100) / length);
                    outStream.write(data, 0, count);
                }
                bitmap = BitmapFactory.decodeByteArray(
                        outStream.toByteArray(), 0, data.length);
                in.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            friendProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            friendProgressBar.setVisibility(View.GONE);
            iconView.setImageBitmap(bitmap);
            Log.d(TAG, "DownloadPic - OnPostExecute");
        }
    }
}