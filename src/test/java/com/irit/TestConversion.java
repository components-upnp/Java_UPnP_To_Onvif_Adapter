package test.java.com.irit;

import com.irit.upnp.AdapterServer;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mkostiuk on 31/05/2017.
 */
public class TestConversion extends TestCase {

    private Subscription subTonOnvifService;
    private Subscription subRemoteControlService;
    private Thread app;
    private String commande = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TelecommandeEleve xmlns=\"/\"><UDN>lol</UDN><Commande>DROITE</Commande></TelecommandeEleve>";

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

    @Test
    public void testConversionDroite() {
        pause(2000);
        subRemoteControlService.executeAction(
                "SetCommande",
                "NewCommande",
                commande
        );
        pause(2000);
        assertEquals("1", subTonOnvifService.getXs().get(2));
        assertEquals("0", subTonOnvifService.getYs().get(2));
    }
}
