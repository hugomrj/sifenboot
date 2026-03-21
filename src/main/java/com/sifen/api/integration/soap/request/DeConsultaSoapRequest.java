package com.sifen.api.integration.soap.request;


import com.sifen.api.integration.util.message.SoapIdGenerator;
import org.springframework.stereotype.Component;

@Component
public class DeConsultaSoapRequest {

    public String createQueryXml(String cdc) {

        String xmlId = SoapIdGenerator.generateId();

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
                           xmlns:xsd="http://ekuatia.set.gov.py/sifen/xsd">
                <soap:Header/>
                <soap:Body>
                    <xsd:rEnviConsDeRequest xmlns="http://ekuatia.set.gov.py/sifen/xsd"
                                            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                        <dId>%s</dId>
                        <dCDC>%s</dCDC>
                    </xsd:rEnviConsDeRequest>
                </soap:Body>
            </soap:Envelope>
            """.formatted(xmlId, cdc);
    }
}