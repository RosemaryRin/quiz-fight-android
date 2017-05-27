package rogueone.quizfight.rest;

import android.support.annotation.NonNull;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.Duel;
import rogueone.quizfight.rest.pojo.Round;

/**
 * Created by mdipirro on 27/05/17.
 */

public class NewDuel extends APICaller<Round> {
    private Duel duel;

    public NewDuel(@NonNull Duel duel) {
        super();
        this.duel = duel;
    }

    @Override
    protected Call<Round> getActualMethod() {
        return apiService.newDuel(duel);
    }
}
