package rogueone.quizfight;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

        //FIXME: get proper data from login and db
        String[] opponents = {"mdipirro", "emanuelec", "rajej", "Pinco Pallo", "Tizio", "Caio", "Sempronio", "Player1234", "Tua mamma", "armir"};
        int[][] scores = {{10,7},{5,9},{10,8},{9,9},{12,8},{10,7},{5,9},{10,8},{9,9},{12,8}};

        // setting username from login
        username.setText(Games.Players.getCurrentPlayer(
                ((QuizFightApplication)getApplicationContext()).getClient()
        ).getDisplayName());

        // if there's at least one old duel hide empty message and show old duels list
        /*if (opponents.length > 0) {



            // initializing showed data arrays
            String[] opponentsShown = new String[DUELS_SHOWN];
            int[][] scoresShown = new int[DUELS_SHOWN][2];
            if (opponents.length < DUELS_SHOWN) {
                opponentsShown = opponents;
                scoresShown = scores;
            }
            else {
                System.arraycopy(opponents, 0, opponentsShown, 0, DUELS_SHOWN);
                System.arraycopy(scores, 0, scoresShown, 0, DUELS_SHOWN);
            }

            final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, opponentsShown, scoresShown);
            listView.setAdapter(listAdapter);
        }*/


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
}
