package rogueone.quizfight.models;

import java.io.Serializable;

/**
 * This class represents a question. It implements <tt>Serializable</tt> to be easily cast in a
 * <tt>byte[]</tt>. It stores the question difficulty and two boolean flags representing whether
 * the two users answered correctly or not.
 *
 * @author Matteo Di Pirro
 * @see Serializable
 */

public class Question implements Serializable {
    private static final long serialVersionUID = -2864338797321233141L;

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

    public void setOpponentAnswer(boolean answer) {
        opponentAnswer = answer;
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
