package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 30/05/17.
 */

public class RoundResult {
    private String duelID;
    private String quizID;
    private String playerID;
    private int score;

    public RoundResult(@NonNull String duelID, @NonNull String quizID,
                       @NonNull String playerID, int score) {
        this.duelID = duelID;
        this.quizID = quizID;
        this.playerID = playerID;
        this.score = score;
    }
}
