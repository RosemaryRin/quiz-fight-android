package rogueone.quizfight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.fragments.MultipleChoiceFragment;
import rogueone.quizfight.fragments.TrueFalseFragment;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.History;
import rogueone.quizfight.rest.api.GetRound;
import rogueone.quizfight.rest.api.SendRoundScore;
import rogueone.quizfight.rest.pojo.Question;
import rogueone.quizfight.rest.pojo.Round;
import rogueone.quizfight.rest.pojo.RoundResult;
import rogueone.quizfight.utils.SavedGames;

/**
 * Created by mdipirro on 26/05/17.
 */

public class DuelActivity extends SavedGamesActivity {

    private static final int ONE    = 1;
    private static final int TWO    = 2;
    private static final int THREE  = 3;
    private static final int FOUR   = 4;
    private static final int QUESTIONS_PER_ROUND = 5;
    private static final int ALLOWED_TIME = 20000;

    private QuizFightApplication application;
    private Snapshot snapshot;

    private Round round;
    private Question currentQuestion;
    private int count;
    private int score;
    private History history;
    private Duel duel;
    private boolean[] answers;

    private CountDownTimer timer;

    private FragmentManager fragmentManager;
    @BindView(R.id.textview_question) TextView textView_question;
    @BindView(R.id.progressbar_timer) ProgressBar progressBar;
    private TrueFalseFragment trueFalseFragment;
    private MultipleChoiceFragment multipleChoiceFragment;

    @BindString(R.string.round) String roundString;
    @BindString(R.string.duel_id) String duelString;
    @BindString(R.string.unable_to_start_round) String errorRound;
    @BindString(R.string.correct_answers_100) String answers100;
    @BindString(R.string.correct_answers_250) String answers250;
    @BindString(R.string.correct_answers_500) String answers500;
    @BindString(R.string.correct_answers_1000) String answers1000;
    @BindString(R.string.corrects_answers) String correctAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel);
        ButterKnife.bind(this);

        application = (QuizFightApplication)getApplication();
        history = application.getHistory();
        getGames();

        count = 0; score = 0;
        answers = new boolean[5];
    }

    private void setup() {
        Bundle extras = getIntent().getExtras();

        if (extras.containsKey(roundString)) {
            round = extras.getParcelable(roundString);
            initDuel();
        } else {
            new GetRound(
                    extras.getString(duelString),
                    Games.getCurrentAccountName(application.getClient())
            ).call(new Callback<Round>() {
                @Override
                public void onResponse(Call<Round> call, Response<Round> response) {
                    if (response.isSuccessful()) {
                        round = response.body();
                        initDuel();
                    } else {
                        errorToast(errorRound);
                    }
                }

                @Override
                public void onFailure(Call<Round> call, Throwable t) {
                    errorToast(errorRound);
                }
            });
        }
    }

    private void initDuel() {
        duel = history.getDuelByID(round.getDuelID()).get(new Duel(round.getDuelID(), round.getOpponent()));

        setupTimer();
        fragmentManager = getFragmentManager();
        trueFalseFragment = ((TrueFalseFragment)fragmentManager.findFragmentById(R.id.fragment_true_false));
        multipleChoiceFragment = ((MultipleChoiceFragment)fragmentManager.findFragmentById(R.id.fragment_multiple_choice));

        nextQuestion();
    }

    private void setupTimer() {
        timer = new CountDownTimer(ALLOWED_TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int)millisUntilFinished * 100 / ALLOWED_TIME);
            }

            public void onFinish() {
                answer(0);
            }
        }.start();
    }

    private void errorToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void answer(int answer) {
        timer.cancel();
        if (answer == currentQuestion.getAnswer()) {
            // TODO something green

            answers[count] = true;
            // Update achievements
            GoogleApiClient client = application.getClient();
            Games.Achievements.increment(client, answers100, 1);
            Games.Achievements.increment(client, answers250, 1);
            Games.Achievements.increment(client, answers500, 1);
            Games.Achievements.increment(client, answers1000, 1);

            // Update events
            Games.Events.increment(client, correctAnswers, 1);

            score += currentQuestion.getDifficulty();
            saveAnswer(true);
        } else {
            answers[count] = false;
            // TODO something red
            saveAnswer(false);
        }
        count++; // get the next question
        nextQuestion();
    }

    private void saveAnswer(boolean correct) {
        duel.getCurrentQuiz().addQuestion(new rogueone.quizfight.models.Question(currentQuestion.getDifficulty(), correct));
    }

    private void nextQuestion() {
        if (count < QUESTIONS_PER_ROUND) {
            currentQuestion = round.getQuestions().get(count);
            textView_question.setText(currentQuestion.getQuestion());
            if (currentQuestion.isTrueOrFalse()) {
                showHide(true, trueFalseFragment);
                showHide(false, multipleChoiceFragment);

                trueFalseFragment.fillOptions(currentQuestion.getOptions());
            } else {
                showHide(false, trueFalseFragment);
                showHide(true, multipleChoiceFragment);

                multipleChoiceFragment.fillOptions(currentQuestion.getOptions());
            }
            timer.start();
        } else {
            roundTerminated();
        }
    }

    private void roundTerminated() {
        new SendRoundScore(new RoundResult(
                round.getDuelID(), round.getQuizID(),
                Games.Players.getCurrentPlayer(application.getClient()).getDisplayName(),
                answers, score
        )).call(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
        history.setDuelByID(duel);
        SavedGames.writeSnapshot(snapshot, history, "", application.getClient());
        showScoreDialog();
    }

    private void showScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(String.format(getString(R.string.score_dialog), score))
                .setTitle(getString(R.string.round_completed))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(DuelActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHide(boolean show, @NonNull Fragment fragment) {
        if (show) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(fragment)
                    .commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(fragment)
                    .commitAllowingStateLoss();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onLoadFinished(Loader<Snapshot> loader, Snapshot data) {
        snapshot = data;
        setup();
    }
}
