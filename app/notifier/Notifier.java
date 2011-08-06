package notifier;

import play.Logger;

public class Notifier {
    public static Growl growl = null;

    public static Growl growl() {
        if (growl == null) {
            growl = new Growl("Play feat. Growl",
                    new String[] {"system", "boss", "spam"},
                    new String[] {"system", "boss"});
            growl.init();
            growl.registerApplication();
            growl.notify("system", "Play feat. Growl", "Growl initialized.");
        }
        return growl;
    }

    public static void notify(String message) {
        Logger.info("Notify: %s", message);
        growl().notify("system", "Play feat. Growl", message);
        NotificationStream.publish(message);
    }
}