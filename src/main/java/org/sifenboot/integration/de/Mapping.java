package org.sifenboot.integration.de;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import java.util.ArrayList;

@Component
@RequestScope
public class Mapping {

    private ArrayList<FieldMapping> elementos = new ArrayList<>();
    private String json;

    public void add(String linea) {

        String[] arraylinea = linea.split(":");
        if (arraylinea.length != 2) {
            return;
        }

        FieldMapping fieldMapping = new FieldMapping();
        fieldMapping.setXml(arraylinea[0].trim());
        fieldMapping.setJson(arraylinea[1].trim());

        this.elementos.add(fieldMapping);
    }

    public ArrayList<FieldMapping> getElementos() {
        return elementos;
    }

    public void setElementos(ArrayList<FieldMapping> elementos) {
        this.elementos = elementos;
    }

    public String getCampoJson(String xml) {

        for (FieldMapping fm : elementos) {
            if (fm.getXml().equals(xml)) {
                return fm.getJson();
            }
        }
        return "";
    }

    public String getValorJson(String campo) {

        if (json == null || json.isEmpty()) {
            return "";
        }

        try {
            JSONObject objeto = new JSONObject(json);
            return objeto.optString(campo, "");
        } catch (Exception e) {
            return "";
        }
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
