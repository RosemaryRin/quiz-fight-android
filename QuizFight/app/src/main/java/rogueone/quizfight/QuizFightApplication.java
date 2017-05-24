package rogueone.quizfight;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;

import rogueone.quizfight.models.SavedGames;

/**
 * Created by mdipirro on 19/05/17.
 */

public class QuizFightApplication extends Application {

    private GoogleApiClient games;
    private SavedGames savedGames;

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

    public void setSavedGames(@NonNull SavedGames savedGames) {
        this.savedGames = savedGames;
    }

    public SavedGames getSavedGames() {
        return savedGames;
    }
}
