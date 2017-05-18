package rogueone.quizfight;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartDuel extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_duel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start_duel, container, false);

            // FIXME: get data from database/ggames
            String[] friends = {"mdipirro", "emanuelec"};
            String[][] topRanked = {{"1", "pinco pallo", "1234"},
                                    {"2", "asdrubale", "1034"},
                                    {"3", "tullio", "1033"}};

            LinearLayout list = (LinearLayout) rootView.findViewById(R.id.List);
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) { // friends tab
                for (int i = 0; i < friends.length; i++)
                    list.addView(getFriendRow(list.getContext(), friends[i]));
            }
            else { // top-ranked tab
                for (int i = 0; i < topRanked.length; i++)
                    list.addView(getRankedRow(list.getContext(), topRanked[i]));
            }

            return rootView;
        }


        private LinearLayout getFriendRow(Context c, String name) {
            LinearLayout ll = new LinearLayout(c);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            ImageView img = new ImageView(ll.getContext());
            img.setImageResource(R.drawable.duel);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics());
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height));
            ll.addView(img);

            TextView nameView = new TextView(ll.getContext());
            nameView.setText(name);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            nameView.setGravity(Gravity.CENTER);
            ll.addView(nameView);

            return ll;
        }

        private LinearLayout getRankedRow(Context c, String[] player) {
            LinearLayout ll = new LinearLayout(c);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView positionView = new TextView(ll.getContext());
            positionView.setText(player[0]);
            int dim = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics());
            positionView.setLayoutParams(new LinearLayout.LayoutParams(dim, dim));
            positionView.setGravity(Gravity.CENTER);
            ll.addView(positionView);

            TextView nameView = new TextView(ll.getContext());
            nameView.setText(player[1]);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            nameView.setGravity(Gravity.CENTER);
            ll.addView(nameView);

            TextView scoreView = new TextView(ll.getContext());
            scoreView.setText(player[2]);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics());
            scoreView.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
            scoreView.setGravity(Gravity.CENTER);
            ll.addView(scoreView);

            return ll;
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
            return PlaceholderFragment.newInstance(position + 1);
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
                    return "FRIENDS";
                case 1:
                    return "TOP RANKED";
            }
            return null;
        }
    }
}
