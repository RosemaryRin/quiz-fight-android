package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Duel implements Serializable {
    private static final long serialVersionUID = -6756798794233451204L;

    private String duelID;
    private String opponent;
    private Score score;
    private List<Quiz> quizzes = new LinkedList<>();

    public Duel (@NonNull String duelID, @NonNull String opponent,
                 @NonNull Score score, @NonNull List<Quiz> quizzes) {
        this.duelID = duelID;
        this.opponent = opponent;
        this.score = score;
        this.quizzes = quizzes;
    }

    public Duel (@NonNull String duelID, @NonNull String opponent) {
        this.duelID = duelID;
        this.opponent = opponent;
        this.score = new Score(0, 0);
    }

    public String getDuelID() {
        return duelID;
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

    public void addQuiz(@NonNull Quiz quiz) {
        quizzes.add(quiz);
    }
}
