package rogueone.quizfight.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Duel implements Serializable {


    private static final long serialVersionUID = 1342499902115047075L;
    private String duelID;
    private String opponent;
    private List<Quiz> quizzes = new ArrayList<Quiz>(3);

    public Duel (@NonNull String duelID, @NonNull String opponent, @NonNull List<Quiz> quizzes) {
        this.duelID = duelID;
        this.opponent = opponent;
        this.quizzes = quizzes;
    }

    public Duel (@NonNull String duelID, @NonNull String opponent) {
        this.duelID = duelID;
        this.opponent = opponent;
        quizzes.add(new Quiz());
    }

    public Duel(@NonNull String duelID, @NonNull String opponent, @NonNull Quiz quiz) {
        this.duelID = duelID;
        this.opponent = opponent;
        quizzes.add(quiz);
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

    public Quiz getCurrentQuiz() {
        if (quizzes.size() == 0) {
            quizzes.add(new Quiz());
        }
        return quizzes.get(quizzes.size() - 1);
    }
}
