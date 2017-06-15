package com.irit.xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by mkostiuk on 14/06/2017.
 *
 * Lecteur XML utilisé afin de lire la requête enoyée par un composant qui
 * donne une direction à prendre (GAUCHE, BAS , DROITE ...)
 */
public class LecteurXmlRemote implements LecteurXml {
    private String udn;
    private String commande;


    public LecteurXmlRemote(String xml)  throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {

            boolean isUdn = false;
            boolean isCommande = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equalsIgnoreCase("UDN"))
                    isUdn = true;
                if (qName.equalsIgnoreCase("Commande"))
                    isCommande = true;
            }

            @Override
            public void characters(char ch[], int start, int length) {
                if (isCommande) {
                    isCommande = false;
                    commande = new String(ch, start, length);
                    System.err.println(commande);
                }
                if (isUdn) {
                    isUdn = false;
                    udn = new String(ch, start, length);
                }
            }
        };
        sp.parse(new InputSource(new StringReader(xml)), handler);
    }

    public String getUdn() {
        return udn;
    }

    public String getCommande() {
        return commande;
    }

    @Override
    public HashMap<String, String> getResultat() {
        HashMap<String,String> res = new HashMap<>();
        res.put("UDN",udn);
        res.put("COMMANDE",commande);

        return res;
    }
}
