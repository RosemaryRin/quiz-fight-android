package rogueone.quizfight;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import rogueone.quizfight.adapters.DuelSummaryAdapter;
import rogueone.quizfight.listeners.DuelDetailListener;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.History;

public class DuelsHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duels_history);

        History history = ((QuizFightApplication)getApplicationContext()).getHistory();

        // if there's at least one old duel hide empty message and show old duels list
        if (history!= null && !history.isEmpty()) {
            final ListView listView = (ListView) findViewById(R.id.listview_duelshistory_history);

            findViewById(R.id.textview_duelshistory_noduels).setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, history.getCompletedDuels());
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new DuelDetailListener(this));
        }
    }
}
