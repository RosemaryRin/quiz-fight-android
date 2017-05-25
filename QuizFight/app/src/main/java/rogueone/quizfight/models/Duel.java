package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Duel implements Serializable {
    private String opponent;
    private Score score;
    private List<Quiz> quizzes = new LinkedList<>();

    public Duel (@NonNull String opponent, @NonNull Score score,@NonNull List<Quiz> quizzes) {
        this.opponent = opponent;
        this.score = score;
        this.quizzes = quizzes;
    }

    public String getOpponent() {
        return opponent;
    }

    public Score getScore() {
        return score;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }
}
