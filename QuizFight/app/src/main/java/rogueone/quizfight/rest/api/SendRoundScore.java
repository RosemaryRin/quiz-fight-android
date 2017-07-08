package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rogueone.quizfight.rest.pojo.RoundResult;

/**
 * This class represents a PUT REST call to /results. It stores a <tt>RoundResult</tt>s corresponding
 * to the round result. Since /results doesn't return any response (i.e. an empty body), <tt>ResponseBody</tt>
 * is used as generic type.
 *
 * @see rogueone.quizfight.rest.api.APICaller
 * @see rogueone.quizfight.rest.EndpointInterface
 * @see RoundResult
 */

public class SendRoundScore extends APICaller<ResponseBody> {
    private RoundResult result;

    public SendRoundScore(@NonNull RoundResult result) {
        super();
        this.result = result;
    }

    @Override
    protected Call<ResponseBody> getActualMethod() {
        return apiService.sendRoundScore(result);
    }
}
