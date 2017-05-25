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
 * Created by mdipirro on 23/05/17.
 */

public class SavedGamesLoader extends AsyncTaskLoader<Snapshot> {

    private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 3;

    public SavedGamesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        // implicit call loadInBackground
        forceLoad();
    }

    @Override
    public Snapshot loadInBackground() {
        Snapshots.OpenSnapshotResult result = Games.Snapshots.open(
                ((QuizFightApplication)getContext()).getClient(),
                getContext().getString(R.string.snapshot_name),
                true).await();
        return processSnapshotOpenResult(result, 0);
    }

    private Snapshot processSnapshotOpenResult(Snapshots.OpenSnapshotResult result, int retryCount) {
        Snapshot snapshot = null;
        retryCount++;

        int status = result.getStatus().getStatusCode();

        if (status == GamesStatusCodes.STATUS_OK) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT) {
            Snapshot currentSnapshot = result.getSnapshot();
            Snapshot conflictSnapshot = result.getConflictingSnapshot();

            snapshot = (currentSnapshot.getMetadata().getLastModifiedTimestamp() <
                    conflictSnapshot.getMetadata().getLastModifiedTimestamp()) ?
                        conflictSnapshot :
                        currentSnapshot;

            Snapshots.OpenSnapshotResult resolveResult = Games.Snapshots.resolveConflict(
                    ((QuizFightApplication)getContext()).getClient(),
                    result.getConflictId(),
                    snapshot).await();

            if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES) {
                return processSnapshotOpenResult(resolveResult, retryCount);
            } else {
                // Failed, log error and show Toast to the user
                String message = "Could not resolve snapshot conflicts";
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }

        }
        // Fail, return null.
        return null;
    }
}
