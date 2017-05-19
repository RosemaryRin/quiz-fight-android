package rogueone.quizfight;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rogueone.quizfight.rest.EndpointInterface;
import rogueone.quizfight.models.User;
import rogueone.quizfight.utils.BaseGameUtils;
import rogueone.quizfight.utils.GoogleAPIHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by mdipirro on 19/05/17.
 */

public class SignInActivity extends AppCompatActivity {

    private static final String TAG     = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleAPIHelper client;

    private boolean resolvingConnectionFailure  = false;
    private boolean autoStartSignInFlow         = true;
    private boolean signInClicked               = false;
    private boolean inSignInFlow                = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        client = ((QuizFightApplication)getApplicationContext()).getGoogleAPIHelper();
    }

    public void signIn(View v) {
        signInClicked = true;
        inSignInFlow = true;
        client.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!inSignInFlow) {
            // auto sign in
            inSignInFlow = true;
            client.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            client.connect();
        }
    }
}
