package rogueone.quizfight.services;

import android.provider.Settings;

import com.google.android.gms.games.Games;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.rest.pojo.User;
import rogueone.quizfight.rest.api.AddToken;


/**
 * This service handles the token changes. Every time a token changes (e.g. new installation / cache
 * wipe) it sends the new value to the server (so that he user can go on receiving notifications).
 *
 * @author Matteo Di Pirro
 * @see FirebaseInstanceIdService
 */

public class CustomFirebaseInstanceID extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        QuizFightApplication context = (QuizFightApplication)getApplicationContext();
        if (context.getClient() != null && context.getClient().isConnected()) {
            new AddToken(new User(
                    Games.getCurrentAccountName(context.getClient()),
                    token,
                    Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
            )).call(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {}
            });
        }
    }
}
