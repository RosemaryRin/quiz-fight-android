package rogueone.quizfight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.games.event.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.R;

/**
 * Created by mdipirro on 08/07/17.
 */

public class StatsActivity extends AppCompatActivity implements ResultCallback<Events.LoadEventsResult> {

    Map<String, Event> events;

    @BindView(R.id.duels_chart) BarChart duelsChart;
    @BindView(R.id.questions_chart) BarChart questionsChart;
    @BindView(R.id.round_chart) BarChart roundsChart;

    @BindString(R.string.duels_won) String duelsWon;
    @BindString(R.string.duels_played) String duelsPlayed;
    @BindString(R.string.corrects_answers) String correctAnswers;
    @BindString(R.string.questions_answered) String questionsAnswered;
    @BindString(R.string.rounds_played) String roundsPlayed;
    @BindString(R.string.rounds_won) String roundsWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        events = new HashMap<>();
    }

    @Override
    public void onResume() {
        super.onResume();

        GoogleApiClient client = ((QuizFightApplication)getApplication()).getClient();
        PendingResult<Events.LoadEventsResult> pr = Games.Events.load(client, true);
        pr.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull Events.LoadEventsResult result) {
        EventBuffer eb = result.getEvents();

        Log.d("EVENTS", eb.getCount() +"");

        for (int i = 0; i < eb.getCount(); i++) {
            Event event = eb.get(i);
            events.put(event.getEventId(), event);
            Log.d("EVENTS", event.getEventId());
        }
        eb.close();
        populateUI();
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

        duelsPlayed.add(new BarEntry(0f, 18f));
        duelsWon.add(new BarEntry(1f, 10f));

        questionsAnswered.add(new BarEntry(0f, 90f));
        correctAnswers.add(new BarEntry(1f, 40f));

        roundsPlayed.add(new BarEntry(0f, 54f));
        roundsWon.add(new BarEntry(1f, 23f));

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
        questionsChart.setData(questionsData);
        questionsChart.setFitBars(true);
        questionsChart.getXAxis().setDrawLabels(false);
        questionsChart.getDescription().setEnabled(false);
        roundsChart.setData(roundsData);
        roundsChart.setFitBars(true);
        roundsChart.getXAxis().setDrawLabels(false);
        roundsChart.getDescription().setEnabled(false);

        Legend duelsLegend = duelsChart.getLegend();
        duelsLegend.setEnabled(true);

        duelsChart.invalidate();
        questionsChart.invalidate();
        roundsChart.invalidate();
    }
}
