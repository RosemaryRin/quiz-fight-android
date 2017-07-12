package rogueone.quizfight.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rogueone.quizfight.rest.pojo.PendingDuels;
import rogueone.quizfight.rest.pojo.RESTDuel;
import rogueone.quizfight.rest.pojo.Round;
import rogueone.quizfight.rest.pojo.RoundResult;
import rogueone.quizfight.rest.pojo.User;

/**
 * This interface contains the signatures of every server call, using Retrofit annotations.
 *
 * @author Matteo Di Pirro
 * @see retrofit2.Retrofit
 */

public interface EndpointInterface {
    @Headers({"Content-Type: application/json"})
    @PUT("user")
    Call<ResponseBody> addToken(@Body User user);

    @Headers({"Content-Type: application/json"})
    @POST("fight")
    Call<Round> newDuel(@Body RESTDuel user);

    @Headers({"Content-Type: application/json"})
    @GET("fight/{playerID}/{duelID}")
    Call<Round> getRound(@Path("playerID") String player, @Path("duelID") String duels);

    @Headers({"Content-Type: application/json"})
    @PUT("result")
    Call<ResponseBody> sendRoundScore(@Body RoundResult result);

    @Headers({"Content-Type: application/json"})
    @PUT("users/{googleUsername}/{facebookId}")
    Call<User> sendFacebookId(@Path("googleUsername") String googleUsername,
                                      @Path("facebookId") String facebookId);

    @Headers({"Content-Type: application/json"})
    @GET("users/{facebookId}")
    Call<User> getGoogleUsername(@Path("facebookId") String facebookId);

    @GET("result/{playerID}/{ids}")
    Call<PendingDuels> getProgress(@Path("playerID") String player, @Path("ids") String duelIDs);
}
