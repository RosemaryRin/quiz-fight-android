package rogueone.quizfight;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
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

import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.Players;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import butterknife.BindString;
import butterknife.ButterKnife;
import rogueone.quizfight.adapters.FriendListAdapter;
import rogueone.quizfight.adapters.LeaderboardAdapter;

public class StartDuelActivity extends AppCompatActivity {

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

    private static QuizFightApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_duel);

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

        application = (QuizFightApplication)getApplicationContext();

        findViewById(R.id.button_random_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartDuelActivity.this, DuelActivity.class);
                startActivity(intent);
            }
        });
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
            final View rootView = inflater.inflate(R.layout.fragment_start_duel, container, false);

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) { // friends tab

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

            }

            return rootView;
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
