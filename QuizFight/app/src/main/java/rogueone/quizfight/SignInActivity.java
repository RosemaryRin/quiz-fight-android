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

import android.content.Intent;
import android.os.Bundle;
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

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG     = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        ((QuizFightApplication)getApplicationContext()).setClient(client);
    }

    public void signIn(View v) {
        client.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            startHomeActivity();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EndpointInterface apiService = retrofit.create(EndpointInterface.class);
        Call<ResponseBody> addToken = apiService.addToken(new User(
                Games.getCurrentAccountName(client),
                FirebaseInstanceId.getInstance().getToken().toString()
        ));
        addToken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                startHomeActivity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        BaseGameUtils.resolveConnectionFailure(this, client, connectionResult,
                    RC_SIGN_IN, R.string.signin_other_error);

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
}
