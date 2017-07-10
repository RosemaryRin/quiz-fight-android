package rogueone.quizfight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import rogueone.quizfight.adapters.DuelDetailAdapter;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.Score;

public class DuelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel_detail);

        Duel duel = (Duel) getIntent().getSerializableExtra("Duel");

        // fight/waiting button
        Button button = (Button) findViewById(R.id.button_dueldetail_fight);
        if (duel.getCurrentQuiz().isPlayed()) {
            button.setText(R.string.dueldetail_waitingforopponent);
            button.setEnabled(false);
        } else {
            button.setText(R.string.dueldetail_fight);
            button.setEnabled(true);
        }

        // opponent name
        TextView opponentName = (TextView) findViewById(R.id.textview_dueldetail_opponent);
        opponentName.setText(duel.getOpponent());

        // global score
        Score score = duel.getScore();
        TextView scoreView = (TextView) findViewById(R.id.textview_dueldetail_globalscore);
        scoreView.setText(score.getPlayerScore() + " - " + score.getOpponentScore());

        // list
        ExpandableListView list = (ExpandableListView) findViewById(R.id.expandablelistview_dueldetail_list);

        DuelDetailAdapter adapter = new DuelDetailAdapter(getLayoutInflater(), duel);
        list.setAdapter(adapter);

        // initially expands all rounds
        for (int i = 0; i < adapter.getGroupCount(); i++)
            list.expandGroup(i);
    }

}
