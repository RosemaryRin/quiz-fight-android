package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Duel implements Serializable {
    private static final long serialVersionUID = 8344928931366336118L;

    private String duelID;
    private String opponent;
    private List<Quiz> quizzes = new LinkedList<Quiz>();

    public Duel (@NonNull String duelID, @NonNull String opponent, @NonNull List<Quiz> quizzes) {
        this.duelID = duelID;
        this.opponent = opponent;
        this.quizzes = quizzes;
    }

    public Duel (@NonNull String duelID, @NonNull String opponent) {
        this.duelID = duelID;
        this.opponent = opponent;
    }

    public String getDuelID() {
        return duelID;
    }

    public String getOpponent() {
        return opponent;
    }

    public Score getScore() {
        int playerScore = 0, opponentScore = 0;

        for (Quiz q : quizzes) {
            Score quizScore = q.getScore();

            playerScore += quizScore.getPlayerScore();
            opponentScore += quizScore.getOpponentScore();
        }

        return new Score(playerScore, opponentScore);
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void addQuiz(@NonNull Quiz quiz) {
        quizzes.add(quiz);
    }
}
