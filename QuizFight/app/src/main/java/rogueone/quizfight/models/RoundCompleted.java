package rogueone.quizfight.models;

import android.support.annotation.NonNull;

/**
 * This is a POJO class used to store information about a new available round. It stores both the
 * duel ID ans an array of <tt>String</tt> representing the opponent's answers. It is used when a
 * new notification is received. <tt>MessagingService</tt> stores the information of a new available
 * round in an object of this class. At the opening (in <tt>SignInActivity</tt>), a
 * <tt>List<RoundCompleted></tt> is used for adding to the history the new rounds.
 * The list is stored in SharedPreference encoded in a JSON object. That makes simple saving and
 * restoring custom object from and to SharedPreferences. The encoding/decoding is performed by Gson.
 *
 * @author Matteo Di Pirro
 * @see rogueone.quizfight.services.MessagingService
 * @see com.google.gson.Gson
 * @see android.content.SharedPreferences
 */

public class RoundCompleted {
    private String duelID;
    private boolean[] answers;

    public RoundCompleted(@NonNull String duelID, boolean[] answers) {
        this.duelID = duelID;
        this.answers = answers;
    }

    public String getDuelID() {
        return duelID;
    }

    public boolean[] getAnswers() {
        return answers;
    }
}
