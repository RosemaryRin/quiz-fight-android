package rogueone.quizfight.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class Score implements Serializable{
    private static final long serialVersionUID = 7455190313761484872L;

    private List<Integer> playerScores = new ArrayList<>(3);
    private List<Integer> opponentScores = new ArrayList<>(3);

    public Score(int player, int opponent) {
        playerScores.add(player);
        opponentScores.add(opponent);
    }

    public Score(@NonNull List<Integer> player, @NonNull List<Integer> opponent) {
        Collections.copy(player, playerScores);
        Collections.copy(opponent, opponentScores);
    }

    public int getPlayerScore() {
        return sum(playerScores);
    }

    public int getOpponentScore() {
        return sum(opponentScores);
    }

    public List<Integer> getPlayerScores() {
        return getScores(playerScores);
    }

    public List<Integer> getOpponentScores() {
        return getScores(opponentScores);
    }

    public void setPlayerScores(@NonNull List<Integer> scores) {
        setScores(scores, playerScores);
    }

    public void setOpponentScores(@NonNull List<Integer> scores) {
        Log.d("SCORES", scores.size() +"");
        setScores(scores, opponentScores);
    }

    public void addRound(int score) {
        if (playerScores.size() < 3) {
            playerScores.add(score);
        }
    }

    private int sum(List<Integer> scores) {
        int sum = 0;
        for(Integer s : scores) {
            sum += s;
        }
        return sum;
    }

    private List<Integer> getScores(@NonNull List<Integer> scores) {
        List<Integer> copy = new ArrayList<>();
        Collections.copy(scores, copy);
        return copy;
    }

    private void setScores(@NonNull List<Integer> source, @NonNull List<Integer> dest) {
        int i = 0;
        for (Integer s : source) {
            Log.d("SCORES", s.toString());
            dest.set(i++, s);
        }
    }
}
