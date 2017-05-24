package rogueone.quizfight;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import butterknife.BindView;
import butterknife.ButterKnife;
import rogueone.quizfight.loaders.SavedGamesLoader;
import rogueone.quizfight.models.SavedGames;

public class HomeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<SavedGames> {

    private static final int DUELS_SHOWN = 5;
    private static final int SAVED_GAMES_LOADER = 1;

    private SavedGames savedState;

    @BindView(R.id.UsernameView) TextView username;
    @BindView(R.id.ListTest) ListView oldDuels_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        getLoaderManager().initLoader(SAVED_GAMES_LOADER, null, this);

        // setting username from login
        username.setText(Games.Players.getCurrentPlayer(
                ((QuizFightApplication)getApplicationContext()).getClient()
        ).getDisplayName());

        // duels history button
        View rootView = findViewById(android.R.id.content);
        Button historyButton = (Button) rootView.findViewById(R.id.DuelsHistory);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), DuelsHistory.class));
            }
        });


        // start duel button
        FloatingActionButton startDuelFAB = (FloatingActionButton) rootView.findViewById(R.id.StartDuelFAB);
        startDuelFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), StartDuel.class));
            }
        });
    }

    @Override
    public Loader<SavedGames> onCreateLoader(int id, Bundle args) {
        return new SavedGamesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<SavedGames> loader, SavedGames data) {
        ((QuizFightApplication)getApplicationContext()).setSavedGames(data);
        savedState = data;
        updateUI();
    }

    private void updateUI() {
        if (savedState!= null && !savedState.isEmpty()) {
            findViewById(R.id.NoDuels).setVisibility(View.GONE);
            findViewById(R.id.DuelsHistory).setVisibility(View.VISIBLE);
            oldDuels_listview.setVisibility(View.VISIBLE);
            final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, savedState.getDuels());
            oldDuels_listview.setAdapter(listAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<SavedGames> loader) {}

    //FIXME temporary
    public void signOut(View v) {
        v.setEnabled(false); //prevent another click
        GoogleApiClient client = ((QuizFightApplication)getApplicationContext()).getClient();
        Games.signOut(client);
        client.disconnect();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.signed_in), false);
        editor.apply();
    }
}
