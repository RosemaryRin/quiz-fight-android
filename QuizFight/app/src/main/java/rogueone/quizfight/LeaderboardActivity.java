package rogueone.quizfight;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import butterknife.BindString;
import butterknife.ButterKnife;

public class LeaderboardActivity extends AppCompatActivity {

    private GoogleApiClient client;

    @BindString(R.string.leaderboard_id) String leaderboardId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);

        client = ((QuizFightApplication) getApplicationContext()).getClient();

        // temp!
        Games.Leaderboards.submitScore(client, leaderboardId, 1);

        Games.Leaderboards.loadTopScores(client, leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC, 5)
                .setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            public void onResult(Leaderboards.LoadScoresResult result) {

                int size = result.getScores().getCount();
                Log.d("Debug", "Size: "+size);

                for (int i = 0; i < size; i++) {
                    LeaderboardScore lbs = result.getScores().get(i);

                    String name = lbs.getScoreHolderDisplayName();
                    String score = lbs.getDisplayScore();
                    Uri urlimage = lbs.getScoreHolderHiResImageUri();
                    Log.d("Debug", "Name: "+name+" - Score: " + score);
                }
            }
        });

    }
}
