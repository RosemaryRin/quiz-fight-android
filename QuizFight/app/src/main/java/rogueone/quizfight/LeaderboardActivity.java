package rogueone.quizfight;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rogueone.quizfight.adapters.LeaderboardAdapter;

public class LeaderboardActivity extends AppCompatActivity {

    private static final int PLAYERS_SHOWN = 10;

    private QuizFightApplication application;
    private List<LeaderboardScore> entries;

    @BindString(R.string.leaderboard_id) String leaderboardId;
    @BindView(R.id.listview_leaderboard_list) ListView leaderboard_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);

        application = (QuizFightApplication) getApplicationContext();
        entries = new ArrayList<LeaderboardScore>();
        final Context ctx = this;

        Games.Leaderboards.loadTopScores(application.getClient(), leaderboardId,
                LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC, PLAYERS_SHOWN)
                .setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            public void onResult(Leaderboards.LoadScoresResult result) {

                LeaderboardScoreBuffer lsb = result.getScores();

                for (int i = 0; i < lsb.getCount(); i++)
                    entries.add(lsb.get(i));

                final LeaderboardAdapter adapter = new LeaderboardAdapter(ctx, entries);
                leaderboard_list.setAdapter(adapter);
            }
        });

    }
}
