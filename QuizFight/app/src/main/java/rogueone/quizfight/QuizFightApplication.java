package rogueone.quizfight;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

import rogueone.quizfight.utils.GoogleAPIHelper;

/**
 * Created by mdipirro on 19/05/17.
 */

public class QuizFightApplication extends Application {

    private GoogleAPIHelper gHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        gHelper = new GoogleAPIHelper(this);
    }

    public GoogleAPIHelper getGoogleAPIHelper() {
        return gHelper;
    }
}
