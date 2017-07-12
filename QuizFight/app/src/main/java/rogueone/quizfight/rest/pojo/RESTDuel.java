package rogueone.quizfight.rest.pojo;

import android.support.annotation.NonNull;

/**
 * This class represents a request for a new Duel to be sent to the server. It contains the current
 * player, the opponent (possibly <tt>null</tt> --> random) and a list of topics.
 *
 * @author Matteo Di Pirro
 */

public class RESTDuel {
    private String user1;
    private String user2;
    private String[] topics;

    public RESTDuel(@NonNull String user1, @NonNull String[] topics) {
        new RESTDuel(user1, null, topics);
    }

    public RESTDuel(@NonNull String user1, String user2, @NonNull String[] topics) {
        this.user1 = user1;
        this.user2 = user2;
        this.topics = topics;
    }
}
