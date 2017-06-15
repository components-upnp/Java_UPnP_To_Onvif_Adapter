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
 * Created by mkostiuk on 30/05/2017.
 *
 * Interface décrivant la méthode que doit impélmenter un lecteur XML.
 * On pourra comme ça implémenter différents lecteurs XML selon le service UPnP par lequel
 * est entré le message (entre autres quel type de composant est à l'origine du message).
 */
public interface LecteurXml {
    public HashMap<String,String> getResultat();
}
