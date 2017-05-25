package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Duel implements Serializable {
    private String opponent;
    private Score score;
    private Quiz quiz;

    public Duel (@NonNull String opponent, @NonNull Score score, @NonNull Quiz quiz) {
        this.opponent = opponent;
        this.score = score;
        this.quiz = quiz;
    }

    public String getOpponent() {
        return opponent;
    }

    public Score getScore() {
        return score;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
