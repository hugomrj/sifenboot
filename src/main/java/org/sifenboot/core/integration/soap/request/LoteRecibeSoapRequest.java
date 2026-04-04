package org.sifenboot.core.integration.soap.request;

import org.sifenboot.core.integration.util.message.SoapIdGenerator;
import org.springframework.stereotype.Component;

@Component
public class LoteRecibeSoapRequest {

    public String createEnvioXml(String base64Zip) {

        String xmlId = SoapIdGenerator.generateId();

        return """
        <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
            <soap:Header/>
            <soap:Body>
                <rEnvioLote xmlns="http://ekuatia.set.gov.py/sifen/xsd">
                    <dId>%s</dId>
                    <xDE>%s</xDE>
                </rEnvioLote>
            </soap:Body>
        </soap:Envelope>
        """.formatted(xmlId, base64Zip);
    }
}