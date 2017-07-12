package rogueone.quizfight;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.games.Games;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.rest.api.NewDuel;
import rogueone.quizfight.rest.api.getGoogleUsername;
import rogueone.quizfight.rest.pojo.RESTDuel;
import rogueone.quizfight.rest.pojo.Round;
import rogueone.quizfight.rest.pojo.User;
import rogueone.quizfight.utils.SavedGames;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;

import rogueone.quizfight.adapters.FriendListAdapter;

public class StartDuelActivity extends SavedGamesActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private QuizFightApplication application;
    private final static String TAG = "StartDuelActivity";

    private ListView listView;

    @BindView(R.id.indeterminateBar3) ProgressBar mProgressBar;

    @BindString(R.string.unable_to_start_duel) String duelError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_duel);
        ButterKnife.bind(this);

        application = (QuizFightApplication)getApplication();
        getGames();
    }

    private void setupUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_startduel_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager_startduel_content);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_startduel_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        findViewById(R.id.button_random_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDuel("elena.pullin95@gmail.com"); //FIXME to be removed
            }
        });

        listView = (ListView) findViewById(R.id.listview_startduel_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("LIST SIZE", String.valueOf(listView.getCount()));
                FriendListAdapter friendListAdapter = (FriendListAdapter) listView.getAdapter();
                String facebookId = friendListAdapter.getFacebookId(position);
                Log.d("FACEBOOK ID", facebookId);
                new getGoogleUsername(facebookId).call(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            String username = response.body().getUsername();
                            mProgressBar.setVisibility(View.VISIBLE);
                            createDuel(username);
                        }
                        else {
                            errorToast(duelError);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        errorToast(duelError);
                    }
                });
            }
        });
    }

    public void createDuel(String opponentUsername) {
        String[] topics = getRandomTopics().toArray(new String[3]); // 3 rounds
        new NewDuel(new RESTDuel(
                Games.Players.getCurrentPlayer(application.getClient()).getDisplayName(),
                opponentUsername,
                topics
        )).call(new Callback<Round>() {
            @Override
            public void onResponse(Call<Round> call, Response<Round> response) {
                mProgressBar.setVisibility(View.GONE);
                startDuel(response.body());
            }

            @Override
            public void onFailure(Call<Round> call, Throwable t) {
                errorToast(duelError);
            }
        });
    }

    private List<String> getRandomTopics() {
        // shuffle for getting three random topics to be used during the duel
        // those elements will be the first three in the
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.topics));
        Collections.shuffle(list, new Random());
        return list.subList(0, 3);
    }

    private void startDuel(@NonNull Round round) {
        Duel newDuel = new Duel(round.getDuelID(), round.getOpponent());
        history.addDuel(newDuel);
        SavedGames.writeSnapshot(snapshot, history, "", application.getClient());

        // Begin with the first round
        Intent intent = new Intent(StartDuelActivity.this, DuelActivity.class);
        intent.putExtra(getString(R.string.round), round);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onLoadFinished(boolean success) {
        if (success) {
            setupUI();
        } else {
            errorToast(duelError);
        }
    }

    /**
     * A fragment containing a list of possible opponents.
     */
    public static class StartDuelFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ListView listView;

        public StartDuelFragment() {}

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StartDuelFragment newInstance(int sectionNumber) {
            StartDuelFragment fragment = new StartDuelFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d(TAG, "StartDuelFragment - onCreate");
            final View rootView = inflater.inflate(R.layout.fragment_start_duel, container, false);

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1
                    && AccessToken.getCurrentAccessToken() != null) { // friends tab
                friendsNamesRequest(rootView);
            } else if (AccessToken.getCurrentAccessToken() == null) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                Toast.makeText(getContext(),
                        getResources().getString(R.string.no_facebook_access),
                        Toast.LENGTH_LONG).show();
            }

            /*if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) { // friends tab
                Games.Players.loadConnectedPlayers(application.getClient(), true).setResultCallback(
                        new ResultCallback<Players.LoadPlayersResult>() {
                            @Override
                            public void onResult(@NonNull Players.LoadPlayersResult loadPlayersResult) {
                                PlayerBuffer friends = loadPlayersResult.getPlayers();
                                if (friends.getCount() > 0) {
                                    final ListView listView = (ListView) rootView.findViewById(R.id.listview_startduel_list);
                                    rootView.findViewById(R.id.textview_startduel_nouserstoshow).setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    final FriendListAdapter listAdapter = new FriendListAdapter(getContext(), friends);
                                    listView.setAdapter(listAdapter);
                                }
                            }
                        }
                );
            }
            else { // leaderboard tab
                Games.Leaderboards.loadPlayerCenteredScores(application.getClient(), getString(R.string.leaderboard_id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC, 10, true).setResultCallback(
                        new ResultCallback<Leaderboards.LoadScoresResult>() {
                            @Override
                            public void onResult(@NonNull Leaderboards.LoadScoresResult loadScoresResult) {
                                LeaderboardScoreBuffer leaderboard = loadScoresResult.getScores();
                                Log.d("Debug", ""+leaderboard.getCount());
                                if (leaderboard.getCount() > 0) {
                                    final ListView listView = (ListView) rootView.findViewById(R.id.listview_startduel_list);
                                    rootView.findViewById(R.id.textview_startduel_nouserstoshow).setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    final LeaderboardAdapter listAdapter = new LeaderboardAdapter(getContext(), leaderboard);
                                    listView.setAdapter(listAdapter);
                                }
                            }
                        }
                );
            }*/

            return rootView;
        }

        private void friendsNamesRequest(final View rootView) {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            if (response != null) {
                                Log.d(TAG, "Friend list request completed");
                                try {
                                    JSONArray friends = response.getJSONObject()
                                            .getJSONArray("data");
                                    if (friends.length() == 0) {
                                        Toast.makeText(getContext(), getResources().
                                                getString(R.string.no_facebook_friends),
                                                Toast.LENGTH_LONG).show();
                                    }
                                    setFriendsAdapter(friends, rootView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            ).executeAsync();
        }

        private void setFriendsAdapter(JSONArray friends, View rootView) {
            listView = (ListView) rootView
                    .findViewById(R.id.listview_startduel_list);

            rootView.findViewById(R.id.textview_startduel_nouserstoshow)
                    .setVisibility(View.GONE);

            listView.setVisibility(View.VISIBLE);

            final FriendListAdapter listAdapter =
                    new FriendListAdapter(getContext(), friends);

            listView.setAdapter(listAdapter);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return StartDuelFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_friends);
                case 1:
                    return getString(R.string.title_leaderboard);
            }
            return null;
        }
    }
}