package com.irit.upnp;

import com.irit.xml.LecteurXml;
import com.irit.xml.LecteurXmlOrientation;
import org.fourthline.cling.binding.annotations.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mkostiuk on 14/06/2017.
 */

@UpnpService(
        serviceId = @UpnpServiceId("DeviceOrientationService"),
        serviceType = @UpnpServiceType(value = "DeviceOrientationService", version = 1)
)
public class DeviceOrientationControlService {

    private final PropertyChangeSupport propertyChangeSupport;

    public DeviceOrientationControlService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(name = "OrientationCommand")
    private String orientationCommand = "";


    @UpnpAction(name = "SetOrientationCommand")
    public void setOrientationCommand(@UpnpInputArgument(name = "NewOrientationCommand") String c) throws IOException, SAXException, ParserConfigurationException {
        orientationCommand = c;

        LecteurXml l = new LecteurXmlOrientation(orientationCommand);
        HashMap<String,String> args = l.getResultat();

        getPropertyChangeSupport().firePropertyChange("orientationCommand", "", args);
    }
}
