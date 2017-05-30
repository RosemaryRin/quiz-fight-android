package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Quiz implements Serializable {
    private static final long serialVersionUID = 8581129064427857736L;

    private List<Question> questions = new ArrayList<>(5);

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
}
