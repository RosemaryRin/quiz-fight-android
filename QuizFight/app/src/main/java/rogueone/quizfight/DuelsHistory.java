package rogueone.quizfight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import rogueone.quizfight.models.SavedGames;

public class DuelsHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duels_history);

        SavedGames savedState = ((QuizFightApplication)getApplicationContext()).getSavedGames();

        // if there's at least one old duel hide empty message and show old duels list
        if (savedState!= null && !savedState.isEmpty()) {
            final ListView listView = (ListView) findViewById(R.id.history);

            findViewById(R.id.noDuels).setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, savedState.getDuels());
            listView.setAdapter(listAdapter);
        }
    }
}
