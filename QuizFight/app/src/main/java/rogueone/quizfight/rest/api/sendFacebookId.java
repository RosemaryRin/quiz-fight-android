package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import retrofit2.Call;
import rogueone.quizfight.rest.pojo.User;

/**
 * Created by Emanuele on 02/06/2017.
 *
 * This class represents a PUT REST call to /users/:googleUsername/:facebookId that updates a user
 * with googleUsername with the correspondent facebookId.
 *
 * @author Emanuele Carraro
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see rogueone.quizfight.rest.pojo.User
 */

public class sendFacebookId extends APICaller<User> {

    private String googleUsername;
    private String facebookId;

    public sendFacebookId(@NonNull String googleUsername, @NonNull String facebookId) {
        super();
        this.googleUsername = googleUsername;
        this.facebookId = facebookId;
    }

    @Override
    protected Call<User> getActualMethod() {
        return apiService.sendFacebookId(googleUsername, facebookId);
    }

}
