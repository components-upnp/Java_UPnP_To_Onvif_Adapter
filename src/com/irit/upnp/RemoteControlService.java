package com.irit.upnp;

import com.irit.xml.LecteurXml;
import com.irit.xml.LecteurXmlRemote;
import org.fourthline.cling.binding.annotations.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mkostiuk on 30/05/2017.
 */

@UpnpService(
        serviceId = @UpnpServiceId("RemoteControlService"),
        serviceType = @UpnpServiceType(value = "RemoteControlService")
)
public class RemoteControlService {

    private final PropertyChangeSupport propertyChangeSupport;

    public RemoteControlService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable
    private String commande = "";

    @UpnpAction(name = "SetCommande")
    public void setCommande(@UpnpInputArgument(name = "NewCommande") String c) throws IOException, SAXException, ParserConfigurationException {

        LecteurXml l = new LecteurXmlRemote(c);
        HashMap<String,String> args = l.getResultat();
        commande = args.get("COMMANDE");

        getPropertyChangeSupport().firePropertyChange("Commande", "", commande);
    }
}
