package rogueone.quizfight;

import butterknife.BindString;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.rest.api.AddToken;
import rogueone.quizfight.rest.api.GetProgress;
import rogueone.quizfight.rest.pojo.PendingDuels;
import rogueone.quizfight.rest.pojo.User;
import rogueone.quizfight.services.MessagingService;
import rogueone.quizfight.utils.SavedGames;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import static rogueone.quizfight.NotificationFactory.getTargetActivity;

/**
 * This class performs Google Games sign in. It sends the user's token to the server after a
 * successful sign in, loads pending duels and starts HomeActivity after the loading.
 *
 * @author Matteo Di Pirro
 */

public class SignInActivity extends SavedGamesActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Snapshot> {

    private static final int RESOLUTION = 2404;

    ProgressBar mProgressBar;

    private GoogleApiClient client;

    @BindString(R.string.unable_to_restore_saved_games) String savedGamesError;
    @BindString(R.string.unable_to_connect) String connectionError;
    @BindString(R.string.duels_played) String duelsPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setContentView(R.layout.activity_sign_in);

        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar0);

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
                if (client != null && client.isConnected()) {
                    client.clearDefaultAccountAndReconnect();
                }
                mProgressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        MessagingService.resetNotificationCount();
        ShortcutBadger.applyCount(this, 0);
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
            errorToast(connectionError);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        saveSuccessfulSignIn();
        String token = FirebaseInstanceId.getInstance().getToken();
        getGames();
        if (token != null) {
            new AddToken(new User(
                    Games.Players.getCurrentPlayer(client).getDisplayName(),
                    token,
                    Secure.getString(getContentResolver(), Secure.ANDROID_ID)
            )).call(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {}
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
            } catch (IntentSender.SendIntentException e) {}
        }
    }

    @Override
    protected void onLoadFinished(boolean success) {
        if (success) {
            updatePendingDuels();
            //application.setHistory(history);
            //SavedGames.writeSnapshot(snapshot, new History(), "", client);
            /*addPendingDuelsIfExist(history);
            addCompletedRoundScores(history);
            SavedGames.writeSnapshot(snapshot, history, "", client);
            application.setHistory(history);*/
        } else {
            errorToast(savedGamesError);
        }
    }

    /**
     * Add new duels data so that if the user taps on the notification she is able to play the first
     * round.
     */
    private void updatePendingDuels() {
        final QuizFightApplication application = (QuizFightApplication)getApplication();
        new GetProgress(
                Games.Players.getCurrentPlayer(application.getClient()).getDisplayName(),
                "dummy" // a dummy string just to fill in the parameter
        ).call(new Callback<PendingDuels>() {
            @Override
            public void onResponse(Call<PendingDuels> call, Response<PendingDuels> response) {
                if (response.isSuccessful()) {
                    for (PendingDuels.Duel pendingDuel : response.body().getPendingDuels()) {
                        if (history.getDuelByID(pendingDuel.getDuelID()) == null) {
                            Games.Events.increment(application.getClient(), duelsPlayed, 1);
                            history.addDuel(new Duel(pendingDuel.getDuelID(), pendingDuel.getOpponent()));
                        }
                    }
                    SavedGames.writeSnapshot(snapshot, history, "", application.getClient());
                    mProgressBar.setVisibility(View.GONE);
                    startHomeActivity();
                } else {
                    errorToast(savedGamesError);
                }
            }

            @Override
            public void onFailure(Call<PendingDuels> call, Throwable t) {
                t.printStackTrace();
                errorToast(savedGamesError);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Snapshot> loader) {}

    /**
     * Start HomeActivity and finish() the current activity. Finishing is necessary to prevent
     * SignInActivity to be created again when the application is in background.
     */
    private void startHomeActivity() {
        if (client.isConnected()) {
            Intent intent;
            Bundle bundle = getIntent().getExtras();
            if (bundle == null || bundle.get("id") == null) {
                intent = new Intent(SignInActivity.this, HomeActivity.class);
            } else {
                String stringID = bundle.getString("id");
                int id = (stringID != null) ? Integer.parseInt(stringID) : 0;
                intent = new Intent(SignInActivity.this, getTargetActivity(id));
                intent.putExtras(bundle);
            }
            finish();
            startActivity(intent);
        } else {
            client.connect();
        }
    }

    /**
     * Save a successful sign in in the <tt>SharedPreference</tt>s so that a silent sign in can be
     * performed if the user reopen the application without signing out.
     */
    private void saveSuccessfulSignIn() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.signed_in), true);
        editor.apply();
    }
}