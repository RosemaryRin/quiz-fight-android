package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class History implements Serializable {
    private List<Duel> duels = new ArrayList<>();

    public List getDuels() {
        return duels;
    }

    public List getDuels(int n) {
        if (n < duels.size()) {
            return duels.subList(0, n);
        } else {
            return duels;
        }
    }

    public boolean isEmpty() {
        return duels.isEmpty();
    }

    public void addDuel(@NonNull Duel duel) {
        duels.add(duel);
    }
}
