package org.sifenboot.core.integration.builder;

import jakarta.persistence.EntityNotFoundException;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.model.EmisorConfiguracion;
import org.sifenboot.app.admin.emisor.repository.EmisorRepository;
import org.sifenboot.core.integration.util.crypto.HashUtils;
import org.sifenboot.core.integration.util.io.IOUtils;
import org.sifenboot.errors.InvalidConfigurationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Component
public class QrNodeBuilder {

    private final EmisorRepository emisorRepository;

    public QrNodeBuilder(EmisorRepository emisorRepository) {
        this.emisorRepository = emisorRepository;
    }

    public Node addQrNode(String emisorCod, Node node) {

        Document doc = node.getOwnerDocument();

        Element gCamFuFD = doc.createElement("gCamFuFD");

        Element dCarQR = doc.createElement("dCarQR");

        String qrLink = generateQRLink(emisorCod, node);

        dCarQR.appendChild(doc.createTextNode(qrLink));

        gCamFuFD.appendChild(dCarQR);

        node.appendChild(gCamFuFD);

        return node;
    }


    public String generateQRLink(String codEmisor, Node root) {


        Document doc = root.getOwnerDocument();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LinkedHashMap<String, String> queryParams = new LinkedHashMap<>();

        Element rootElement = (Element) root;

        queryParams.put(
                "nVersion",
                rootElement.getElementsByTagName("dVerFor")
                        .item(0)
                        .getTextContent()
        );

        Element deElement =
                (Element) rootElement.getElementsByTagName("DE").item(0);

        queryParams.put("Id", deElement.getAttribute("Id"));

        // Fecha emisión
        String dFeEmiDE =
                deElement.getElementsByTagName("dFeEmiDE")
                        .item(0)
                        .getTextContent();

        LocalDateTime dateTime =
                LocalDateTime.parse(
                        dFeEmiDE,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                );

        queryParams.put(
                "dFeEmiDE",
                HashUtils.bytesToHex(
                        dateTime.format(formatter)
                                .getBytes(StandardCharsets.UTF_8)
                )
        );

        // Datos receptor
        Element gDatRec =
                (Element) deElement.getElementsByTagName("gDatRec").item(0);

        String iNatRec =
                gDatRec.getElementsByTagName("iNatRec")
                        .item(0)
                        .getTextContent();

        String iTiOpe =
                gDatRec.getElementsByTagName("iTiOpe")
                        .item(0)
                        .getTextContent();

        NodeList dNumIDRecList =
                gDatRec.getElementsByTagName("dNumIDRec");

        String dNumIDRec =
                dNumIDRecList.getLength() > 0
                        ? dNumIDRecList.item(0).getTextContent()
                        : null;

        if (Integer.parseInt(iNatRec) == 1) {

            queryParams.put(
                    "dRucRec",
                    gDatRec.getElementsByTagName("dRucRec")
                            .item(0)
                            .getTextContent()
            );

        }
        else if (Integer.parseInt(iTiOpe) != 4 && dNumIDRec != null) {

            queryParams.put("dNumIDRec", dNumIDRec);

        }
        else {

            queryParams.put("dNumIDRec", "0");
        }

        // Totales
        String iTiDE =
                deElement.getElementsByTagName("iTiDE")
                        .item(0)
                        .getTextContent();

        if (Integer.parseInt(iTiDE) != 7) {

            queryParams.put(
                    "dTotGralOpe",
                    deElement.getElementsByTagName("dTotGralOpe")
                            .item(0)
                            .getTextContent()
            );

            String iTImp =
                    deElement.getElementsByTagName("iTImp")
                            .item(0)
                            .getTextContent();

            if (Integer.parseInt(iTImp) == 1 ||
                    Integer.parseInt(iTImp) == 5) {

                queryParams.put(
                        "dTotIVA",
                        deElement.getElementsByTagName("dTotIVA")
                                .item(0)
                                .getTextContent()
                );
            }
            else {
                queryParams.put("dTotIVA", "0");
            }

        }
        else {

            queryParams.put("dTotGralOpe", "0");
            queryParams.put("dTotIVA", "0");
        }

        queryParams.put(
                "cItems",
                String.valueOf(
                        deElement.getElementsByTagName("gCamItem")
                                .getLength()
                )
        );

        String digestValue =
                doc.getElementsByTagName("DigestValue")
                        .item(0)
                        .getTextContent();

        queryParams.put(
                "DigestValue",
                HashUtils.bytesToHex(
                        digestValue.getBytes(StandardCharsets.UTF_8)
                )
        );

        // Emisor
        Emisor emisor = emisorRepository
                .findByCodEmisor(codEmisor)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Emisor no encontrado"
                        )
                );

        EmisorConfiguracion config = emisor.getConfiguracion();

        String idCsc = config.getIdCsc();

        queryParams.put("IdCSC", idCsc);

        String ambiente = config.getAmbiente();

        String urlParams = IOUtils.buildUrlParams(queryParams);

        String hashedParams =
                HashUtils.sha256Hex(
                        urlParams + resolveUrlConsultaQr(ambiente)
                );

        String finalUrl =
                resolveUrlConsultaQr(ambiente)
                        + urlParams
                        + "&cHashQR="
                        + hashedParams;

        return finalUrl;
    }



    private String resolveUrlConsultaQr(String ambiente) {

        if ("prod".equalsIgnoreCase(ambiente)) {
            return "https://ekuatia.set.gov.py/consultas/qr?";
        }

        if ("test".equalsIgnoreCase(ambiente)) {
            return "https://ekuatia.set.gov.py/consultas-test/qr?";
        }

        throw new InvalidConfigurationException(
                "Ambiente inválido"
        );
    }


}
