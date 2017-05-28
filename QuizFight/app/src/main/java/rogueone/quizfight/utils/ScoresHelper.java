package rogueone.quizfight.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.games.Games;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.R;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.History;
import rogueone.quizfight.rest.api.AddToken;
import rogueone.quizfight.rest.api.GetPendingScores;
import rogueone.quizfight.rest.pojo.Scores;
import rogueone.quizfight.rest.pojo.User;

/**
 * Created by mdipirro on 28/05/17.
 */

public class ScoresHelper {
    public static void addPendingDuelsIfExist(@NonNull Context context, History history) {
        if (history == null) {
            history = new History();
        }
        String duelIDString = context.getResources().getString(R.string.duel_id);
        String opponentString = context.getResources().getString(R.string.opponent);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPref.contains(duelIDString) && sharedPref.contains(opponentString)) {
            // Get sets containing duel dares received by notifications
            Set<String> opponentsS = sharedPref.getStringSet(opponentString, new LinkedHashSet<String>());
            Set<String> pendingDuelsS = sharedPref.getStringSet(duelIDString, new LinkedHashSet<String>());

            // Convert them to arrays for a easier management
            String[] opponents = opponentsS.toArray(new String[opponentsS.size()]);
            String[] pendingDuels = pendingDuelsS.toArray(new String[pendingDuelsS.size()]);

            // Populate the History object with new duels
            for (int i = 0; i < opponents.length; i++) {
                history.addDuel(new Duel(pendingDuels[i], opponents[i]));
            }

            // Commit changes
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(duelIDString);
            editor.remove(opponentString);
            editor.apply();
        }
    }


}
