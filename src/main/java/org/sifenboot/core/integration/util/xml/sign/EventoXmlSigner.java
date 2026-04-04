package org.sifenboot.core.integration.util.xml.sign;

import org.sifenboot.core.integration.sifen.config.SifenProperties;
import org.sifenboot.core.integration.util.xml.FileXML;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class EventoXmlSigner {

    private final SifenProperties sifenProperties;
    private final XmlSigner xmlSigner;

    public EventoXmlSigner(SifenProperties sifenProperties, XmlSigner xmlSigner) {
        this.sifenProperties = sifenProperties;
        this.xmlSigner = xmlSigner;
    }

    public Node firmar(String xml) {

        try {
            // 1️⃣ Obtener nodo raíz a firmar
            Node root = FileXML.getRootNode(xml, "rGesEve");

            // 2️⃣ Obtener rEve y marcar Id como ID XML
            Node eveNode = FileXML.getElementByTagName(root, "rEve");
            Element eveElement = (Element) eveNode;

            String signedNodeId = eveElement.getAttribute("Id");
            eveElement.setIdAttribute("Id", true);

            // evaluar unificar con PKCS12 (factura); por ahora se mantiene defaul
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            System.out.println("KeyStore default type = " + KeyStore.getDefaultType());
            // KeyStore default type = pkcs12


                try (InputStream in = new FileInputStream(
                    sifenProperties.getCertPath()
            )) {
                ks.load(in, sifenProperties.getCertPass().toCharArray());
            }

            String alias = ks.aliases().nextElement();

            X509Certificate certificate =
                    (X509Certificate) ks.getCertificate(alias);

            PrivateKey privateKey = (PrivateKey) ks.getKey(
                    alias,
                    sifenProperties.getCertPass().toCharArray()
            );

            // 4️⃣ Firmar (misma canonicalización que legacy)
            return xmlSigner.sign(
                    root,
                    signedNodeId,
                    certificate,
                    privateKey
            );

        } catch (Exception e) {
            Logger.getLogger(EventoXmlSigner.class.getName())
                    .log(Level.SEVERE, null, e);
            return null;
        }
    }
}
