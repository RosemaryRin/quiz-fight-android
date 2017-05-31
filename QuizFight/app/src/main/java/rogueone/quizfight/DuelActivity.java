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
import rogueone.quizfight.models.Quiz;
import rogueone.quizfight.rest.api.GetRound;
import rogueone.quizfight.rest.api.SendRoundScore;
import rogueone.quizfight.rest.pojo.Question;
import rogueone.quizfight.rest.pojo.Round;
import rogueone.quizfight.rest.pojo.RoundResult;
import rogueone.quizfight.utils.SavedGames;

/**
 * This class represents the Activity showed for answering to the questions of a certain duel. Since
 * it stores the results in Google Saved Games, it extends <tt>SavedGamesActivity</tt> for automatically
 * starting the loading at the beginning.
 * At the end of the round the results are both sent to the server and saved in the player's history.
 * The user is prompted a dialog showing her score and then redirected th the <tt>HomeActivity</tt>.
 *
 * This Activity may be started with a new duel request, tapping on a notification or on a 'Continue'
 * button in the duel details Activity.
 *
 * @author Matteo Di Pirro
 * @see SavedGamesActivity
 */

public class DuelActivity extends SavedGamesActivity {

    // Constants for the answer IDs
    private static final int ONE    = 1;
    private static final int TWO    = 2;
    private static final int THREE  = 3;
    private static final int FOUR   = 4;

    private static final int QUESTIONS_PER_ROUND = 5;
    private static final int ALLOWED_TIME = 20000;

    private QuizFightApplication application;
    private Snapshot snapshot; // Loaded at the beginning

    private Round round;
    private Question currentQuestion;
    private int count;
    private int score;
    private History history;
    private Duel duel;
    private boolean[] answers;

    private CountDownTimer timer; // The timer

    private FragmentManager fragmentManager;
    @BindView(R.id.textview_question) TextView textView_question;
    @BindView(R.id.progressbar_timer) ProgressBar progressBar;
    private TrueFalseFragment trueFalseFragment;
    private MultipleChoiceFragment multipleChoiceFragment;
    private AlertDialog dialog;

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

    /**
     * Setup every field required for this Activity. It cannot be invoked in onCreate since the Loader
     * must load the user Snapshot.
     */
    private void setup() {
        Bundle extras = getIntent().getExtras();

        if (extras.containsKey(roundString)) { // If the round is provided using Bundles
            round = extras.getParcelable(roundString);
            initDuel();
        } else { // If the round has to be retrieved using a server call
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

    /**
     * Actually begin a new round for the current duel.
     */
    private void initDuel() {
        // The round has been retrieved, do some housekeeping
        duel = history.getDuelByID(round.getDuelID());
        if (duel.getCurrentQuiz().isCompleted() && duel.getQuizzes().size() < 3) {
            duel.addQuiz(new Quiz()); // Add a new quiz if there's a new round
        }

        if (duel != null) { // Everything ok, init the UI
            setupTimer();
            fragmentManager = getFragmentManager();
            trueFalseFragment = ((TrueFalseFragment)fragmentManager.findFragmentById(R.id.fragment_true_false));
            multipleChoiceFragment = ((MultipleChoiceFragment)fragmentManager.findFragmentById(R.id.fragment_multiple_choice));

            nextQuestion();
        } else {
            errorToast(errorRound);
        }
    }

    /**
     * Setup the timer.
     */
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

    /**
     * Show an error <tt>Toast</tt> if something went wrong
     * @param message The message to bhe showed
     */
    private void errorToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle a new answer saving its correctness and updating the UI.
     * @param answer The answer. It may be 0, if the timer expired.
     */
    private void answer(int answer) {
        if (timer != null) {
            timer.cancel(); // Stop the timer
        }
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

    /**
     * Add a new answer to the current question of the current round
     * @param correct
     */
    private void saveAnswer(boolean correct) {
        duel.getCurrentQuiz().addQuestion(new rogueone.quizfight.models.Question(currentQuestion.getDifficulty(), correct));
    }

    /**
     * Show the next question to the user.
     */
    private void nextQuestion() {
        if (count < QUESTIONS_PER_ROUND) { // there are some more questions
            currentQuestion = round.getQuestions().get(count); // get the next question
            textView_question.setText(currentQuestion.getQuestion()); // show that...
            //.. with the correct Fragment
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
        } else { // every question has been answered, final housekeeping
            roundTerminated();
        }
    }

    /**
     * Do the final operations. It shows a <tt>Dialog</tt> with the score and saved the result both
     * client and server side.
     */
    private void roundTerminated() {
        // Call the server for remote saving the result
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
        // Write the snapshot for updating the history
        if (duel.getQuizzes().size() < 3) {
            duel.getCurrentQuiz().complete();
        }
        history.setDuelByID(duel);
        SavedGames.writeSnapshot(snapshot, history, "", application.getClient());

        showScoreDialog();
    }

    /**
     * Show a dialog with the user's score.
     */
    private void showScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(String.format(getString(R.string.score_dialog), score))
                .setTitle(getString(R.string.round_completed))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(DuelActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    /**
     * Show and hide fragments according to the current question type.
     * @param show true if the <tt>Fragment</tt> has to be shown, false if it has to be hide
     * @param fragment The fragment
     */
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

    /**
     * Check the button ID for retrieving the answer.
     * @param v The clicked view
     */
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
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss(); // dismiss the dialog fo avoiding UI leaked exception
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel(); // stop the timer
    }

    /**
     * Loading finished, save the loaded <tt>Snapshot</tt>. It calls <tt>setup</tt> for beginning
     * the duel.
     * @param loader The finished loader
     * @param data The loaded data
     */
    @Override
    public void onLoadFinished(Loader<Snapshot> loader, Snapshot data) {
        snapshot = data;
        setup();
    }
}
