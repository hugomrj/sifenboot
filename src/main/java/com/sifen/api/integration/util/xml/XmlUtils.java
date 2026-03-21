package com.sifen.api.integration.util.xml;


import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public abstract class XmlUtils {

    public static String obtenerCdcDesdeXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        NodeList deNodes = doc.getElementsByTagNameNS("*", "DE");

        if (deNodes.getLength() == 0) {
            return null;
        }

        Element deElement = (Element) deNodes.item(0);
        return deElement.getAttribute("Id");
    }
}