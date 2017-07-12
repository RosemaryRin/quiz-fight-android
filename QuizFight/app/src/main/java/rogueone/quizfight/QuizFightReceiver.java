package rogueone.quizfight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rogueone.quizfight.services.CustomFirebaseInstanceID;
import rogueone.quizfight.services.MessagingService;

/**
 * This class is a BroadcastReceiver and receives the boot event. At that time it starts two services
 * for updating the user's token and receiving notifications.
 */

public class QuizFightReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent instanceIDService = new Intent(context, CustomFirebaseInstanceID.class);
        Intent notificationService = new Intent(context, MessagingService.class);
        context.startService(instanceIDService);
        context.startService(notificationService);

    }
}