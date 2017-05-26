package rogueone.quizfight.models.client;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Quiz implements Serializable {
    private List<Question> questions = new LinkedList<>();

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
}
