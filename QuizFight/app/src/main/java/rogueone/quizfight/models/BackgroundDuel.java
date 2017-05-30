package rogueone.quizfight.models;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 30/05/17.
 *
 * This is a POJO class used to store information about a new dare. It stores both the opponent name
 * and the duel ID.
 */

public class BackgroundDuel {
    private String opponent;
    private String duelID;

    public BackgroundDuel(@NonNull String opponent, @NonNull String duelID) {
        this.opponent = opponent;
        this.duelID = duelID;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getDuelID() {
        return duelID;
    }
}
