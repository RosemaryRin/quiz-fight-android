package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.RESTDuel;
import rogueone.quizfight.rest.pojo.Round;

/**
 * Created by mdipirro on 27/05/17.
 */

public class NewDuel extends APICaller<Round> {
    private RESTDuel duel;

    public NewDuel(@NonNull RESTDuel duel) {
        super();
        this.duel = duel;
    }

    @Override
    protected Call<Round> getActualMethod() {
        return apiService.newDuel(duel);
    }
}
