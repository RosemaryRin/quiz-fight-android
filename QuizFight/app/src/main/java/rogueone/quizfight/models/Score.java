package rogueone.quizfight.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Score implements Serializable {
    private static final long serialVersionUID = -3923326187155594196L;

    private int player;
    private int opponent;

    public Score(int player, int opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    public int getPlayerScore() {
        return player;
    }

    public int getOpponentScore() {
        return opponent;
    }
}
