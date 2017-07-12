package rogueone.quizfight;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mdipirro on 08/07/17.
 */

public class StatsActivity extends SavedGamesActivity {

    private boolean configurationChanged;
    private boolean loadFinished;
    private int howManyDuelsWon;
    private int howManyCorrectAnswers;
    private int howManyRoundsWon;
    private int completedDuels;

    @BindView(R.id.duels_chart) BarChart duelsChart;
    @BindView(R.id.questions_chart) BarChart questionsChart;
    @BindView(R.id.round_chart) BarChart roundsChart;
    @BindView(R.id.indeterminateBarStat1) ProgressBar mProgressBar1;
    @BindView(R.id.indeterminateBarStat2) ProgressBar mProgressBar2;
    @BindView(R.id.indeterminateBarStat3) ProgressBar mProgressBar3;

    @BindString(R.string.stats_error) String statsError;
    @BindString(R.string.compl_duels) String completedDuelsString;
    @BindString(R.string.duels_won) String duelsWonString;
    @BindString(R.string.correct_answers) String correctAnswrsString;
    @BindString(R.string.rounds_won) String roundsWonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            configurationChanged = true;

            completedDuels = savedInstanceState.getInt(completedDuelsString);
            howManyDuelsWon = savedInstanceState.getInt(duelsWonString);
            howManyCorrectAnswers = savedInstanceState.getInt(correctAnswrsString);
            howManyRoundsWon = savedInstanceState.getInt(roundsWonString);

            loadFinished = true;
        }
    }

    @Override
    protected void onLoadFinished(boolean success) {
        if (success) {
            populateUI();
            Games.Snapshots.discardAndClose(((QuizFightApplication)getApplication()).getClient(), snapshot);
        } else {
            errorToast(statsError);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!configurationChanged) {
            getGames();
        } else {
            populateUI();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (loadFinished) {
            outState.putInt(completedDuelsString, completedDuels);
            outState.putInt(duelsWonString, howManyDuelsWon);
            outState.putInt(correctAnswrsString, howManyCorrectAnswers);
            outState.putInt(roundsWonString, howManyRoundsWon);
        }
    }

    private void populateUI() {
        List<BarEntry> duelsWon = new ArrayList<>();
        List<BarEntry> duelsPlayed = new ArrayList<>();
        List<BarEntry> questionsAnswered = new ArrayList<>();
        List<BarEntry> correctAnswers = new ArrayList<>();
        List<BarEntry> roundsWon = new ArrayList<>();
        List<BarEntry> roundsPlayed = new ArrayList<>();

        /*duelsPlayed.add(new BarEntry(0f, Float.parseFloat(events.get(duelsWon).getFormattedValue())));
        duelsWon.add(new BarEntry(1f, Float.parseFloat(events.get(duelsPlayed).getFormattedValue())));

        questionsAnswered.add(new BarEntry(0f, Float.parseFloat(events.get(roundsWon).getFormattedValue())));
        correctAnswers.add(new BarEntry(1f, Float.parseFloat(events.get(roundsPlayed).getFormattedValue())));

        roundsPlayed.add(new BarEntry(0f, Float.parseFloat(events.get(questionsAnswered).getFormattedValue())));
        roundsWon.add(new BarEntry(1f, Float.parseFloat(events.get(correctAnswers).getFormattedValue())));*/

        if (!configurationChanged) {
            completedDuels = history.getCompletedDuels().size();
            howManyDuelsWon = history.howManyWonDuels();
            howManyCorrectAnswers = history.howManyCorrectAnswers();
            howManyRoundsWon = history.howManyWonRounds();
            loadFinished = true;
        }

        if (completedDuels > 0) {
            duelsPlayed.add(new BarEntry(0f, completedDuels));
            duelsWon.add(new BarEntry(1f, howManyDuelsWon));

            questionsAnswered.add(new BarEntry(0f, completedDuels * 3 * 5));
            correctAnswers.add(new BarEntry(1f, howManyCorrectAnswers));

            roundsPlayed.add(new BarEntry(0f, completedDuels * 3));
            roundsWon.add(new BarEntry(1f, howManyRoundsWon));

            BarDataSet duelsPlayedSet = new BarDataSet(duelsPlayed, "Duels Played");
            BarDataSet duelsWonSet = new BarDataSet(duelsWon, "Duels Won");
            BarDataSet questionsAnsweredSet = new BarDataSet(questionsAnswered, "Questions Answered");
            BarDataSet correctAnswersSet = new BarDataSet(correctAnswers, "Correct Answers");
            BarDataSet roundsWonSet = new BarDataSet(roundsWon, "Rounds Won");
            BarDataSet roundsPlayedSet = new BarDataSet(roundsPlayed, "Rounds Played");

            duelsPlayedSet.setColors(new int[] { R.color.won_duel}, this);
            duelsWonSet.setColors(new int[] { R.color.lost_duel}, this);
            questionsAnsweredSet.setColors(new int[] { R.color.won_duel}, this);
            correctAnswersSet.setColors(new int[] { R.color.lost_duel}, this);
            roundsPlayedSet.setColors(new int[] { R.color.won_duel}, this);
            roundsWonSet.setColors(new int[] { R.color.lost_duel}, this);

            BarData duelsData = new BarData(duelsPlayedSet, duelsWonSet);
            BarData questionsData = new BarData(questionsAnsweredSet, correctAnswersSet);
            BarData roundsData = new BarData(roundsPlayedSet, roundsWonSet);

            duelsChart.setData(duelsData);
            duelsChart.setFitBars(true);
            duelsChart.getXAxis().setDrawLabels(false);
            duelsChart.getDescription().setEnabled(false);
            mProgressBar1.setVisibility(View.GONE);
            duelsChart.setVisibility(View.VISIBLE);
            questionsChart.setData(questionsData);
            questionsChart.setFitBars(true);
            questionsChart.getXAxis().setDrawLabels(false);
            questionsChart.getDescription().setEnabled(false);
            mProgressBar2.setVisibility(View.GONE);
            questionsChart.setVisibility(View.VISIBLE);
            roundsChart.setData(roundsData);
            roundsChart.setFitBars(true);
            roundsChart.getXAxis().setDrawLabels(false);
            roundsChart.getDescription().setEnabled(false);
            mProgressBar3.setVisibility(View.GONE);
            roundsChart.setVisibility(View.VISIBLE);

            Legend duelsLegend = duelsChart.getLegend();
            duelsLegend.setEnabled(true);

            duelsChart.invalidate();
            questionsChart.invalidate();
            roundsChart.invalidate();
        }
    }
}
