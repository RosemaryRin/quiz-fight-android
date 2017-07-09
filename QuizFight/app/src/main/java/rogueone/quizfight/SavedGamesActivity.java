package rogueone.quizfight;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.snapshot.Snapshot;

import java.io.IOException;

import rogueone.quizfight.fragments.SavedGamesFragment;
import rogueone.quizfight.loaders.SavedGamesLoader;
import rogueone.quizfight.models.History;

import static rogueone.quizfight.utils.SavedGames.byteToHistory;

/**
 * This class asks Google Games for retrieving a <tt>Snapshot</tt> representing the user's history.
 * The loading must be started by calling <tt>getGames</tt>. Every subclass should call this method
 * based on its responsibilities and its flow (calling that in <tt>onCreate</tt> could be not
 * appropriate for every situation). The loading is performed using <tt>SavedGamesLoader</tt>.
 * When the loading is completed it calls <tt>onLoadFinished(boolean)</tt>, a template method which
 * every concrete subclass should implements with the operations to be done with the retrieved History.
 *
 * This class stores both the <tt>Snapshot</tt> and the <tt>History</tt>.
 *
 * @author Matteo Di Pirro
 * @see SavedGamesLoader
 * @see Snapshot
 * @see History
 */

public abstract class SavedGamesActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Snapshot> {
    private static final int SAVED_GAMES_LOADER = 1;
    private static final String SAVED_GAMES_FRAGMENT = "SavedGamesFragment";

    private SavedGamesFragment savedGamesFragment;
    protected boolean configurationChanged;

    protected Snapshot snapshot;
    protected History history = new History();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedGamesFragment = (SavedGamesFragment) getFragmentManager().findFragmentByTag(SAVED_GAMES_FRAGMENT);
        Log.d("CONF", savedGamesFragment + "");
        if (savedGamesFragment != null) {
            history = savedGamesFragment.getHistory();
            snapshot = savedGamesFragment.getSnapshot();
            configurationChanged = true;
        } else {
            savedGamesFragment = new SavedGamesFragment();
            getFragmentManager().beginTransaction().add(savedGamesFragment, SAVED_GAMES_FRAGMENT).commit();
        }
    }

    @Override
    public Loader<Snapshot> onCreateLoader(int id, Bundle args) {
        return new SavedGamesLoader(this);
    }

    @Override
    public void onLoaderReset(Loader<Snapshot> loader) {}

    /**
     * A Snapshot has been loaded. It calls <tt>onLoadFinished(true)</tt> iff the loading was
     * successful.
     * @param loader The finished Loader
     * @param snapshot The Snapshot
     */
    @Override
    public void onLoadFinished(Loader<Snapshot> loader, Snapshot snapshot) {
        this.snapshot = snapshot;
        if (snapshot != null && snapshot.getSnapshotContents() != null) {
            try {
                history = byteToHistory(snapshot.getSnapshotContents().readFully());
                savedGamesFragment.setSnapshot(snapshot);
                savedGamesFragment.setHistory(history);
                onLoadFinished(true);
            } catch (IOException e) {
                e.printStackTrace();
                onLoadFinished(false);
            }
        } else {
            onLoadFinished(false);
        }
    }

    /**
     * Starts the loading.
     */
    protected boolean getGames() {
        QuizFightApplication application = (QuizFightApplication) getApplication();
        if (application != null && application.getClient() != null && application.getClient().isConnected()) {
            getLoaderManager().restartLoader(SAVED_GAMES_LOADER, null, this);
            return true;
        }
        return false;
    }

    /**
     * Template method.
     * @param success true iff the loading was successful.
     */
    protected abstract void onLoadFinished(boolean success);

    /**
     * An auxiliary method for handling error situation by showing a <tt>Toast</tt> with an error
     * message.
     * @param message The message to be showed.
     */
    protected void errorToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
