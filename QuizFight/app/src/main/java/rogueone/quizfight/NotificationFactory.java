package rogueone.quizfight;

/**
 * Created by mdipirro on 23/05/17.
 */

public class NotificationFactory {
    public static Class getTargetActivity(int id) {
        switch (id) {
            case 1:
                return HomeActivity.class;
            case 2:
                return DuelActivity.class;
        }
        return SignInActivity.class;
    }
}
