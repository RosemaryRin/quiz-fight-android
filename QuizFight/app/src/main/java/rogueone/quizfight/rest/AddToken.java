package rogueone.quizfight.rest;

import android.support.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rogueone.quizfight.models.User;

/**
 * Created by mdipirro on 23/05/17.
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
