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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;


/**
 * Created by mkostiuk on 30/05/2017.
 */
public class AdapterServer implements Runnable {

    private LocalService<RemoteControlService> remoteControlService;
    private LocalService<ToOnvifService> toOnvifService;
    private LocalService<DeviceOrientationControlService> orientationService;

    /**
     * Fonction de lancement du thread serveur UPnP.
     * On initialise ici les listeners sur les évènements UPnP entrants, en spécifiant le
     * comportement à adopter en retour.
     * */
    @Override
    public void run() {

        final UpnpService upnpService = new UpnpServiceImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> upnpService.shutdown()));

        try {
            upnpService.getRegistry().addDevice(
                    createDevice()
            );
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        /**
         *  Listener utilisé lors de la réception d'une direction à prendre.
         *  On initie ensuite un mouvement de la caméra dans la direction demandée.
         * */
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
                            else { //IMMOBILE
                                movement(0, 0);
                            }
                        }
                );

        /**
         * Listener utilisé lors de la réception de l'orientation d'un device Android
         * Les valeurs sont traitées et selon l'orientation effective du device on initie
         * une rotation de la caméra.
         * */
        orientationService.getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(evt -> {
                    HashMap<String,String> args = (HashMap<String, String>) evt.getNewValue();
                    float xf = Float.parseFloat(args.get("X"));
                    float yf = Float.parseFloat(args.get("Y"));
                    float zf = Float.parseFloat(args.get("Z"));

                    System.out.println("X : " + xf + " Y : " + yf + " Z : " + zf);

                    int x = Math.round(xf);
                    int y = Math.round(yf);
                    int z = Math.round(zf);


                    if (x > 1) {
                        if (y > 1)      //BAS GAUCHE
                            movement(-1, -1);
                        else if (y < -1)    //HAUT GAUCHE
                            movement(-1, 1);
                        else                //GAUCHE
                            movement(-1,0);
                    }
                    else if (x < -1) {
                        if (y > 1)      //BAS DROITE
                            movement(1, -1);
                        else if (y < -1)        //HAUT DROITE
                            movement(1, 1);
                        else                    //DROITE
                            movement(1,0);
                    }
                    else {
                        if (y < -1)     //HAUT
                            movement(0,1);
                        else if (y > 1) //BAS
                            movement(0,-1);
                        else            //IMMOBILE
                            movement(0,0);
                    }

                });
    }

    /**
     * Cette fonction permet d'ordonner à la caméra d'opérer un mouvement sur les axes X et Y.
     * Les valeurs possibles pour X et Y sont {-1, 0, 1}, toute autre valeurs supérieures ou inférieures
     * seront considérées comme -1 ou 1 selon leur signe.
     * Par exemple pour un couple (X,Y), (1,1) siginifie mouvement en haut à droite.
     * */
    public void movement(Integer x, Integer y) {
        System.out.println("Movement: " + x + " " + y);
        toOnvifService.getManager().getImplementation().setX(x.toString());
        toOnvifService.getManager().getImplementation().setY(y.toString());
        //pause(200);
        toOnvifService.getManager().getImplementation().actionMove();
    }

    /**
     * Fonction de création du composant UPnP.
     * */
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

        orientationService =
                new AnnotationLocalServiceBinder().read(DeviceOrientationControlService.class);
        orientationService.setManager(
                new DefaultServiceManager<>(orientationService, DeviceOrientationControlService.class)
        );



        return new LocalDevice(
                identity, type, details,
                new LocalService[] {remoteControlService, toOnvifService, orientationService}
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
