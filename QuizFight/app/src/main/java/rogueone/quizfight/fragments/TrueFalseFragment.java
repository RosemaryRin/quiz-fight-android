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

public class TrueFalseFragment extends Fragment implements QuestionFragment {
    @BindView(R.id.button_tf_one) Button button_tf_one;
    @BindView(R.id.button_tf_two) Button button_tf_two;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_true_false, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void fillOptions(@NonNull List<Option> options) {
        button_tf_one.setText(options.get(0).getOption());
        button_tf_two.setText(options.get(1).getOption());
    }
}
