package rogueone.quizfight.rest;

import android.support.annotation.NonNull;

import com.google.android.gms.games.Games;
import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import rogueone.quizfight.models.User;

/**
 * Created by mdipirro on 19/05/17.
 *
 * The hierarchy instantiate the Template Method pattern. Given an `apiService` one can use the
 * `call` method just providing a Callback object. One must use `call` from a concrete APICaller
 * instance. The latter must implement the `getActualMethod` method, providing the right
 * EndpointInterface REST service.
 */

public abstract class APICaller<T> {
    protected EndpointInterface apiService;

    public APICaller() {
        apiService = RetrofitHelper.getAPIService();
    }

    public void call(@NonNull Callback<T> callback) {
        Call<T> api = getActualMethod();
        api.enqueue(callback);
    }

    protected abstract Call<T> getActualMethod();
}
