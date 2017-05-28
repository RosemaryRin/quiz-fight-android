package rogueone.quizfight.rest.api;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.Scores;

/**
 * Created by mdipirro on 28/05/17.
 */

public class GetPendingScores extends APICaller<Scores> {
    private String duelID;
    private String playerID;

    public GetPendingScores(String duelID, String user) {
        super();
        this.duelID = duelID;
        playerID = user;
    }

    @Override
    protected Call<Scores> getActualMethod() {
        return apiService.getPendingScores(playerID, duelID);
    }
}
