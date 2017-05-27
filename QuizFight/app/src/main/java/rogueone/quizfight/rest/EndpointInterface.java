package rogueone.quizfight.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<ResponseBody> newDuel(@Body User user);
}
