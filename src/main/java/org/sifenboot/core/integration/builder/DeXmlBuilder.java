package org.sifenboot.core.integration.builder;

import org.sifenboot.core.integration.de.DeComplemento;
import org.sifenboot.core.integration.de.Mapping;
import org.sifenboot.core.integration.de.structure.DeXmlElement;
import org.sifenboot.core.integration.de.structure.DeXmlStructure;
import org.sifenboot.core.integration.sifen.config.SifenProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequestScope
public class DeXmlBuilder {

    private final SifenProperties sifenProperties;
    private final DeComplemento complegen;
    private final Mapping mapping;
    private final DeXmlStructure xmlEstructura;

    public DeXmlBuilder(SifenProperties sifenProperties,
                        DeComplemento complegen,
                        Mapping mapping,
                        DeXmlStructure xmlEstructura) {
        this.sifenProperties = sifenProperties;
        this.complegen = complegen;
        this.mapping = mapping;
        this.xmlEstructura = xmlEstructura;
    }

    public SifenProperties getSifenProperties() {
        return sifenProperties;
    }

    public DeComplemento getComplegen() {
        return complegen;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public DeXmlStructure getXmlEstructura() {
        return xmlEstructura;
    }

    private final String declaracion = """
        xmlns="http://ekuatia.set.gov.py/sifen/xsd"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="https://ekuatia.set.gov.py/sifen/xsd siRecepDE_v150.xsd"
        """;

    public String generateXml(String jsonInput, String cdc) {

        mapping.setJson(jsonInput);

        DeXmlElement rootElement = xmlEstructura.generar_esquema(cdc);

        StringBuilder xmlOutput = new StringBuilder();
        xmlOutput.append("<")
                .append(rootElement.getNombre())
                .append(" ")
                .append(declaracion)
                .append(">");

        for (DeXmlElement hijo : rootElement.getHijos()) {
            xmlOutput.append(buildElementValue(hijo));
        }

        xmlOutput.append("</")
                .append(rootElement.getNombre())
                .append(">");

        return xmlOutput.toString();
    }

    public String buildElementValue(DeXmlElement elemento) {

        String ret = "<" + elemento.getNombre();

        if (!elemento.getAtributos().isEmpty()) {
            ret += elemento.strAtributos();
        }
        ret += ">";

        if (!elemento.getHijos().isEmpty()) {
            for (DeXmlElement hijo : elemento.getHijos()) {
                ret += buildElementValue(hijo);
            }
        } else {
            String valor = elemento.getValor();
            if (valor == null) {
                if (!elemento.getAtributos().isEmpty()) {
                    ret = ret.substring(0, ret.length() - 1) + "/>";
                } else {
                    return "";
                }
            } else {
                ret += valor;
            }
        }

        if (!ret.endsWith("/>")) {
            ret += "</" + elemento.getNombre() + ">";
        }

        ret = ret.trim();

        Pattern pattern = Pattern.compile("<[a-zA-Z0-9]+></[a-zA-Z0-9]+>");
        Matcher matcher = pattern.matcher(ret);
        if (matcher.matches()) {
            return "";
        }

        return ret;
    }

    public String getTag(String xmlData, String tag) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(
                    new ByteArrayInputStream(xmlData.getBytes())
            );

            NodeList nodeList = document.getElementsByTagName(tag);

            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getTextContent();
            }
        } catch (Exception ignored) {
        }

        return "";
    }

    public String getCDC(String xmlData) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = factory.newDocumentBuilder().parse(
                    new org.xml.sax.InputSource(
                            new java.io.StringReader(xmlData)
                    )
            );

            NodeList deNodes = document.getElementsByTagName("DE");
            if (deNodes.getLength() > 0) {
                return ((Element) deNodes.item(0)).getAttribute("Id");
            }
        } catch (Exception ignored) {
        }

        return "error";
    }
}
