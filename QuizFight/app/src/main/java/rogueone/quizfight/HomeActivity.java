package rogueone.quizfight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.adapters.DuelSummaryAdapter;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.Question;
import rogueone.quizfight.rest.api.GetProgress;
import rogueone.quizfight.rest.pojo.PendingDuels;
import rogueone.quizfight.utils.SavedGames;

/**
 * This class shows the current situation to the user (pending and completed duels). It allows her
 * to sign out and to start a new duel. At the very beginning and at every resume it asks the server
 * for updates both in pending and new duels.
 *
 * @author Alex Beccaro
 * @see SavedGamesActivity
 */
public class HomeActivity extends SavedGamesActivity {

    private static final int DUELS_SHOWN = 5;

    private QuizFightApplication application;

    private boolean signOutClicked = false;

    @BindView(R.id.textview_home_username) TextView username;
    @BindView(R.id.listview_home_lastduels) ListView oldDuels_listview;
    @BindView(R.id.imageview_profile) ImageView userProfileImage;
    @BindView(R.id.listview_home_duels_in_progress) ListView duelsInProgress_listview;

    @BindString(R.string.unable_to_get_pending_duels) String callError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        application = (QuizFightApplication)getApplicationContext();

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

        getGames();
    }

    /**
     * Get updates from the server. If there are new duels or new rounds available write them to
     * history, otherwise do nothing.
     */
    private void updatePendingDuels() {
        String duelIDs = "";
        for (Duel duel : history.getInProgressDuels()) {
            duelIDs += duel.getDuelID() + ",";
        }
        // If there are no pending duels, send a dummy string just to fill in the parameter
        duelIDs = (duelIDs.length() - 1 > 0) ? duelIDs.substring(0, duelIDs.length() - 1) : "dummy";
        new GetProgress(
                Games.Players.getCurrentPlayer(application.getClient()).getDisplayName(),
                duelIDs
        ).call(new Callback<PendingDuels>() {
            @Override
            public void onResponse(Call<PendingDuels> call, Response<PendingDuels> response) {
                if (response.isSuccessful()) {
                    for (PendingDuels.Duel pendingDuel : response.body().getPendingDuels()) {
                        Duel duel = history.getDuelByID(pendingDuel.getDuelID());
                        if (duel != null) { //existing duel
                            // Get the indexes for updating an existing duel
                            int index = 0, currentQuizIndex = duel.getQuizzes().size() - 1;
                            // If the player completed the round (1st condition) and if the opponent
                            // answered (2nd condition) save the result
                            if (currentQuizIndex < pendingDuel.getAnswers().length &&
                                    index < pendingDuel.getAnswers()[currentQuizIndex].length) {
                                // For each new question save the opponent's answer
                                for (Question question : duel.getCurrentQuiz().getQuestions()) {
                                    question.setOpponentAnswer(pendingDuel.getAnswers()[currentQuizIndex][index++]);
                                }
                                // If both the two players completed the duel, sets it as complete
                                if (duel.getQuizzes().size() == 3) {
                                    duel.getCurrentQuiz().complete();
                                }
                                history.setDuelByID(duel);
                            }
                        } else { // new duel
                            history.addDuel(new Duel(pendingDuel.getDuelID(), pendingDuel.getOpponent()));
                        }
                    }
                    SavedGames.writeSnapshot(snapshot, history, "", application.getClient());
                    updateHistory();
                } else {
                    errorToast(callError);
                }
            }

            @Override
            public void onFailure(Call<PendingDuels> call, Throwable t) {
                t.printStackTrace();
                errorToast(callError);
            }
        });
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
        getGames();
    }

    /* TODO To be tested
    @Override
    public void onDestroy() {
        super.onDestroy();
        application.getClient().disconnect();
    }*/

    //FIXME temporary
    public void signOut(View v) {
        if (!signOutClicked) {
            signOutClicked = true;
            GoogleApiClient client = application.getClient();
            Games.signOut(client);
            client.disconnect();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.signed_in), false);
            editor.apply();

            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    @Override
    protected void onLoadFinished(boolean success) {
        if (success) {
            updatePendingDuels();
        } else {
            errorToast(callError);
        }
    }
}
