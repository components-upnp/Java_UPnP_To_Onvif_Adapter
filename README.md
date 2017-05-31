# Java_UPnP_To_Onvif_Adapter
Composant qui permet de convertir les commandes des composants UPnP pour les composants ONVIF


<strong>Description :</strong>

Application Java qui permet aux composants UPnP que nous avons développés de communiquer avec l'assemblage de composants 
ONVIF permettant de contrôler une caméra PTZ. Entre autres, on peut faire communiquer Android Remote Control avec l'assemblage. 
Ce composant prend en entrée une commande en XML et la convertit en une plus courte commande en String.
Ici le composant va recevoir une direction en XML et la transmet à la caméra en String

<strong>Lancement de l'application :</strong>

Lancer le .jar du dossier target an utilisant la commande suivante dans un terminal: java -jar <i>nom du fichier jar</i>.

<strong>Spécifications UPnP :</strong>

Ce composant présente deux services UPnP, dont voici les descriptions:

    a) RemoteControlService :
    
        1) SetCommande(String NewCommande) : reçoit une commande XML NewCommande et la convertit
        
     Ce service ne fait que recevoir des évènements UPnP, il n'en produit pas.
     
     
    b) ToOnvifService :
    
        1) ActionMove() : permet de spécifier que la caméra doit faire un mouvement. Envoie un évènement Move_Event
        2) SetX(String NewX) : prend en entrée la valeur de la direction que la caméra devra prendre sur l'axe X. 
        Le service envoie cette valeur via l'évènement X_Event.
        3) SetY(String NewY) : prend en entrée la valeur de la direction que la caméra devra prendre sur l'axe Y. 
        Le service envoie cette valeur via l'évènement Y_Event.
       
       
Voici le schéma représentant ce composant ainsi que ses services :

![alt tag](https://github.com/components-upnp/Java_UPnP_To_Onvif_Adapter/blob/master/UPnPToOnvifAdapter.png)
       
<strong>Maintenance : </strong>

L'application se présente sous la forme d'un projet Maven.
