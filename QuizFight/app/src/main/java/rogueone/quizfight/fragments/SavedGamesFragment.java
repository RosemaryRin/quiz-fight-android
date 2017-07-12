package rogueone.quizfight.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.google.android.gms.games.snapshot.Snapshot;

import rogueone.quizfight.loaders.SavedGamesLoader;
import rogueone.quizfight.models.History;

/**
 * This class implements a headless fragment used to retain a stateful object during configuration
 * changes.
 *
 * @author Matteo Di Pirro
 * @see SavedGamesLoader
 * @see Snapshot
 * @see History
 */

public class SavedGamesFragment extends Fragment {
    private History history;
    private Snapshot snapshot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // retain this fragment
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public History getHistory() {
        return history;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }
}
