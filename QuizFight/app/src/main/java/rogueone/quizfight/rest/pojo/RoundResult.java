package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * This class represents the result of a certain round. It contains both the required information for
 * identifying the duel server-side and the given answers (plus the score).
 *
 * @author Matteo Di Pirro
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
