package rogueone.quizfight.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rogueone.quizfight.HomeActivity;
import rogueone.quizfight.R;
import rogueone.quizfight.models.User;
import rogueone.quizfight.rest.EndpointInterface;

/**
 * Created by mdipirro on 19/05/17.
 */

public class GoogleAPIHelper implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG     = "GoogleAPIHelper";
    private static final int RC_HELPER = 9002;

    private GoogleApiClient client;
    private Context context;

    public GoogleAPIHelper(Context context) {
        this.context = context;

        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        client.connect();
    }

    public GoogleApiClient getGoogleApiClient() {
        return client;
    }

    public void connect() {
        if (client != null) {
            client.connect();
        }
    }

    public void disconnect() {
        if (client != null && client.isConnected()){
            client.disconnect();
        }
    }

    public boolean isConnected() {
        if (client != null) {
            return client.isConnected();
        } else {
            return false;
        }
    }

    public String getUserEmailAddress() {
        return Games.getCurrentAccountName(client);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EndpointInterface apiService = retrofit.create(EndpointInterface.class);
        Call<ResponseBody> addToken = apiService.addToken(new User(
                getUserEmailAddress(),
                FirebaseInstanceId.getInstance().getToken().toString()
        ));
        addToken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
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
        Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
    }
}
