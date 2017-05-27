package rogueone.quizfight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import rogueone.quizfight.fragments.MultipleChoiceFragment;
import rogueone.quizfight.fragments.TrueFalseFragment;
import rogueone.quizfight.rest.pojo.Question;
import rogueone.quizfight.rest.pojo.Round;

/**
 * Created by mdipirro on 26/05/17.
 */

public class DuelActivity extends AppCompatActivity {

    private static final int ONE    = 1;
    private static final int TWO    = 2;
    private static final int THREE  = 3;
    private static final int FOUR   = 4;
    private static final int QUESTIONS_PER_ROUND = 5;

    private Round round;
    private Question currentQuestion;
    private int count;
    private int score;

    private FragmentManager fragmentManager;
    private TextView textView_question;
    private TrueFalseFragment trueFalseFragment;
    private MultipleChoiceFragment multipleChoiceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel);

        count = 0; score = 0;
        round = getIntent().getExtras().getParcelable(getString(R.string.round));
        if (round == null) {
            // TODO API call to retrieve round info
        }
        fragmentManager = getFragmentManager();
        textView_question = ((TextView)findViewById(R.id.textview_question));
        trueFalseFragment = ((TrueFalseFragment)fragmentManager.findFragmentById(R.id.fragment_true_false));
        multipleChoiceFragment = ((MultipleChoiceFragment)fragmentManager.findFragmentById(R.id.fragment_multiple_choice));

        nextQuestion();
    }

    private void answer(int answer) {
        if (answer == currentQuestion.getAnswer()) {
            // TODO something green
            score += currentQuestion.getDifficulty();
        } else {
            // TODO something red
        }
        nextQuestion();
    }

    private void nextQuestion() {
        if (count < QUESTIONS_PER_ROUND) {
            currentQuestion = round.getQuestions().get(count++);
            textView_question.setText(currentQuestion.getQuestion());
            if (currentQuestion.isTrueOrFalse()) {
                showHide(true, trueFalseFragment);
                showHide(false, multipleChoiceFragment);
            } else {
                showHide(false, trueFalseFragment);
                showHide(true, multipleChoiceFragment);
            }
        }
    }

    private void showHide(boolean show, @NonNull Fragment fragment) {
        if (show) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(fragment)
                    .commit();
        }
    }

    public void getAnswer(View v) {
        switch (v.getId()) {
            case R.id.button_choice_one:
            case R.id.button_tf_one:
                answer(ONE);
                break;
            case R.id.button_choice_two:
            case R.id.button_tf_two:
                answer(TWO);
                break;
            case R.id.button_choice_three:
                answer(THREE);
                break;
            case R.id.button_choice_four:
                answer(FOUR);
                break;
        }
    }
}
