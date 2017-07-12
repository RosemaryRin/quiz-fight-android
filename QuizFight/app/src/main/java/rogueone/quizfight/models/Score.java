package rogueone.quizfight.models;

import java.io.Serializable;


/**
 * This class represents a score.
 *
 * @author Matteo Di Pirro
 */

public class Score {

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
