package rogueone.quizfight;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rogueone.quizfight.adapters.DuelSummaryAdapter;
import rogueone.quizfight.listeners.DuelDetailListener;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.History;
import rogueone.quizfight.models.Question;
import rogueone.quizfight.models.Quiz;
import rogueone.quizfight.models.Score;

import static rogueone.quizfight.utils.SavedGames.writeSnapshot;

public class HomeActivity extends AppCompatActivity {

    private static final int DUELS_SHOWN = 5;

    private History history;
    private QuizFightApplication application;

    @BindView(R.id.textview_home_username) TextView username;
    @BindView(R.id.listview_home_lastduels) ListView oldDuels_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        application = (QuizFightApplication)getApplicationContext();
        history = application.getHistory();

//        Question q1 = new Question("Question 1", "Answer 1"),
//                q2 = new Question("Question 2", "Answer 2");
//        Quiz quiz1 = new Quiz(), quiz2 = new Quiz();
//        quiz1.addQuestion(q1); quiz1.addQuestion(q2);
//        quiz2.addQuestion(q1); quiz2.addQuestion(q2);
//        List<Quiz> quizzes = new ArrayList<Quiz>(2);
//        quizzes.add(quiz1);
//        quizzes.add(quiz2);
//        Duel duel = new Duel("Matteo", new Score(10,9), quizzes);
//        Duel duel1 = new Duel("Emanuele", new Score(8, 7), quizzes);
//        Duel duel2 = new Duel("Rajej", new Score(2, 7), quizzes);
//        Duel duel3 = new Duel("Milly Maietti", new Score(0, 0), quizzes);
//        Duel duel4 = new Duel("Maestro Tullio", new Score(0, 2000), quizzes);
//        Duel duel5 = new Duel("Bresolin", new Score(5, 0), quizzes);
//        history.addDuel(duel); history.addDuel(duel1); history.addDuel(duel2); history.addDuel(duel3);
//        history.addDuel(duel4); history.addDuel(duel5);
//        writeSnapshot(application.getSnapshot(), history, "First write", application.getClient());

        // setting username from login
        username.setText(Games.Players.getCurrentPlayer(
                application.getClient()
        ).getDisplayName());

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

    private void updateHistory() {
        if (history!= null && !history.isEmpty()) {
            findViewById(R.id.textview_home_noduels).setVisibility(View.GONE);
            findViewById(R.id.button_home_duelshistory).setVisibility(View.VISIBLE);
            oldDuels_listview.setVisibility(View.VISIBLE);
            final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, history.getDuels(DUELS_SHOWN));
            oldDuels_listview.setAdapter(listAdapter);
            oldDuels_listview.setOnItemClickListener(new DuelDetailListener(this));
        }
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
