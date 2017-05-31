package com.irit.upnp;

import org.fourthline.cling.binding.annotations.*;

import java.beans.PropertyChangeSupport;

/**
 * Created by mkostiuk on 30/05/2017.
 */

@UpnpService(
        serviceType = @UpnpServiceType(value = "ToOnvifService"),
        serviceId = @UpnpServiceId("ToOnvifService")
)
public class ToOnvifService {

    private final PropertyChangeSupport propertyChangeSupport;

    public ToOnvifService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable
    private boolean move = false;

    @UpnpStateVariable
    private String x = "0";

    @UpnpStateVariable
    private String y = "0";

    @UpnpAction(name = "ActionMove")
    public void actionMove() {
        boolean oldMoveValue = move;
        move = !move;

        getPropertyChangeSupport().firePropertyChange("Move", oldMoveValue, move);
    }

    @UpnpAction(name = "SetX")
    public void setX(@UpnpInputArgument(name = "NewX") String newX) {
        String oldXValue  = x;
        x = newX;

        getPropertyChangeSupport().firePropertyChange("X", oldXValue, x);
    }

    @UpnpAction(name = "SetY")
    public void setY(@UpnpInputArgument(name = "NewY") String newY) {
        String oldYValue  = y;
        y = newY;

        getPropertyChangeSupport().firePropertyChange("Y", oldYValue, y);
    }

}
