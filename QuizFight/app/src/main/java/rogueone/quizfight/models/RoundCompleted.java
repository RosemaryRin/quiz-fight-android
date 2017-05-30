package rogueone.quizfight.models;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 30/05/17.
 */

public class RoundCompleted {
    private String duelID;
    private boolean[] answers;

    public RoundCompleted(@NonNull String duelID, boolean[] answers) {
        this.duelID = duelID;
        this.answers = answers;
    }

    public String getDuelID() {
        return duelID;
    }

    public boolean[] getAnswers() {
        return answers;
    }
}
