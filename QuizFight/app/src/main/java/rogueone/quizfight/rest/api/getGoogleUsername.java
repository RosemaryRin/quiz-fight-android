package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.User;

/**
 * Created by Emanuele on 02/06/2017.
 *
 * This class represents a GET REST call to /users/:facebookId that returns
 * a user google username given the facebookId
 *
 * @author Emanuele Carraro
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see rogueone.quizfight.rest.pojo.User
 */

public class getGoogleUsername extends APICaller<User> {

    private String facebookId;

    public getGoogleUsername(@NonNull String facebookId) {
        super();
        this.facebookId = facebookId;
    }

    @Override
    protected Call<User> getActualMethod() {
        return apiService.getGoogleUsername(facebookId);
    }

}
