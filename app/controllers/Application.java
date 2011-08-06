package controllers;
 
import java.util.*;

import notifier.Growl;
import notifier.Notifier;
import play.mvc.*;
 
import models.*;
 
public class Application extends Controller {
 
    public static void growlHTTP() {
        render();
    }

    public static void growlWS() {
        render();
    }

    public static void notifyWithGrowl(String message) {
        Notifier.notify(message);
    }
}
