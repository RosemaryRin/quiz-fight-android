package rogueone.quizfight.fragments;

import android.support.annotation.NonNull;

import java.util.List;

import rogueone.quizfight.rest.pojo.Option;

/**
 * This interface contains just one method, <tt>fillOptions</tt>. It has to be used for filling the
 * answers options in <b>DuelActivity</b>. Since there is a variable number of answers, every concrete
 * instance should populate its layout accordingly. That should be done in <tt>fillOptions</tt>.
 *
 * @author Matteo Di Pirro
 */

public interface QuestionFragment {
    /**
     * Correctly fill the answer options.
     * @param options A <tt>List</tt> of <tt>Options</tt> to be used for populating the layout.
     *                @see Option
     */
    void fillOptions(@NonNull List<Option> options);
}
