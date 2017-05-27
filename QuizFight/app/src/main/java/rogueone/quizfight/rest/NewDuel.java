package rogueone.quizfight.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by mdipirro on 27/05/17.
 */

public class NewDuel extends APICaller<ResponseBody> {
    @Override
    protected Call<ResponseBody> getActualMethod() {
        return null;
    }
}
