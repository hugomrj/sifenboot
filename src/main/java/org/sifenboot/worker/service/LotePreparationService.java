package org.sifenboot.worker.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LotePreparationService {

    public List<String> generarXmls(List<String> documentos ) {

        List<String> xmls = new ArrayList<>();

        for (String documento : documentos) {


            // ver si es formato correcto

            String xml = """
                    <DE>
                        <Id>%s</Id>
                    </DE>
                    """.formatted(documento);

            xmls.add(xml);
        }

        return xmls;
    }
}