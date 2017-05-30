package rogueone.quizfight.rest.api;

import android.support.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rogueone.quizfight.rest.pojo.RoundResult;

/**
 * Created by mdipirro on 30/05/17.
 */

public class SendRoundScore extends APICaller<ResponseBody> {
    private RoundResult result;

    public SendRoundScore(@NonNull RoundResult result) {
        this.result = result;
    }

    @Override
    protected Call<ResponseBody> getActualMethod() {
        return apiService.sendRoundScore(result);
    }
}
