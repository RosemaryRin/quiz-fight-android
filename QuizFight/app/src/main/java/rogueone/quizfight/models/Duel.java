package rogueone.quizfight.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a duel. It stores the ID, the opponent and a list of quizzes representing
 * the rounds. It implements <tt>Serializable</tt> to be easily cast in a <tt>byte[]</tt>.
 *
 * @author Matteo Di Pirro
 * @see Serializable
 */

public class Duel implements Serializable {
    private static final long serialVersionUID = 3830665748594826614L;

    private String duelID;
    private String opponent;
    private List<Quiz> quizzes = new ArrayList<Quiz>(3);

    /**
     * Complete constructor
     * @param duelID Duel ID
     * @param opponent Opponent's Google Games username
     * @param quizzes A list of quizzes
     */
    public Duel (@NonNull String duelID, @NonNull String opponent, @NonNull List<Quiz> quizzes) {
        this.duelID = duelID;
        this.opponent = opponent;
        this.quizzes = quizzes;
    }

    /**
     * Empty duel constructor
     * @param duelID DuelID
     * @param opponent Opponent's Google Games username
     */
    public Duel (@NonNull String duelID, @NonNull String opponent) {
        this(duelID, opponent, new Quiz());
    }

    /**
     * Duel with just one Quiz
     * @param duelID Duel ID
     * @param opponent Opponent's Google Games username
     * @param quiz A Quiz
     */
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

    /**
     * Return a Score object containing the scores of the two players.
     * @return Duel score so far.
     */
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
        return quizzes.get(quizzes.size() - 1);
    }

    public boolean isCompleted() {
        return checkForQuizCompletion() && (quizzes.size() == 3);
    }

    public boolean newRoundAvailable() {
        return checkForQuizCompletion() && (quizzes.size() < 3);
    }

    private boolean checkForQuizCompletion() {
        boolean completed = true;
        for (Quiz quiz : quizzes) {
            completed = completed && quiz.isCompleted();
        }
        return completed;
    }
}
