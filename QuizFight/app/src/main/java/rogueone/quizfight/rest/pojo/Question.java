package rogueone.quizfight.rest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * This class represents a Question. It contains the actual question text, a boolean (true iff the
 * question if a T/F one), an int representing the correct answer index, the difficulty and a <tt>List</tt>
 * of <tt>Option</tt>s. The class implements Parcelable to be passed in a Bundle.
 *
 * @author Matteo Di Pirro
 * @see Parcelable
 */

public class Question implements Parcelable {

    private String question;
    private boolean trueOrFalse;
    private int answer;
    private int difficulty;
    private List<Option> options;

    protected Question(Parcel in) {
        question = in.readString();
        trueOrFalse = in.readByte() != 0;
        answer = in.readInt();
        difficulty = in.readInt();
        options = in.createTypedArrayList(Option.CREATOR);
    }

    public String getQuestion() {
        return question;
    }

    public boolean isTrueOrFalse() {
        return trueOrFalse;
    }

    public int getAnswer() {
        return answer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public List<Option> getOptions() {
        return options;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeByte((byte) (trueOrFalse ? 1 : 0));
        dest.writeInt(answer);
        dest.writeInt(difficulty);
        dest.writeTypedList(options);
    }
}
