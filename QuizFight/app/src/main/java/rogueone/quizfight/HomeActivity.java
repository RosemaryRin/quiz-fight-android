package rogueone.quizfight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.adapters.DuelSummaryAdapter;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.History;
import rogueone.quizfight.rest.api.sendFacebookId;
import rogueone.quizfight.rest.pojo.User;

public class HomeActivity extends AppCompatActivity {

    private static final int DUELS_SHOWN = 5;
    private static final String TAG = "HomeActivity";

    private History history;
    private QuizFightApplication application;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;

    @BindView(R.id.textview_home_username) TextView username;
    @BindView(R.id.listview_home_lastduels) ListView oldDuels_listview;
    @BindView(R.id.imageview_profile) ImageView userProfileImage;
    @BindView(R.id.listview_home_duels_in_progress) ListView duelsInProgress_listview;
    @BindView(R.id.login_button) LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        application = (QuizFightApplication)getApplicationContext();
        history = application.getHistory();
        loginButton.setReadPermissions("email", "user_friends");

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook login request success");
                accessToken = AccessToken.getCurrentAccessToken();
                new sendFacebookId(
                        Games.Players.getCurrentPlayer(application.getClient()).getDisplayName(),
                        Profile.getCurrentProfile().getId())
                        .call(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Log.d(TAG, response.message());
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login request Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Facebook login request Error");
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };

        // setting username from login
        username.setText(Games.Players.getCurrentPlayer(
                application.getClient()
        ).getDisplayName());

        // setting user image from login
        ImageManager mgr = ImageManager.create(this);
        mgr.loadImage(userProfileImage, Games.Players.getCurrentPlayer(application.getClient()).getIconImageUri());

        // duels history button
        View rootView = findViewById(android.R.id.content);
        Button historyButton = (Button) rootView.findViewById(R.id.button_home_duelshistory);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), DuelsHistoryActivity.class));
            }
        });


        // start duel button
        FloatingActionButton startDuelFAB = (FloatingActionButton) rootView.findViewById(R.id.floatingactionbutton_home_startduel);
        startDuelFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), StartDuelActivity.class));
            }
        });

        updateHistory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateHistory() {
        if (history != null && !history.isEmpty()) {
            List<Duel> completedDuels = history.getCompletedDuels(DUELS_SHOWN);
            List<Duel> duelsInProgress = history.getInProgressDuels(DUELS_SHOWN);
            if (completedDuels.size() > 0) {
                findViewById(R.id.textview_home_noduels).setVisibility(View.GONE);
                findViewById(R.id.button_home_duelshistory).setVisibility(View.VISIBLE);
                oldDuels_listview.setVisibility(View.VISIBLE);
                final DuelSummaryAdapter complAdapter = new DuelSummaryAdapter(this, completedDuels);
                oldDuels_listview.setAdapter(complAdapter);
                complAdapter.notifyDataSetChanged();
            }
            if (duelsInProgress.size() > 0) {
                findViewById(R.id.textview_home_no_duels_in_progress).setVisibility(View.GONE);
                duelsInProgress_listview.setVisibility(View.VISIBLE);
                final DuelSummaryAdapter progAdapter = new DuelSummaryAdapter(this, duelsInProgress);
                duelsInProgress_listview.setAdapter(progAdapter);
                progAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HOME","onResume");
        history = application.getHistory();
        updateHistory();
    }

    //FIXME temporary
    public void signOut(View v) {
        v.setEnabled(false); //prevent another click
        GoogleApiClient client = application.getClient();
        Games.signOut(client);
        client.disconnect();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.signed_in), false);
        editor.apply();
    }
}