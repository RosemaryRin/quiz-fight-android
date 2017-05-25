package rogueone.quizfight.rest;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mdipirro on 19/05/17.
 *
 * This class is a helper class for retrieving a Retrofit instance.
 */

public class RetrofitHelper {
    private static final String BASE_URL = "https://quiz-fight.herokuapp.com/";

    public static EndpointInterface getAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointInterface.class);
    }
}
