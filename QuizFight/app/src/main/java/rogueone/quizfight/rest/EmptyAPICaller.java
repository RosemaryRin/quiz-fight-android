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
 */

public class EmptyAPICaller extends APICallerInterface<ResponseBody> {
    @Override
    public void call(@NonNull Callback callback) {
        EndpointInterface apiService = getAPIService();

    }
}
