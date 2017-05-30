package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Question implements Serializable {


    private static final long serialVersionUID = 8631839101984474191L;
    private int difficulty;
    private boolean playerAnswer;
    private boolean opponentAnswer;

    public Question(int difficulty) {
        this(difficulty, false, false);
    }

    public Question(int difficulty, boolean player, boolean opponent) {
        this.difficulty = normalizeDifficulty(difficulty);
        this.playerAnswer = player;
        this.opponentAnswer = opponent;
    }

    public Question(int difficulty, boolean player) {
        this(difficulty, player, false);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean getPlayerAnswer() {
        return playerAnswer;
    }

    public boolean getOpponentAnswer() {
        return opponentAnswer;
    }

    private int normalizeDifficulty(int difficulty) {
        if (difficulty < 1) {
            return 1;
        } else if (difficulty > 3) {
            return 3;
        } else {
            return  difficulty;
        }
    }

    public int getPlayerScore() {
        return getScore(playerAnswer);
    }

    public int getOpponentScore() {
        return getScore(opponentAnswer);
    }

    private int getScore(boolean correct) {
        return (correct) ? difficulty : 0;
    }
}
