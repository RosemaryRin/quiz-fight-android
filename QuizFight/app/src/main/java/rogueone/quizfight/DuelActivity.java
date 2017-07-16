package rogueone.quizfight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.IOException;
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

    private Round round;
    private Question currentQuestion;
    private int count;
    private int score;
    private Duel duel;
    private boolean[] answers;

    private long availableTime;

    private CountDownTimer timer; // The timer

    private FragmentManager fragmentManager;
    @BindView(R.id.textview_question) TextView textView_question;
    @BindView(R.id.progressbar_duel_difficultybar) ProgressBar progressBar_difficulty;
    @BindView(R.id.progressbar_timer) ProgressBar progressBar;
    private TrueFalseFragment trueFalseFragment;
    private MultipleChoiceFragment multipleChoiceFragment;
    private AlertDialog dialog;

    @BindString(R.string.round) String roundString;
    @BindString(R.string.duel_id) String duelString;
    @BindString(R.string.count) String countString;
    @BindString(R.string.score) String scoreString;
    @BindString(R.string.answers) String answersString;
    @BindString(R.string.available_time) String availableTimeString;
    @BindString(R.string.unable_to_start_round) String errorRound;
    @BindString(R.string.correct_answers_100) String answers100;
    @BindString(R.string.correct_answers_250) String answers250;
    @BindString(R.string.correct_answers_500) String answers500;
    @BindString(R.string.correct_answers_1000) String answers1000;
    @BindString(R.string.score_15_points) String points15;
    @BindString(R.string.score_30_points) String points30;
    @BindString(R.string.score_45_points) String points45;
    @BindString(R.string.correct_answers) String correctAnswers;
    @BindString(R.string.rounds_played) String roundsPlayed;
    @BindString(R.string.questions_answered) String questionsAnswered;
    @BindString(R.string.leaderboard_id) String leaderboardId;
    @BindString(R.string.correct_answer) String correctAnswer;
    @BindString(R.string.incorrect_answer) String incorrectAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel);
        ButterKnife.bind(this);

        availableTime = ALLOWED_TIME;

        application = (QuizFightApplication)getApplication();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if (savedInstanceState != null) {
            configurationChanged = true;

            count = savedInstanceState.getInt(countString);
            score = savedInstanceState.getInt(scoreString);
            answers = savedInstanceState.getBooleanArray(answersString);
            round = savedInstanceState.getParcelable(roundString);
            availableTime = savedInstanceState.getLong(availableTimeString);

            getGames();
        } else {
            getGames();

            count = 0; score = 0;
            answers = new boolean[5];
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(countString, count);
        outState.putInt(scoreString, score);
        outState.putBooleanArray(answersString, answers);
        outState.putParcelable(roundString, round);
        outState.putLong(availableTimeString, availableTime);
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
                    t.printStackTrace();
                    errorToast(errorRound);
                }
            }, application);
        }
    }

    /**
     * Actually begin a new round for the current duel.
     */
    private void initDuel() {
        // The round has been retrieved, do some housekeeping
        duel = history.getDuelByID(round.getDuelID());
        if (duel.getCurrentQuiz().isCompleted() && duel.getQuizzes().size() < 3 && !configurationChanged) {
            Games.Events.increment(application.getClient(), roundsPlayed, 1);
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
        timer = new CountDownTimer(availableTime, 1000) {
            public void onTick(long millisUntilFinished) {
                availableTime = millisUntilFinished;
                progressBar.setProgress((int)millisUntilFinished * 100 / ALLOWED_TIME);
            }

            public void onFinish() {
                answer(0, -1);
            }
        }.start();
    }

    /**
     * Handle a new answer saving its correctness and updating the UI.
     * @param answer The answer. It may be 0, if the timer expired.
     */
    private void answer(int answer, int id) {
        GoogleApiClient client = application.getClient();
        Games.Events.increment(client, questionsAnswered, 1);
        if (timer != null) {
            timer.cancel(); // Stop the timer
            availableTime = ALLOWED_TIME;
        }
        if (id != -1) {
            Button selected = (Button) findViewById(id);
            if (answer == currentQuestion.getAnswer()) {
                selected.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.won_duel),
                        PorterDuff.Mode.MULTIPLY);
                answers[count] = true;
                // Update achievements
                Games.Achievements.increment(client, answers100, 1);
                Games.Achievements.increment(client, answers250, 1);
                Games.Achievements.increment(client, answers500, 1);
                Games.Achievements.increment(client, answers1000, 1);

                // Update events
                Games.Events.increment(client, correctAnswers, 1);

                score += currentQuestion.getDifficulty();
                saveAnswer(true);
            } else {
                selected.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.lost_duel),
                        PorterDuff.Mode.MULTIPLY);
                answers[count] = false;
                saveAnswer(false);
            }
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

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do this after 1s = 1000ms
                    // difficulty
                    int diff = currentQuestion.getDifficulty();
                    if (diff <= 1) {
                        progressBar_difficulty.setProgress(1);
                        progressBar_difficulty.getProgressDrawable().setColorFilter(
                                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    else if (diff == 2) {
                        progressBar_difficulty.setProgress(2);
                        progressBar_difficulty.getProgressDrawable().setColorFilter(
                                Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
                    } else {
                        progressBar_difficulty.setProgress(3);
                        progressBar_difficulty.getProgressDrawable().setColorFilter(
                                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                    }
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
                }
            }, 1000);
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
        // setting round played by player
        duel.getCurrentQuiz().played();
        // Call the server for remote saving the result
        new SendRoundScore(new RoundResult(
                round.getDuelID(), round.getQuizID(),
                Games.Players.getCurrentPlayer(application.getClient()).getDisplayName(),
                answers, score
        )).call(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("Debug", "" + response.body().string());
                } catch (IOException e) {
                    Log.d("Debug", "Exception occurred");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        }, application);
        // Write the snapshot for updating the history
        // < 3 is needed because otherwise the duel would appear as complete. @see HomeActivity.updatePendingDuels
        // There the duel is marked as complete if the opponent completed it as well.
        if (duel.getQuizzes().size() == 3) {
            // Duel completed, let's check if an achievement may be updated
            GoogleApiClient client = application.getClient();
            if (score == 45) {
                Games.Achievements.increment(client, points15, 1);
                Games.Achievements.increment(client, points30, 1);
                Games.Achievements.increment(client, points45, 1);
            } else if (score >= 30) {
                Games.Achievements.increment(client, points15, 1);
                Games.Achievements.increment(client, points30, 1);
            } else if (score >= 15) {
                Games.Achievements.increment(client, points15, 1);
            } // otherwise no achievements will be updated
        }
        history.setDuelByID(duel);
        SavedGames.writeSnapshot(snapshot, history, "", application.getClient());

        // adding score to leaderboard
        Games.Leaderboards.submitScore(application.getClient(), leaderboardId, history.getTotalPlayerScore());

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
        int view_id = v.getId();
        switch (view_id) {
            case R.id.button_choice_one:
            case R.id.button_tf_one:
                answer(ONE, view_id);
                break;
            case R.id.button_choice_two:
            case R.id.button_tf_two:
                answer(TWO, view_id);
                break;
            case R.id.button_choice_three:
                answer(THREE, view_id);
                break;
            case R.id.button_choice_four:
                answer(FOUR, view_id);
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
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel(); // stop the timer
        }
    }

    @Override
    protected void onDestroy() {
        // Close the round as a surrender if the user hasn't answered every question
        if (!isChangingConfigurations() && count < QUESTIONS_PER_ROUND) {
            for (int i = 0; i < QUESTIONS_PER_ROUND; i++) {
                answers[i] = false;
            }
            score = 0;
            roundTerminated();
        }
        super.onDestroy();
    }

    /**
     * Loading finished, call <tt>setup</tt> for beginning the duel.
     * @param success true iff the Snapshot load succeeded.
     */
    @Override
    protected void onLoadFinished(boolean success) {
        if (success) {
            if (configurationChanged) {
                initDuel();
            } else {
                setup();
            }
        } else {
            errorToast(errorRound);
        }
    }
}
