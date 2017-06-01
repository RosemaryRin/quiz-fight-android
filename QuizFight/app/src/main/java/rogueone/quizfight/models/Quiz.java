package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a quiz. It implements <tt>Serializable</tt> to be easily cast in a
 * <tt>byte[]</tt>. It stores a <tt>List</tt> of questions and a boolean flag representing if it has
 * been completed or not.
 *
 * @author Matteo Di Pirro
 * @see Serializable
 */

public class Quiz implements Serializable {
    private static final long serialVersionUID = 4793650144614811370L;

    private List<Question> questions = new ArrayList<>(5);
    private boolean completed;

    public Quiz() {}

    public Quiz(@NonNull List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(@NonNull Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Score getScore() {
        int playerScore = 0, opponentScore = 0;

        for (Question q : questions) {
            playerScore += q.getPlayerScore();
            opponentScore += q.getOpponentScore();
        }

        return new Score(playerScore, opponentScore);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        completed = true;
    }
}
