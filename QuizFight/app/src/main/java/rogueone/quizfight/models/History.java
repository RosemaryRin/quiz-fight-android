package rogueone.quizfight.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mdipirro on 23/05/17.
 */

public class History implements Serializable {
    private static final long serialVersionUID = -5320592455096837718L;

    private List<Duel> duels = new ArrayList<>();

    public List<Duel> getCompletedDuels() {
        List<Duel> inProgress = new ArrayList<>();
        for(Duel duel : duels) {
            if (duel.getQuizzes().size() == 3) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public List<Duel> getCompletedDuels(int n) {
        List<Duel> inProgress = new ArrayList<>();
        Iterator<Duel> iterator = duels.iterator();
        while (iterator.hasNext() && inProgress.size() < n) {
            Duel duel = iterator.next();
            if (duel.getQuizzes().size() == 3) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public List<Duel> getInProgressDuels() {
        List<Duel> inProgress = new ArrayList<>();
        for(Duel duel : duels) {
            if (duel.getQuizzes().size() < 3) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public List<Duel> getInProgressDuels(int n) {
        List<Duel> inProgress = new ArrayList<>();
        Iterator<Duel> iterator = duels.iterator();
        while (iterator.hasNext() && inProgress.size() < n) {
            Duel duel = iterator.next();
            if (duel.getQuizzes().size() < 3) {
                inProgress.add(duel);
            }
        }
        return inProgress;
    }

    public boolean isEmpty() {
        return duels.isEmpty();
    }

    public void addDuel(@NonNull Duel duel) {
        duels.add(duel);
    }
}
