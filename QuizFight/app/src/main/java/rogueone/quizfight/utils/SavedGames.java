package rogueone.quizfight.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotContents;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import rogueone.quizfight.models.History;

/**
 * This class has basically the responsibility to serialize and deserialize a byte[] (corresponding
 * to a raw <tt>History</tt> object) into a structured History object. It also handles the entire
 * write process to a Google Games Snapshot.
 *
 * @author Matteo Di Pirro
 * @see java.io.Serializable
 * @see rogueone.quizfight.loaders.SavedGamesLoader
 */

public class SavedGames {
    /**
     * Given a byte[] get a structured History object.
     * @param data The raw data
     * @return A structured History object.
     */
    public static History byteToHistory(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        History history = new History();
        try {
            in = new ObjectInputStream(bis);
            history = (History) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return history;
    }

    /**
     * Given a structured History object get a raw byte[].
     * @param history The history
     * @return Raw data
     */
    public static byte[] historyToByte(@NonNull History history) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] data = {};
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(history);
            out.flush();
            data = bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {

            }
        }
        return data;
    }

    /**
     * Write to the Snapshot.
     * @param snapshot The snapshot
     * @param history The history to be wrote.
     * @param desc The saving description.
     * @param client The GoogleApiClient representing the logged user.
     * @return The result.
     */
    public static PendingResult<Snapshots.CommitSnapshotResult> writeSnapshot(
            Snapshot snapshot, History history, String desc, GoogleApiClient client
    ) { //TODO Bitmap screenshot?

        // Set the data payload for the snapshot
        SnapshotContents contents = snapshot.getSnapshotContents();
        if (contents != null) {
            contents.writeBytes(historyToByte(history));
            // Create the change operation
            SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                    .setDescription(desc)
                    .build();

            return Games.Snapshots.commitAndClose(client, snapshot, metadataChange);
        } else {
            return null;
        }
    }
}
