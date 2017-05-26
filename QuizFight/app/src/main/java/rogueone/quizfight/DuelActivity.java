package rogueone.quizfight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindArray;

/**
 * Created by mdipirro on 26/05/17.
 */

public class DuelActivity extends AppCompatActivity {

    @BindArray(R.array.topics) private String[] topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // shuffle for getting three random topics to be used during the duel
        // those elements will be the first three in the
        List<String> list = Arrays.asList(topics); list.add(""); // empty string means "Every topic"
        Collections.shuffle(list);

    }
}
