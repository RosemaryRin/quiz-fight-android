package rogueone.quizfight;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This class extends the Application class and is used for storing objects and share them across
 * every Activity.
 *
 * @author Matteo Di Pirro
 * @see Application
 */

public class QuizFightApplication extends Application {

    private GoogleApiClient games;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void setClient(@NonNull GoogleApiClient client) {
        this.games = client;
    }

    public GoogleApiClient getClient() {
        return games;
    }

    /**
     * General function to check connection
     * @param context
     */
    public boolean checkConnection(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            status = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}