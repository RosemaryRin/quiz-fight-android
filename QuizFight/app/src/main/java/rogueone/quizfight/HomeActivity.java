package rogueone.quizfight;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        final TextView usernameView = (TextView) findViewById(R.id.UsernameView);
        usernameView.setText(username);

        // if there's at least one old duel hide empty message and show old duels list
        if (opponents.length > 0) {
            findViewById(R.id.NoDuels).setVisibility(View.GONE);
            findViewById(R.id.DuelsHistory).setVisibility(View.VISIBLE);
            findViewById(R.id.ListTest).setVisibility(View.VISIBLE);
        }


        final ListView listview = (ListView) findViewById(R.id.ListTest);

        final DuelSummaryAdapter listAdapter = new DuelSummaryAdapter(this, opponents, scores);
        listview.setAdapter(listAdapter);
    }
}
