package rogueone.quizfight.fragments;

import android.support.annotation.NonNull;

import java.util.List;

import rogueone.quizfight.rest.pojo.Option;

/**
 * Created by mdipirro on 27/05/17.
 */

public interface QuestionFragment {
    void fillOptions(@NonNull List<Option> options);
}
