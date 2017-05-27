package rogueone.quizfight.models;

import java.io.Serializable;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Score implements Serializable{
    private static final long serialVersionUID = 2321585485734097863L;

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
