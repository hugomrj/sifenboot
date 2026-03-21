package org.sifenboot.integration.util.xml;

import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public  class FileXML {

    public static Node getRootNode(String xml, String root) {

        try {
            xml = xml.replaceAll(">[\\s\r\n]*<", "><");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(
                    new InputSource(new StringReader(xml))
            );

            return document.getElementsByTagName(root).item(0);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(
                    "Error al parsear el XML. Formato incorrecto.", e
            );
        }
    }

    public static String xmlToString(Node node) {

        try {
            StringWriter sw = new StringWriter();

            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.OMIT_XML_DECLARATION, "yes"
            );

            transformer.transform(
                    new DOMSource(node),
                    new StreamResult(sw)
            );

            return sw.toString();

        } catch (TransformerException e) {
            throw new RuntimeException(
                    "Error al convertir Node a String", e
            );
        }
    }




    public static Node getElementByTagName(Node node, String tag) {

        if (!(node instanceof Element)) {
            return null;
        }

        NodeList nodeList = ((Element) node).getElementsByTagName(tag);

        return nodeList.getLength() == 1
                ? nodeList.item(0)
                : null;
    }
}
