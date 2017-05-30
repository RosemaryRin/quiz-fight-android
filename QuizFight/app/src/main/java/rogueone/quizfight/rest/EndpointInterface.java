package rogueone.quizfight.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rogueone.quizfight.rest.pojo.RESTDuel;
import rogueone.quizfight.rest.pojo.Round;
import rogueone.quizfight.rest.pojo.RoundResult;
import rogueone.quizfight.rest.pojo.Scores;
import rogueone.quizfight.rest.pojo.User;

/**
 * Created by mdipirro on 19/05/17.
 */

public interface EndpointInterface {
    @Headers({"Content-Type: application/json"})
    @PUT("user")
    Call<ResponseBody> addToken(@Body User user);

    @Headers({"Content-Type: application/json"})
    @POST("fight")
    Call<Round> newDuel(@Body RESTDuel user);

    @Headers({"Content-Type: application/json"})
    @GET("scores/{playerID}/{duelID}")
    Call<Scores> getPendingScores(@Path("playerID") String player, @Path("duelID") String duels);

    @Headers({"Content-Type: application/json"})
    @GET("fight/{playerID}/{duelID}")
    Call<Round> getRound(@Path("playerID") String player, @Path("duelID") String duels);

    @Headers({"Content-Type: application/json"})
    @PUT("results")
    Call<ResponseBody> sendRoundScore(@Body RoundResult result);
}
