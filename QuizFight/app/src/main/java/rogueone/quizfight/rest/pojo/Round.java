package rogueone.quizfight.rest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * This class represents a duel Round. It contains the duelID, the quizID (of the current round),
 * its topic, the opponent username (Games) and a list of questions.
 * The class implements Parcelable to be passed in a Bundle.
 *
 * @author Matteo Di Pirro
 * @see Parcelable
 */

public class Round implements Parcelable {
    private String duelID;
    private String quizID;
    private String topic;
    private String opponent;
    private List<Question> questions;

    protected Round(Parcel in) {
        duelID = in.readString();
        quizID = in.readString();
        topic = in.readString();
        opponent = in.readString();
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

    public String getOpponent() {
        return opponent;
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
        dest.writeString(opponent);
        dest.writeTypedList(questions);
    }
}
