package test.java.com.irit;

import com.irit.upnp.AdapterServer;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

/**
 * Created by mkostiuk on 31/05/2017.
 */
public class TestConversion extends TestCase {

    Subscription subTonOnvifService;
    Subscription subRemoteControlService;
    Thread app;

    @Before
    public void setUp() {
        app = new Thread(new AdapterServer());
        app.run();
        pause(2000);
        subTonOnvifService = new Subscription("ToOnvifService");
        subTonOnvifService.run();
        subRemoteControlService = new Subscription("RemoteControlService");
        subRemoteControlService.run();
        pause(3000);
    }

    @After
    public void done() {
        app.interrupt();
    }

    //Permet de mettre l'exécution en pause, afin d'avoir le temps de recevoir les évènements
    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
