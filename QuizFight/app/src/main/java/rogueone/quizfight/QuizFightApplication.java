package rogueone.quizfight;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.snapshot.Snapshot;

import rogueone.quizfight.models.History;

/**
 * This class extends the Application class and is used for storing objects and share them across
 * every Activity.
 *
 * @author Matteo Di Pirro
 * @see Application
 */

public class QuizFightApplication extends Application {

    private GoogleApiClient games;
    private History history;

    @Override
    public void onCreate() {
        super.onCreate();
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
}
