package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * Created by mdipirro on 27/05/17.
 */

public class RESTDuel {
    private String user1;
    private String user2;
    private String user1Username;
    private String[] topics;

    public RESTDuel(@NonNull String user1, @NonNull String user1Username, @NonNull String[] topics) {
        new RESTDuel(user1, null, user1Username, topics);
    }

    public RESTDuel(@NonNull String user1, String user2,
                    @NonNull String user1Username, @NonNull String[] topics) {
        this.user1 = user1;
        this.user2 = user2;
        this.user1Username = user1Username;
        this.topics = topics;
    }
}
