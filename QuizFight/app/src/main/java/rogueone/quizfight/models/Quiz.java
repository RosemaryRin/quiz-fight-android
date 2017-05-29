package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Quiz implements Serializable {
    private static final long serialVersionUID = 8581129064427857736L;

    private List<Question> questions = new LinkedList<Question>();

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
            String correctAnswer = q.getCorrectAnswer();

            if (q.getPlayerAnswer() == correctAnswer)
                playerScore++;

            if (q.getOpponentAnswer() == correctAnswer)
                opponentScore++;
        }

        return new Score(playerScore, opponentScore);
    }
}
