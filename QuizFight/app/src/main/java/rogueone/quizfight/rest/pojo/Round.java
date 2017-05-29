package rogueone.quizfight.rest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mdipirro on 27/05/17.
 */

public class Round implements Parcelable {
    private String duelID;
    private String quizID;
    private String topic;
    private List<Question> questions;

    protected Round(Parcel in) {
        duelID = in.readString();
        quizID = in.readString();
        topic = in.readString();
        questions = in.createTypedArrayList(Question.CREATOR);
    }

    public String getDuelID() {
        return duelID;
    }

    public String getQuizID() {
        return quizID;
    }

    public String getTopic() {
        return topic;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static final Creator<Round> CREATOR = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode(); // identify the kind of object
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duelID);
        dest.writeString(quizID);
        dest.writeString(topic);
        dest.writeTypedList(questions);
    }
}
