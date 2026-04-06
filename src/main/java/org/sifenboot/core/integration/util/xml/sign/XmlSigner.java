package org.sifenboot.core.integration.util.xml.sign;

import org.sifenboot.core.integration.sifen.config.SifenProperties_Deprecated;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.spec.*;

import org.w3c.dom.Node;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class XmlSigner {

    private final SifenProperties_Deprecated sifenPropertiesDeprecated;

    @Autowired
    public XmlSigner(SifenProperties_Deprecated sifenPropertiesDeprecated) {
        this.sifenPropertiesDeprecated = sifenPropertiesDeprecated;
    }

    public Node sign(Node parentNode,
                     String signedNodeId,
                     X509Certificate certificate,
                     PrivateKey privateKey)
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            MarshalException, XMLSignatureException {

        XMLSignatureFactory sf = XMLSignatureFactory.getInstance("DOM");

        DigestMethod digestMethod =
                sf.newDigestMethod(DigestMethod.SHA256, null);

        List<Transform> transforms = List.of(
                sf.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null),
                sf.newTransform(
                        "http://www.w3.org/2001/10/xml-exc-c14n#",
                        (TransformParameterSpec) null
                )
        );

        Reference reference = sf.newReference(
                "#" + signedNodeId,
                digestMethod,
                transforms,
                null,
                null
        );

        SignedInfo signedInfo = sf.newSignedInfo(
                sf.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE,
                        (C14NMethodParameterSpec) null
                ),
                sf.newSignatureMethod(
                        "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
                        null
                ),
                Collections.singletonList(reference)
        );

        KeyInfoFactory kif = sf.getKeyInfoFactory();
        X509Data x509Data = kif.newX509Data(
                Collections.singletonList(certificate)
        );
        KeyInfo keyInfo = kif.newKeyInfo(
                Collections.singletonList(x509Data)
        );

        DOMSignContext ctx = new DOMSignContext(privateKey, parentNode);
        sf.newXMLSignature(signedInfo, keyInfo).sign(ctx);

        return parentNode;
    }


    public Node sign(Node parentNode, String signedNodeId) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");

            try (InputStream in =
                         new FileInputStream(sifenPropertiesDeprecated.getCertPath())) {

                ks.load(in, sifenPropertiesDeprecated.getCertPass().toCharArray());
            }

            String alias = ks.aliases().nextElement();
            X509Certificate cert =
                    (X509Certificate) ks.getCertificate(alias);

            PrivateKey pk = (PrivateKey) ks.getKey(
                    alias,
                    sifenPropertiesDeprecated.getCertPass().toCharArray()
            );

            return sign(parentNode, signedNodeId, cert, pk);

        } catch (Exception e) {
            Logger.getLogger(XmlSigner.class.getName())
                    .log(Level.SEVERE, null, e);
            return null;
        }
    }
}
