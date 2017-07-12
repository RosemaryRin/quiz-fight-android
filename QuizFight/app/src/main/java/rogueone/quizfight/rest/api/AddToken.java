package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rogueone.quizfight.rest.pojo.User;

/**
 * This class represents a PUT REST call to /users. It stores a <tt>User</tt> with every server-side
 * required information. Since /users doesn't return any response (i.e. an empty body), <tt>ResponseBody</tt>
 * is used as generic type.
 *
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see User
 */

public class AddToken extends APICaller<ResponseBody> {
    private User user;

    public AddToken (@NonNull User user) {
        super();
        this.user = user;
    }

    @Override
    protected Call<ResponseBody> getActualMethod() {
        return apiService.addToken(user);
    }
}
