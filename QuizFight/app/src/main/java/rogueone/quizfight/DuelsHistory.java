package rogueone.quizfight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class DuelsHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duels_history);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //FIXME: get proper data from login and db
        String username = "abeccaro";
        String[] opponents = {"mdipirro", "emanuelec", "rajej", "Pinco Pallo", "Tizio", "Caio", "Sempronio", "Player1234", "Tua mamma", "armir"};
        int[][] scores = {{10,7},{5,9},{10,8},{9,9},{12,8},{10,7},{5,9},{10,8},{9,9},{12,8}};

        // if there's at least one old duel hide empty message and show old duels list
        if (opponents.length > 0) {
            //FIXME
            /*final ListView listView = (ListView) findViewById(R.id.history);

            findViewById(R.id.noDuels).setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, opponents, scores);
            listView.setAdapter(listAdapter);*/
        }
    }
}
