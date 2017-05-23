package rogueone.quizfight.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.Snapshots;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.R;
import rogueone.quizfight.models.SavedGames;

/**
 * Created by mdipirro on 23/05/17.
 */

public class SavedGamesLoader extends AsyncTaskLoader<SavedGames> {

    public SavedGamesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        // implicit call loadInBackground
        forceLoad();
    }

    @Override
    public SavedGames loadInBackground() {
        Snapshots.OpenSnapshotResult result = Games.Snapshots.open(
                ((QuizFightApplication)getContext()).getClient(),
                getContext().getString(R.string.snapshot_name),
                true).await();

        if (result.getStatus().isSuccess()) {
            Snapshot snapshot = result.getSnapshot();
            // Read the byte content of the saved game.
            try {
                return byteToObject(snapshot.getSnapshotContents().readFully());
            } catch (IOException e) {
                return null;
            }
        } else{
            return null;
        }
    }

    private SavedGames byteToObject(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        SavedGames savedGames = new SavedGames();
        try {
            in = new ObjectInputStream(bis);
            savedGames = (SavedGames)in.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {}
        }
        return savedGames;
    }
}
