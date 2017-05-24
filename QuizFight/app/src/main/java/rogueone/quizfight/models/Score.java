package rogueone.quizfight.models;

import java.io.Serializable;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Score implements Serializable{
    private int playerScore;
    private int opponentScore;

    public Score(int player, int opponent) {
        playerScore = player;
        opponentScore = opponent;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }
}
