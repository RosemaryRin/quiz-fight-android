package rogueone.quizfight.models;

/**
 * Created by mdipirro on 30/05/17.
 */

public class Maybe<T> {
    private T element;

    public Maybe(T element) {
        this.element = element;
    }

    public T get(T defaultValue) {
        return (element != null) ? element : defaultValue;
    }
}
