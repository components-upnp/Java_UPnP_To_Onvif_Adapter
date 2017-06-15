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
 * Lecteur utilisé pour lire la requête envoyée par un composant qui donne son
 * orientation sur les axes X, Y et Z.
 */
public class LecteurXmlOrientation implements LecteurXml {

    private String udn;
    private String x, y, z;

    public LecteurXmlOrientation(String xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {

            boolean isUdn = false;
            boolean isX = false;
            boolean isY = false;
            boolean isZ = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equalsIgnoreCase("UDN"))
                    isUdn = true;
                if (qName.equalsIgnoreCase("X"))
                    isX = true;
                if (qName.equalsIgnoreCase("Y"))
                    isY = true;
                if (qName.equalsIgnoreCase("Z"))
                    isZ = true;
            }

            @Override
            public void characters(char ch[], int start, int length) {
                if (isX) {
                    isX = false;
                    x = new String(ch, start, length);
                }
                if (isY) {
                    isY = false;
                    y = new String(ch, start, length);
                }
                if (isZ) {
                    isZ = false;
                    z = new String(ch, start, length);
                }
                if (isUdn) {
                    isUdn = false;
                    udn = new String(ch, start, length);
                }
            }
        };
        sp.parse(new InputSource(new StringReader(xml)), handler);
    }

    @Override
    public HashMap<String, String> getResultat() {
        HashMap<String,String> res = new HashMap<>();
        res.put("UDN",udn);
        res.put("X",x);
        res.put("Y",y);
        res.put("Z",z);
        return res;
    }
}
