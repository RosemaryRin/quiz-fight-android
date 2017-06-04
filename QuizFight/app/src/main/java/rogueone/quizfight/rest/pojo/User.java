package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 19/05/17.
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
