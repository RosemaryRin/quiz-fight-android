package rogueone.quizfight.rest.api;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.Round;

/**
 * This class represents a GET REST call to /fight. It stores two <tt>String</tt>s corresponding
 * to both the duel and the player's ID. The generic type is <tt>Round</tt>, since a new round is
 * returned by this call.
 *
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see Round
 */

public class GetRound extends APICaller<Round> {
    private String duelID;
    private String playerID;

    public GetRound(String duelID, String user) {
        super();
        this.duelID = duelID;
        playerID = user;
    }

    @Override
    protected Call<Round> getActualMethod() {
        return apiService.getRound(playerID, duelID);
    }
}
