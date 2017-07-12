package rogueone.quizfight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import rogueone.quizfight.adapters.DuelDetailAdapter;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.Quiz;
import rogueone.quizfight.models.Score;

public class DuelDetailActivity extends AppCompatActivity {

    String duelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel_detail);

        duelId = getResources().getString(R.string.duel_id);

        final Duel duel = (Duel) getIntent().getSerializableExtra("Duel");
        final Quiz currentQuiz = duel.getCurrentQuiz();

        // fight/waiting button
        Button button = (Button) findViewById(R.id.button_dueldetail_fight);

        if (duel.getQuizzes().size() < 3 || !currentQuiz.isCompleted()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DuelDetailActivity.this, DuelActivity.class);

                    intent.putExtra(duelId, duel.getDuelID());

                    startActivity(intent);
                }
            });
            if (currentQuiz.isCompleted() || !currentQuiz.isPlayed()){
                button.setText(R.string.dueldetail_fight);
                button.setEnabled(true);
            } else {
                button.setText(R.string.dueldetail_waitingforopponent);
                button.setEnabled(false);
            }
        } else
            button.setVisibility(View.GONE);

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
