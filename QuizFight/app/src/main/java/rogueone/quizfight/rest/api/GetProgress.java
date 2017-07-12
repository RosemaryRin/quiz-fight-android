package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.PendingDuels;

/**
 * This class represents a GET REST call to /fight. It stores two <tt>String</tt>s corresponding
 * to both the duel and the player's ID. The generic type is <tt>Round</tt>, since a new round is
 * returned by this call.
 *
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see PendingDuels
 */

public class GetProgress extends APICaller<PendingDuels> {
    private String playerID;
    private String duelIDs;

    public GetProgress(@NonNull String player, @NonNull String duelIDs) {
        super();
        playerID = player;
        this.duelIDs = duelIDs;
    }

    @Override
    protected Call<PendingDuels> getActualMethod() {
        return apiService.getProgress(playerID, duelIDs);
    }

}
