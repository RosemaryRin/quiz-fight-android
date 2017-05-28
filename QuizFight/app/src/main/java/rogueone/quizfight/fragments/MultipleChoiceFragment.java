package rogueone.quizfight.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rogueone.quizfight.R;
import rogueone.quizfight.rest.pojo.Option;

/**
 * Created by mdipirro on 27/05/17.
 */

public class MultipleChoiceFragment extends Fragment implements QuestionFragment {
    @BindView(R.id.button_choice_one) Button button_mc_one;
    @BindView(R.id.button_choice_two) Button button_mc_two;
    @BindView(R.id.button_choice_three) Button button_mc_three;
    @BindView(R.id.button_choice_four) Button button_mc_four;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void fillOptions(@NonNull List<Option> options) {
        button_mc_one.setText(options.get(0).getOption());
        button_mc_two.setText(options.get(1).getOption());
        button_mc_three.setText(options.get(2).getOption());
        button_mc_four.setText(options.get(3).getOption());
    }
}
