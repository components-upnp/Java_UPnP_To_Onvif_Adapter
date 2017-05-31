package com.irit.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;


/**
 * Created by mkostiuk on 30/05/2017.
 */
public class AdapterServer implements Runnable {

    private LocalService<RemoteControlService> remoteControlService;
    private LocalService<ToOnvifService> toOnvifService;

    @Override
    public void run() {

        final UpnpService upnpService = new UpnpServiceImpl();

        try {
            upnpService.getRegistry().addDevice(
                    createDevice()
            );
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        remoteControlService.getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(
                        evt -> {

                            String commande = evt.getNewValue().toString();

                            if (commande.equals("DROITE")) {
                                movement(1,0);
                            } if (commande.equals("GAUCHE")) {
                                movement(-1,0);
                            } if (commande.equals("BAS")) {
                                movement(0,-1);
                            }if (commande.equals("HAUT")) {
                                movement(0,1);
                            }
                            else {
                                movement(0, 0);
                            }
                        }
                );
    }

    public void movement(Integer x, Integer y) {
        System.out.println("Movement: " + x + " " + y);
        toOnvifService.getManager().getImplementation().setX(x.toString());
        toOnvifService.getManager().getImplementation().setY(y.toString());
        //pause(200);
        toOnvifService.getManager().getImplementation().actionMove();
    }

    private LocalDevice createDevice() throws ValidationException {

        DeviceIdentity identity =
                new DeviceIdentity(
                        UDN.uniqueSystemIdentifier("Java UPnP to ONVIF Adapter")
                );

        DeviceType type =
                new UDADeviceType("CameraPTZ", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Java UPnP to ONVIF Adapter",					// Friendly Name
                        new ManufacturerDetails(
                                "UPS-IRIT",								// Manufacturer
                                ""),								// Manufacturer URL
                        new ModelDetails(
                                "UpnpToOnvifAdapter",						// Model Name
                                "Composant permettant de faire le lien entre un composant UPnP et ONVIF",	// Model Description
                                "v1" 								// Model Number
                        )
                );

        remoteControlService =
                new AnnotationLocalServiceBinder().read(RemoteControlService.class);
        remoteControlService.setManager(
                new DefaultServiceManager<>(remoteControlService, RemoteControlService.class)
        );

        toOnvifService =
                new AnnotationLocalServiceBinder().read(ToOnvifService.class);
        toOnvifService.setManager(
                new DefaultServiceManager(toOnvifService, ToOnvifService.class)
        );



        return new LocalDevice(
                identity, type, details,
                new LocalService[] {remoteControlService, toOnvifService}
        );
    }
    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

}
