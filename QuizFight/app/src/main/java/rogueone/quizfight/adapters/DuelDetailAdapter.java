package rogueone.quizfight.adapters;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rogueone.quizfight.R;
import rogueone.quizfight.models.Duel;
import rogueone.quizfight.models.Question;
import rogueone.quizfight.models.Quiz;

/**
 * Created by Becks on 27/05/17.
 */

public class DuelDetailAdapter extends BaseExpandableListAdapter {

    private List<Quiz> rounds;
    public LayoutInflater inflater;


    public DuelDetailAdapter(LayoutInflater inflater, Duel duel) {
        this.rounds = duel.getQuizzes();
        this.inflater = inflater;
    }


    @Override
    public int getGroupCount() {
        return rounds.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return rounds.get(groupPosition).getQuestions().size();
    }

    @Override
    public Quiz getGroup(int groupPosition) {
        return rounds.get(groupPosition);
    }

    @Override
    public Question getChild(int groupPosition, int childPosition) {
        return rounds.get(groupPosition).getQuestions().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.duel_detail_group, null);
        }

        Quiz quiz = getGroup(groupPosition);

        TextView round = (TextView) convertView.findViewById(R.id.textview_dueldetailgroup_roundtext);
        round.setText("Round " + (groupPosition + 1));

        TextView score = (TextView) convertView.findViewById(R.id.textview_dueldetailgroup_roundscore);
        // FIXME: get proper data, model doesn't provide this information yet
        score.setText("4 - 3");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.duel_detail_child, null);
        }

        Question question = getChild(groupPosition, childPosition);

        TextView questionText = (TextView) convertView.findViewById(R.id.textview_dueldetailchild_question);
        questionText.setText("Question " + (childPosition + 1));

        // FIXME: set icons according to given answers, model doesn't provide necessary information yet
        ImageView playerIcon = (ImageView) convertView.findViewById(R.id.imageview_dueldetialchild_playericon);
        playerIcon.setImageResource(R.drawable.all_victory);
        playerIcon.setColorFilter(ContextCompat.getColor(convertView.getContext(), R.color.won_duel));

        ImageView opponentIcon = (ImageView) convertView.findViewById(R.id.imageview_dueldetialchild_opponenticon);
        opponentIcon.setImageResource(R.drawable.all_defeat);
        opponentIcon.setColorFilter(ContextCompat.getColor(convertView.getContext(), R.color.lost_duel));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
