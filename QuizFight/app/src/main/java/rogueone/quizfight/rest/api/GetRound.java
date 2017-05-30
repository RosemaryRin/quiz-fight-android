package rogueone.quizfight.rest.api;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.Round;

/**
 * Created by mdipirro on 28/05/17.
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
