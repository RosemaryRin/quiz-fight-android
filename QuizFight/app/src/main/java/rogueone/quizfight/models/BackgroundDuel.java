package rogueone.quizfight.models;

import android.support.annotation.NonNull;

/**
 * This is a POJO class used to store information about a new dare. It stores both the opponent name
 * and the duel ID. It is used when a new notification is received. <tt>MessagingService</tt> stores
 * the information of a new dare in an object of this class. At the opening (in <tt>SignInActivity</tt>),
 * a <tt>List<BackgroundDuel></tt> is used for adding to the history the new duels.
 * The list is stored in SharedPreference encoded in a JSON object. That makes simple saving and
 * restoring custom object from and to SharedPreferences. The encoding/decoding is performed by Gson.
 *
 * @author Matteo Di Pirro
 * @see rogueone.quizfight.services.MessagingService
 * @see com.google.gson.Gson
 * @see android.content.SharedPreferences
 */

public class BackgroundDuel {
    private String opponent;
    private String duelID;

    public BackgroundDuel(@NonNull String opponent, @NonNull String duelID) {
        this.opponent = opponent;
        this.duelID = duelID;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getDuelID() {
        return duelID;
    }
}
