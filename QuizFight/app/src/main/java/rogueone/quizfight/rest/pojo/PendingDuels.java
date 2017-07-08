package rogueone.quizfight.rest.pojo;

import java.util.List;

/**
 * This class represents a pending duel. It is used when HomeActivity asks the server for new duels
 * or changes in the pending ones. A JSON with this type is returned. It is just a container of
 * <tt>Duel</tt>s (see below).
 *
 * @author Matteo Di Pirro
 */

public class PendingDuels {
    private List<Duel> pendingDuels;

    public List<Duel> getPendingDuels() {
        return pendingDuels;
    }

    /**
     * This private class actually contains the information about the pending duels.
     */
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
