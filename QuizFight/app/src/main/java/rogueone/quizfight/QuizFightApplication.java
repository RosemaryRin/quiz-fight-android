package rogueone.quizfight;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.snapshot.Snapshot;

import rogueone.quizfight.models.History;

/**
 * Created by mdipirro on 19/05/17.
 */

public class QuizFightApplication extends Application {

    private GoogleApiClient games;
    private History history;
    private Snapshot snapshot;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void setClient(@NonNull GoogleApiClient client) {
        this.games = client;
    }

    public GoogleApiClient getClient() {
        return games;
    }

    public void setHistory(@NonNull History history) {
        this.history = history;
    }

    public History getHistory() {
        return history;
    }

    public void setSnapshot(@NonNull Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }
}
