package rogueone.quizfight.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindString;
import me.leolin.shortcutbadger.ShortcutBadger;
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.R;
import rogueone.quizfight.SignInActivity;
import rogueone.quizfight.models.BackgroundDuel;
import rogueone.quizfight.models.RoundCompleted;

import static rogueone.quizfight.NotificationFactory.getTargetActivity;

/**
 * This service receives every notification which has been sent to the user corresponding to the
 * current token. It basically parses the notification body and shows a new notification. CLicking on
 * this notification will open different activities based on the notification ID.
 *
 * @author Matteo Di Pirro
 * @see FirebaseMessagingService
 */

public class MessagingService extends FirebaseMessagingService {
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";

    private static int notificationCount = 0;

    public static void resetNotificationCount() {
        notificationCount = 0;
    }

    /**
     * If the message contains a title and a message show the proper notification.
     *
     * @param remoteMessage The message which has been received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> body = remoteMessage.getData();
        if (body != null) {
            if (body.containsKey(TITLE) && body.containsKey(MESSAGE)) {
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
        notificationCount++;
        String stringID = body.get("id");
        int id = (stringID != null) ? Integer.parseInt(stringID) : 0;
        Intent intent = new Intent(
                this,SignInActivity.class
                /*(((QuizFightApplication)getApplicationContext()).getClient() == null)
                    ? SignInActivity.class
                    : getTargetActivity(id)*/
        );
        populateIntent(body, intent); // add every field in the notification to the Intent
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Compose the notification
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(body.get(TITLE))
                .setContentText(body.get(MESSAGE))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.logo);

        ShortcutBadger.applyCount(getApplicationContext(), notificationCount);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }

    /**
     * Parse the message body and add every field (but <tt>title</tt> and <tt>message</tt> to the
     * Intent object.
     * @param body Message body.
     * @param intent New activity Intent.
     */
    private void populateIntent(@NonNull Map<String, String> body, @NonNull Intent intent) {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            if (!entry.getKey().equals(TITLE) && !entry.getKey().equals(MESSAGE)) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
    }
}
