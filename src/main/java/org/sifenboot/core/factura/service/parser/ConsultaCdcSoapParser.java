package org.sifenboot.core.factura.service.parser;

import org.sifenboot.core.factura.dto.response.ConsultaCdcResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;


public class ConsultaCdcSoapParser {

    public ConsultaCdcResponse parse(String xml) {

        try {
            String xmlValido = limpiarSoap(xml);

            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setNamespaceAware(true);

            Document doc = f.newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xmlValido)));

            ConsultaCdcResponse r = new ConsultaCdcResponse();
            r.setRespuestaXml(xml);
            r.setEstado("OK");
            r.setFechaProceso(text(doc, "dFecProc"));
            r.setCodigoResultado(text(doc, "dCodRes"));
            r.setMensaje(text(doc, "dMsgRes"));

            return r;

        } catch (Exception e) {
            throw new IllegalStateException("Error parseando respuesta SOAP", e);
        }
    }

    private String text(Document doc, String tag) {
        NodeList list = doc.getElementsByTagNameNS("*", tag);
        return (list != null && list.getLength() > 0)
                ? list.item(0).getTextContent()
                : null;
    }

    private String limpiarSoap(String xml) {

        int ini = xml.indexOf("<env:Envelope");
        if (ini == -1) ini = xml.indexOf("<soap:Envelope");
        if (ini == -1) ini = xml.indexOf("<Envelope");

        int fin = xml.lastIndexOf("</env:Envelope>");
        if (fin == -1) fin = xml.lastIndexOf("</soap:Envelope>");
        if (fin == -1) fin = xml.lastIndexOf("</Envelope>");

        if (ini == -1 || fin == -1) {
            throw new IllegalStateException("Envelope SOAP no encontrado");
        }

        fin = xml.indexOf(">", fin) + 1;
        String env = xml.substring(ini, fin);

        return env.replaceAll("<\\?xml[^>]*\\?>", "").trim();
    }
}