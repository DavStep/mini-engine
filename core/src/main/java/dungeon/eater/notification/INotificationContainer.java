package dungeon.eater.notification;

public interface INotificationContainer {

    void addNotificationWidget (NotificationWidget widget);

    default boolean isShowNumber () {
        return false;
    }

    default boolean isShowNotification () {
        return true;
    }
}
