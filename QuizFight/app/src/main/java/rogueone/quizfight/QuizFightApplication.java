package rogueone.quizfight;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by mdipirro on 19/05/17.
 */

public class QuizFightApplication extends Application {

    private GoogleApiClient client;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setClient(@NonNull GoogleApiClient client) {
        this.client = client;
    }

    public GoogleApiClient getClient() {
        return client;
    }
}
