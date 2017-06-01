package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * This class represents a User. It stores her username, and both the device token (used for Firebase
 * Cloud Messaging) and its unique ID (user for identifying a token update).
 *
 * @author Matteo Di Pirro
 */

public class User {
    private String username;
    private String token;
    private String deviceID;

    public User (@NonNull String username, @NonNull String token, @NonNull String deviceID) {
        this.username = username;
        this.token = token;
        this.deviceID = deviceID;
    }
}
