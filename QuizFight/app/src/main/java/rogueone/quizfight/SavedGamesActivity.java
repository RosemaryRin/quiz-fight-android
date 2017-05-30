package rogueone.quizfight;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.games.snapshot.Snapshot;

import rogueone.quizfight.loaders.SavedGamesLoader;

/**
 * Created by mdipirro on 30/05/17.
 */

public abstract class SavedGamesActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Snapshot> {
    private static final int SAVED_GAMES_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Loader<Snapshot> onCreateLoader(int id, Bundle args) {
        return new SavedGamesLoader(this);
    }

    @Override
    public void onLoaderReset(Loader<Snapshot> loader) {}

    protected void getGames() {
        getLoaderManager().initLoader(SAVED_GAMES_LOADER, null, this);
    }
}
