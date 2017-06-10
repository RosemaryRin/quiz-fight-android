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
    private String facebookId;

    public User (@NonNull String username) {
        this.username = username;
    }

    public User (@NonNull String username, @NonNull String facebookId) {
        this(username);
        this.facebookId = facebookId;
    }

    public User (@NonNull String username, @NonNull String token, @NonNull String deviceID) {
        this(username);
        this.token = token;
        this.deviceID = deviceID;
    }

    public String getUsername() {
        return this.username;
    }

}
