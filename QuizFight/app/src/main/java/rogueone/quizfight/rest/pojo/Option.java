package rogueone.quizfight.rest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents an Option (i.e. a possible answer to a question). Every <tt>Option</tt> has
 * an ID (from 1 to 4) and a string representing the actual answer. The class implements Parcelable
 * to be passed in a Bundle.
 *
 * @author Matteo Di Pirro
 * @see Parcelable
 */

public class Option implements Parcelable {

    private int option_id;
    private String option;

    protected Option(Parcel in) {
        option_id = in.readInt();
        option = in.readString();
    }

    public int getOptionID() {
        return option_id;
    }

    public String getOption() {
        return option;
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(option_id);
        dest.writeString(option);
    }
}
