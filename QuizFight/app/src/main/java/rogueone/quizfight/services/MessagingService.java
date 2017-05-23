package rogueone.quizfight.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import rogueone.quizfight.R;

import static rogueone.quizfight.NotificationFactory.getTargetActivity;

/**
 * Created by mdipirro on 20/05/17.
 */

public class MessagingService extends FirebaseMessagingService {
    private static final String title = "title";
    private static final String message = "message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> body = remoteMessage.getData();
        if (body != null) {
            if (body.containsKey(title) && body.containsKey(message)) {
                sendNotification(body);
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param body FCM body received
     */
    private void sendNotification(@NonNull Map<String, String> body) {
        int id = Integer.parseInt(body.get("id"));
        Intent intent = new Intent(this, getTargetActivity(id));
        populateIntent(body, intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(body.get(title))
                .setContentText(body.get(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }

    private void populateIntent(@NonNull Map<String, String> body, @NonNull Intent intent) {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            if (!entry.getKey().equals(title) && !entry.getKey().equals(message)) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
    }
}
