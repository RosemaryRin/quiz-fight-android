package rogueone.quizfight.fragments;

import android.support.annotation.NonNull;

import java.util.List;

import rogueone.quizfight.models.Question;

/**
 * Created by mdipirro on 27/05/17.
 */

public interface QuestionFragment {
    void fillOptions(@NonNull List<Question> questions);
}
