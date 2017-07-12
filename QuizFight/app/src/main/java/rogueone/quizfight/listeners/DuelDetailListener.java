package rogueone.quizfight.listeners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import rogueone.quizfight.DuelDetailActivity;
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.models.Duel;

/**
 * Created by Becks on 27/05/17.
 */

public class DuelDetailListener implements AdapterView.OnItemClickListener {

    private Activity activity;

    public DuelDetailListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Duel duel = (Duel) adapter.getItemAtPosition(position);

        Intent intent = new Intent(activity, DuelDetailActivity.class);
        intent.putExtra("Duel", duel);
        //based on item add info to intent
        view.getContext().startActivity(intent);
    }
}
