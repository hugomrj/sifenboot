package org.sifenboot.core.integration.soap.request;

import org.sifenboot.core.integration.builder.EventoSoapBuilder;
import org.sifenboot.core.integration.util.message.SoapIdGenerator;
import org.sifenboot.core.integration.util.time.ClienteNTP;
import org.sifenboot.core.integration.util.xml.FileXML;
import org.sifenboot.core.integration.util.xml.sign.EventoXmlSigner;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
public class EventoCancelarSoapRequest {

    private final EventoSoapBuilder eventoSoapBuilder;
    private final EventoXmlSigner eventoXmlSigner;

    public EventoCancelarSoapRequest(EventoSoapBuilder eventoSoapBuilder,
                                     EventoXmlSigner eventoXmlSigner) {
        this.eventoSoapBuilder = eventoSoapBuilder;
        this.eventoXmlSigner = eventoXmlSigner;
    }

    public String createCancelarXml(String cdc, String motivo) {


        String xmlId = SoapIdGenerator.generateId();
        String fechaFirma = ClienteNTP.getTimeFormat();


        String xmlBase = """
        <gGroupGesEve xmlns="http://ekuatia.set.gov.py/sifen/xsd"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://ekuatia.set.gov.py/sifen/xsd siRecepEvento_v150.xsd">
            <rGesEve xsi:schemaLocation="https://ekuatia.set.gov.py/sifen/xsd siRecepEvento_v150.xsd">
                <rEve Id="%s">
                    <dFecFirma>%s</dFecFirma>
                    <dVerFor>150</dVerFor>
                    <gGroupTiEvt>
                        <rGeVeCan>
                            <Id>%s</Id>
                            <mOtEve>%s</mOtEve>
                        </rGeVeCan>
                    </gGroupTiEvt>
                </rEve>
            </rGesEve>
        </gGroupGesEve>
        """.formatted(xmlId, fechaFirma, cdc, motivo);


        // FIRMAR XML
        Node firmado = eventoXmlSigner.firmar(xmlBase);

        String xmlFirmado = FileXML.xmlToString(firmado);

        return eventoSoapBuilder.build(xmlId, xmlFirmado);
    }



}
