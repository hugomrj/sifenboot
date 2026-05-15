package org.sifenboot.core.integration.util.xml.sign;

import org.sifenboot.core.integration.util.xml.FileXML;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Component
public class SifenXmlSigner {

    private final XmlSigner xmlSigner;

    public SifenXmlSigner(XmlSigner xmlSigner) {
        this.xmlSigner = xmlSigner;
    }

    public Node signXml(String emisorCod, String xml) {

        // obtener nodo raíz
        Node root = FileXML.getRootNode(xml, "rDE");

        // ubicar nodo DE
        Node n = FileXML.getElementByTagName(root, "DE");
        Element signedElement = (Element) n;

        String signedNodeId = signedElement.getAttribute("Id");
        signedElement.setIdAttribute("Id", true);

        // firmar
        Node signedNode = xmlSigner.sign(emisorCod, root, signedNodeId);




        // DEBUG XML FIRMADO
        try {

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();

            transformer.transform(
                    new DOMSource(signedNode),
                    new StreamResult(writer)
            );

            System.out.println("\n===== XML FIRMADO =====");
            System.out.println(writer);
            System.out.println("=======================\n");

        } catch (Exception e) {
            e.printStackTrace();
        }






        // retornar
        return signedNode;
    }
}
