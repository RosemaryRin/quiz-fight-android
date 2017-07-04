package rogueone.quizfight.rest;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is a helper class for retrieving a Retrofit instance. It automatically adds a GSON
 * converter for encoding/decoding requests body.
 *
 * @author Matteo Di Pirro
 * @see Retrofit
 */

public class RetrofitHelper {
    private static final String BASE_URL = "https://quiz-fight.herokuapp.com/";

    public static EndpointInterface getAPIService() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(EndpointInterface.class);
    }
}
