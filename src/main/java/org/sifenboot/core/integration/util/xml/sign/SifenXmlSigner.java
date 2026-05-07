package org.sifenboot.core.integration.util.xml.sign;

import org.sifenboot.core.integration.util.xml.FileXML;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

        // firmar y retornar
        return xmlSigner.sign(emisorCod, root, signedNodeId);
    }
}
