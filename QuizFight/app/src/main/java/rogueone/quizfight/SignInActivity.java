package rogueone.quizfight;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.rest.AddToken;
import rogueone.quizfight.models.User;
import rogueone.quizfight.utils.BaseGameUtils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by mdipirro on 19/05/17.
 */

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int RESOLUTION = 2404;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();

        ((QuizFightApplication)getApplicationContext()).setClient(client);

        findViewById(R.id.button_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        client.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean(getString(R.string.signed_in), false)) {
            client.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLUTION && resultCode == RESULT_OK){
            client.connect();
        }else{
            Toast.makeText(this, "Not able to connect to google client.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        saveSuccessfulSignIn();
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            new AddToken(new User(
                    Games.getCurrentAccountName(client),
                    token,
                    Secure.getString(getContentResolver(), Secure.ANDROID_ID)
            )).call(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    startHomeActivity();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // TODO
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        client.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(
                    this, connectionResult.getErrorCode(), 101
            ).show();
        } else {
            try {
                connectionResult.startResolutionForResult(this, RESOLUTION);
            } catch (IntentSender.SendIntentException e) {

            }
        }
    }

    /**
     * Start HomeActivity and finish() the current activity. Finishing is necessary to prevent
     * SignInActivity to be created again when the application is in background.
     */
    private void startHomeActivity() {
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    private void saveSuccessfulSignIn() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.signed_in), true);
        editor.apply();
    }
}
