package rogueone.quizfight.rest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mdipirro on 27/05/17.
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
