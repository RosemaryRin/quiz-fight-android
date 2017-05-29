package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Question implements Serializable {
    private static final long serialVersionUID = -5867619978268634461L;

    private String question;
    private String correctAnswer;
    private String playerAnswer;
    private String opponentAnswer;

    public Question(@NonNull String question, @NonNull String answer) {
        this.question = question;
        this.correctAnswer = answer;
    }

    public Question(@NonNull String question, @NonNull String correctAnswer, @NonNull String playerAnswer, @NonNull String opponentAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.playerAnswer = playerAnswer;
        this.opponentAnswer = opponentAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getPlayerAnswer() {
        return playerAnswer;
    }

    public String getOpponentAnswer() {
        return opponentAnswer;
    }

}
