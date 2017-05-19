package rogueone.quizfight.models;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 19/05/17.
 */

public class User {
    private String username;
    private String token;

    public User (@NonNull String username, @NonNull String token) {
        this.username = username;
        this.token = token;
    }
}
