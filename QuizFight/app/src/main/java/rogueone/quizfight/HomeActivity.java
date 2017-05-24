package rogueone.quizfight;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private static final int DUELS_SHOWN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FIXME: get proper data from login and db
        String username = "abeccaro";
        String[] opponents = {"mdipirro", "emanuelec", "rajej", "Pinco Pallo", "Tizio", "Caio", "Sempronio", "Player1234", "Tua mamma", "armir"};
        int[][] scores = {{10,7},{5,9},{10,8},{9,9},{12,8},{10,7},{5,9},{10,8},{9,9},{12,8}};

        // setting username from login
        final TextView usernameView = (TextView) findViewById(R.id.textview_home_username);
        usernameView.setText(username);

        // if there's at least one old duel hide empty message and show old duels list
        if (opponents.length > 0) {
            final ListView listView = (ListView) findViewById(R.id.listview_home_lastduels);

            findViewById(R.id.textview_home_noduels).setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            findViewById(R.id.button_home_duelshistory).setVisibility(View.VISIBLE);

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
        }


        // duels history button
        View rootView = findViewById(android.R.id.content);
        Button historyButton = (Button) rootView.findViewById(R.id.button_home_duelshistory);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), DuelsHistory.class));
            }
        });


        // start duel button
        FloatingActionButton startDuelFAB = (FloatingActionButton) rootView.findViewById(R.id.floatingactionbutton_home_startduel);
        startDuelFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), StartDuel.class));
            }
        });
    }
}
