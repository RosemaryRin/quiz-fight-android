package rogueone.quizfight.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.Snapshots;

import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.R;

/**
 * This class loads the user's history from her private Google Drive. It handles conflicts always
 * restoring the most recent <tt>Snapshot</tt>. A limited number of attempts is done for solving
 * conflicts. If everything succeeded, the <tt>Snapshot</tt> is returned in the <tt>onLoadFinished</tt>
 * <tt>Loader</tt> callback.
 *
 * @author Matteo Di Pirro
 * @see Snapshot
 * @see AsyncTaskLoader
 */

public class SavedGamesLoader extends AsyncTaskLoader<Snapshot> {

    private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 3; // the number of attempts

    public SavedGamesLoader(Context context) {
        super(context);
    }

    /**
     * Start the asynchronous loading (in background).
     */
    @Override
    protected void onStartLoading() {
        // implicit call loadInBackground
        forceLoad();
    }

    /**
     * Load the <tt>Snapshot</tt> from Drive.
     * @return The <tt>Snapshot</tt> containing user's history. It may be null if something went
     * wrong.
     */
    @Override
    public Snapshot loadInBackground() {
        // Open the snapshot
        Snapshots.OpenSnapshotResult result = Games.Snapshots.open(
                ((QuizFightApplication)getContext()).getClient(),
                getContext().getString(R.string.snapshot_name),
                true).await();
        // Look for conflicts
        return processSnapshotOpenResult(result, 0);
    }

    /**
     * Handle the possibility of conflicts.
     * @param result The opened <tt>Snapshot</tt>
     * @param retryCount Attempts to go
     * @return The <tt>Snapshot</tt> @see loadInBackground
     */
    private Snapshot processSnapshotOpenResult(Snapshots.OpenSnapshotResult result, int retryCount) {
        Snapshot snapshot;

        int status = result.getStatus().getStatusCode();

        if (status == GamesStatusCodes.STATUS_OK) {
            return result.getSnapshot(); // everything ok
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE) {
            return result.getSnapshot(); // nothing was found
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT) { // conflict
            Snapshot currentSnapshot = result.getSnapshot();
            Snapshot conflictSnapshot = result.getConflictingSnapshot();

            snapshot = (currentSnapshot.getMetadata().getLastModifiedTimestamp() <
                    conflictSnapshot.getMetadata().getLastModifiedTimestamp()) ?
                        conflictSnapshot :
                        currentSnapshot; // select the most recent

            Snapshots.OpenSnapshotResult resolveResult = Games.Snapshots.resolveConflict(
                    ((QuizFightApplication)getContext()).getClient(),
                    result.getConflictId(),
                    snapshot).await();

            if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES) {
                return processSnapshotOpenResult(resolveResult, retryCount + 1);
            } else {
                // Failed, log error and show Toast to the user
                Toast.makeText(getContext(), getContext().getString(R.string.snapshot_error), Toast.LENGTH_LONG).show();
            }

        }
        // Fail, return null.
        return null;
    }
}
