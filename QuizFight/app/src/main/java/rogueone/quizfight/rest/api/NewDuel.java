package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.RESTDuel;
import rogueone.quizfight.rest.pojo.Round;

/**
 * This class represents a POST REST call to /fight. It stores a <tt>RESTDuel</tt>s corresponding
 * to every server-side required information. The generic type is <tt>Round</tt>, since a new round is
 * returned by this call.
 *
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see Round
 * @see RESTDuel
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
