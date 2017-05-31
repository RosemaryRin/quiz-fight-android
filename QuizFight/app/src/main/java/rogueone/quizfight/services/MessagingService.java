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
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.R;
import rogueone.quizfight.SignInActivity;
import rogueone.quizfight.models.BackgroundDuel;
import rogueone.quizfight.models.RoundCompleted;

import static rogueone.quizfight.NotificationFactory.getTargetActivity;

/**
 * Created by mdipirro on 20/05/17.
 */

public class MessagingService extends FirebaseMessagingService {
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";

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
        String stringID = body.get("id");
        int id = (stringID != null) ? Integer.parseInt(stringID) : 0;
        Intent intent = new Intent(
                this,SignInActivity.class
                /*(((QuizFightApplication)getApplicationContext()).getClient() == null)
                    ? SignInActivity.class
                    : getTargetActivity(id)*/
        );
        populateIntent(body, intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_ONE_SHOT);
        persistData(id, body);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(body.get(TITLE))
                .setContentText(body.get(MESSAGE))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher); //FIXME LOGO!!

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }

    private void populateIntent(@NonNull Map<String, String> body, @NonNull Intent intent) {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            if (!entry.getKey().equals(TITLE) && !entry.getKey().equals(MESSAGE)) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
    }

    private void persistData(int id, @NonNull Map<String, String> body) {
        String duelIDString = getString(R.string.duel_id);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        switch (id) {
            case 2:
                String opponentString = getString(R.string.opponent);
                String pendingString = getString(R.string.pending_duels);

                String jsonPendingDuels = sharedPref.getString(pendingString, "");
                List<BackgroundDuel> duels;
                if (!jsonPendingDuels.equals("")) {
                    Type listType = new TypeToken<List<BackgroundDuel>>(){}.getType();
                    duels = gson.fromJson(jsonPendingDuels, listType);
                } else {
                    duels = new ArrayList<>();
                }
                duels.add(new BackgroundDuel(body.get(opponentString), body.get(duelIDString)));
                editor.putString(pendingString, gson.toJson(duels));
                break;
            case 3:
            case 4:
                String roundsString = getString(R.string.new_rounds);
                String answersString = getString(R.string.answers);
                String jsonRoundCompleted = sharedPref.getString(roundsString, "");
                List<RoundCompleted> rounds;
                if (!jsonRoundCompleted.equals("")) {
                    Type listType = new TypeToken<List<RoundCompleted>>(){}.getType();
                    rounds = gson.fromJson(jsonRoundCompleted, listType);
                } else {
                    rounds = new ArrayList<>();
                }
                boolean[] answers = new boolean[5];
                String[] answersStrings = body.get(answersString).split(",");
                for (int i = 0; i < answersStrings.length; i++) {
                    answers[i] = answersStrings[i].equals("true");
                }
                rounds.add(new RoundCompleted(body.get(duelIDString), answers));
                editor.putString(roundsString, gson.toJson(rounds));
                break;
        }
        editor.apply();
    }
}
