package rogueone.quizfight.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import rogueone.quizfight.models.client.History;

/**
 * Created by mdipirro on 25/05/17.
 */

public class SavedGames {
    public static History byteToHistory(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        History history = new History();
        try {
            in = new ObjectInputStream(bis);
            history = (History) in.readObject();
        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
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

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {

            }
        }
        return data;
    }

    public static PendingResult<Snapshots.CommitSnapshotResult> writeSnapshot(
            Snapshot snapshot, History history, String desc, GoogleApiClient client
    ) { //TODO Bitmap screenshot?

        // Set the data payload for the snapshot
        snapshot.getSnapshotContents().writeBytes(historyToByte(history));

        // Create the change operation
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .setDescription(desc)
                .build();

        // Commit the operation
        return Games.Snapshots.commitAndClose(client, snapshot, metadataChange);
    }
}
