package org.sifenboot.core.integration.soap.request;


import org.sifenboot.core.integration.util.message.SoapIdGenerator;
import org.springframework.stereotype.Component;

@Component
public class LoteConsultaSoapRequest {

    public String createQueryXml(String lote) {

        String xmlId = SoapIdGenerator.generateId();

        return """
        <?xml version="1.0" encoding="UTF-8"?>
        <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" 
                       xmlns:xsd="http://ekuatia.set.gov.py/sifen/xsd">
            <soap:Header/>
            <soap:Body>
                <xsd:rEnviConsLoteDe xmlns="http://ekuatia.set.gov.py/sifen/xsd"
                                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                     xsi:schemaLocation="http://ekuatia.set.gov.py/sifen/xsd/ siConsRUC_v150.xsd">
                    <xsd:dId>%s</xsd:dId>
                    <xsd:dProtConsLote>%s</xsd:dProtConsLote>
                </xsd:rEnviConsLoteDe>
            </soap:Body>
        </soap:Envelope>
        """.formatted(xmlId, lote);

    }

}