package rogueone.quizfight;

/**
 * Created by mdipirro on 23/05/17.
 */

public class NotificationFactory {
    public static Class getTargetActivity(int id) {
        switch (id) {
            case 2:
            case 3:
                return DuelActivity.class;
            default:
                return HomeActivity.class;
        }
    }
}
