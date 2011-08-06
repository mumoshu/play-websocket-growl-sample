package notifier;

import play.libs.F;

public class NotificationStream {
    final F.ArchivedEventStream<Notification> notificationStream = new F.ArchivedEventStream<Notification>(20);
    final static NotificationStream instance = new NotificationStream();

    public static F.EventStream<Notification> stream() {
        return instance.notificationStream.eventStream();
    }

    public static void publish(String message) {
        instance.notificationStream.publish(new Notification(message));
    }
}
