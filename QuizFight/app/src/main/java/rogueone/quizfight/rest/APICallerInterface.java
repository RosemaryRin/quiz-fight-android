package rogueone.quizfight.rest;

import android.support.annotation.NonNull;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rogueone.quizfight.R;

/**
 * Created by mdipirro on 19/05/17.
 */

public abstract class APICallerInterface<T> {
    private static final String BASE_URL = "https://quiz-fight.herokuapp.com/";

    protected EndpointInterface getAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointInterface.class);
    }

    public abstract void call(@NonNull Callback<T> callback);
}
