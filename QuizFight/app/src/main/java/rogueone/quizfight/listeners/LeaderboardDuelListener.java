package rogueone.quizfight.listeners;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.StartDuelActivity;
import rogueone.quizfight.rest.api.NewDuel;
import rogueone.quizfight.rest.pojo.RESTDuel;
import rogueone.quizfight.rest.pojo.Round;

/**
 * Created by Becks on 27/05/17.
 */

public class LeaderboardDuelListener implements AdapterView.OnItemClickListener {

    private StartDuelActivity activity;
    private String playerName;

    public LeaderboardDuelListener(Activity activity) {
        this.activity = (StartDuelActivity) activity;

        playerName = Games.Players.getCurrentPlayer(
                ((QuizFightApplication) activity.getApplication()).getClient()
        ).getDisplayName();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        LeaderboardScore lbScore = (LeaderboardScore) adapter.getItemAtPosition(position);
        String opponentName = lbScore.getScoreHolderDisplayName();

        if (!opponentName.equals(playerName))
            activity.createDuel(opponentName);
    }
}
