package rogueone.quizfight;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FIXME: get proper data from login and db
        String username = "abeccaro";
        String[] opponents = {"mdipirro", "emanuelec", "rajej", "pincoPallo", "Tizio"};
        int[][] scores = {{10,7},{5,9},{10,8},{9,9},{12,8}};

        // setting username from login
        TextView usernameView = (TextView) findViewById(R.id.UsernameView);
        usernameView.setText(username);

        // if there's at least one old duel hide empty message and show old duels list
        if (opponents.length > 0) {
            findViewById(R.id.NoDuels).setVisibility(View.GONE);
            findViewById(R.id.LastDuels).setVisibility(View.VISIBLE);
            findViewById(R.id.DuelsHistory).setVisibility(View.VISIBLE);
        }

        // for each of the five last duels set relative overview
        for (int i = 0; i < opponents.length; i++) {
            Resources res = getResources();

            // getting views
            int id = res.getIdentifier("LastDuel"+i+"Icon", "id", getApplicationContext().getPackageName());
            ImageView icon = (ImageView) findViewById(id);
            id = res.getIdentifier("LastDuel"+i+"User", "id", getApplicationContext().getPackageName());
            TextView opponent = (TextView) findViewById(id);
            id = res.getIdentifier("LastDuel"+i+"Score", "id", getApplicationContext().getPackageName());
            TextView score = (TextView) findViewById(id);

            // setting win/loss/tie icon, opponent name and score
            if (scores[i][0] > scores[i][1])
                icon.setImageResource(R.drawable.victory);
            else if (scores[i][0] == scores[i][1])
                icon.setImageResource(R.drawable.tie);
            else
                icon.setImageResource(R.drawable.defeat);
            opponent.setText(opponents[i]);
            score.setText(scores[i][0]+" - "+scores[i][1]);
        }
    }
}
