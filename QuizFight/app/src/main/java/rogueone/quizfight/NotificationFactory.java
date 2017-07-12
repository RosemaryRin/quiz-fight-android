package rogueone.quizfight;

/**
 * This Factory class is used to open the correct activity when a notification is received.
 *
 * @author Matteo Di Pirro
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
