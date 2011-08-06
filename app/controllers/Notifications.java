package controllers;

import notifier.Notification;
import notifier.NotificationStream;
import notifier.Notifier;
import play.*;
import play.mvc.*;
import play.libs.*;

import static play.libs.F.Matcher.*;
import static play.mvc.Http.WebSocketEvent.*;

public class Notifications extends Controller {
    public static void index() {
        render();
    }

    public static class WebSocket extends WebSocketController {

        public static void connect() {
            Logger.info("Connected.");

            F.EventStream<Notification> eventStream = NotificationStream.stream();

            while(inbound.isOpen()) {
                F.Either<Http.WebSocketEvent, Notification> receivedEvent = await(F.Promise.waitEither(
                        inbound.nextEvent(),
                        // 以下のようにするとNotificationが無限に返ってきてアプリが応答しなくなります。
                        // F.EventStream<Notification> eventStream = NotificationStream.stream();
                        eventStream.nextEvent()
                ));

                // WebSocketから受け取ったメッセージをGrowlや他のWebSocketコネクションにブロードキャストする。
                for (String message : TextFrame.match(receivedEvent._1)) {
                    Notifier.notify(message);
                    Logger.info("Message received: %s", message);
                }

                // 他のWebSocket接続から受け取ったメッセージを、現在のWebSocket接続先に送る。
                for (Notification notification : ClassOf(Notification.class).match(receivedEvent._2)) {
                    Logger.info("Matched to Notification class.");
                    outbound.send(notification.message);
                }

                for (Http.WebSocketClose close : SocketClosed.match(receivedEvent._1)) {
                    disconnect();
                }
            }
        }
    }
}
