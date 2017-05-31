package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 30/05/17.
 */

public class RoundResult {
    private String duelID;
    private String quizID;
    private String playerID;
    private boolean[] answers;
    private int score;

    public RoundResult(@NonNull String duelID, @NonNull String quizID,
                       @NonNull String playerID, boolean[] answers, int score) {
        this.duelID = duelID;
        this.quizID = quizID;
        this.playerID = playerID;
        this.answers = answers;
        this.score = score;
    }
}
