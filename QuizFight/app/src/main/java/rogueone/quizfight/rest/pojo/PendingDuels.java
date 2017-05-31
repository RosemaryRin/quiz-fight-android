package rogueone.quizfight.rest.pojo;

import java.util.List;

/**
 * Created by mdipirro on 31/05/17.
 */

public class PendingDuels {
    private List<Duel> pendingDuels;

    public List<Duel> getPendingDuels() {
        return pendingDuels;
    }

    public static class Duel {
        private String duelID;
        private String opponent;
        private boolean[][] answers;

        public  String getDuelID() {
            return duelID;
        }

        public String getOpponent() {
            return opponent;
        }

        public boolean[][] getAnswers() {
            return answers;
        }
    }

}
