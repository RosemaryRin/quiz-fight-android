package rogueone.quizfight;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.loaders.SavedGamesLoader;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.History;
import rogueone.quizfight.rest.api.AddToken;
import rogueone.quizfight.rest.api.GetPendingScores;
import rogueone.quizfight.rest.pojo.Scores;
import rogueone.quizfight.rest.pojo.User;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static rogueone.quizfight.NotificationFactory.getTargetActivity;
import static rogueone.quizfight.utils.SavedGames.byteToHistory;
import static rogueone.quizfight.utils.ScoresHelper.addPendingDuelsIfExist;

/**
 * Created by mdipirro on 19/05/17.
 */

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Snapshot> {

    private static final int RESOLUTION = 2404;
    private static final int SAVED_GAMES_LOADER = 1;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

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
                if (client != null && client.isConnected()) {
                    client.clearDefaultAccountAndReconnect();
                }
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
            errorToast(getApplicationContext().getString(R.string.unable_to_connect));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        saveSuccessfulSignIn();
        getLoaderManager().initLoader(SAVED_GAMES_LOADER, null, this);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            new AddToken(new User(
                    Games.getCurrentAccountName(client),
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
    public Loader<Snapshot> onCreateLoader(int id, Bundle args) {
        return new SavedGamesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Snapshot> loader, Snapshot snapshot) {
        if (snapshot != null) {
            QuizFightApplication application = (QuizFightApplication)getApplicationContext();
            application.setSnapshot(snapshot);
            // Read the byte content of the saved game.
            try {
                History history = byteToHistory(snapshot.getSnapshotContents().readFully());
                addPendingDuelsIfExist(this, history);
                updateScores(history, snapshot, application);
            } catch (IOException e) {
                errorToast(getApplicationContext().getString(R.string.unable_to_restore_saved_games));
            }
        } else {
            errorToast(getApplicationContext().getString(R.string.unable_to_restore_saved_games));
        }
    }

    private void errorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void updateScores(@NonNull final History history, @NonNull final Snapshot snapshot,
                              @NonNull final QuizFightApplication application) {
        final List<Duel> inProgress = history.getInProgressDuels();
        if (inProgress.size() > 0) {
            String ids = "";
            for (Duel duel : inProgress) {
                ids += duel.getDuelID() + ",";
            }
            ids = ids.substring(0, ids.length() - 1);
            new GetPendingScores(ids, Games.getCurrentAccountName(client)).call(new Callback<Scores>() {
                @Override
                public void onResponse(Call<Scores> call, Response<Scores> response) {
                    if (response.isSuccessful()) {
                        Iterator<List<Integer>> iterator = response.body().getDuelsScores().iterator();
                        for (Duel duel : inProgress) {
                            if (iterator.hasNext()) {
                                duel.getScore().setOpponentScores(iterator.next());
                            }
                        }
                        SavedGames.writeSnapshot(snapshot, history, "", client);
                        application.setHistory(history);
                    }
                    startHomeActivity();
                }

                @Override
                public void onFailure(Call<Scores> call, Throwable t) {}
            });
        } else {
            startHomeActivity();
        }
    }

    @Override
    public void onLoaderReset(Loader<Snapshot> loader) {}

    /**
     * Start HomeActivity and finish() the current activity. Finishing is necessary to prevent
     * SignInActivity to be created again when the application is in background.
     */
    private void startHomeActivity() {
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
    }

    private void saveSuccessfulSignIn() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.signed_in), true);
        editor.apply();
    }
}
