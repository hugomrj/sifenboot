package com.sifen.api.integration.de;

import com.sifen.api.integration.util.time.ClienteNTP;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


@RequestScope
@Component
public class DeComplemento {

    private String id;
    private String dDVId;

    public String getId() {
        return id;
    }

    public String getdDVId() {
        return dDVId;
    }

    public String getJsonCom(String jsonEntrada, String cdc) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dVerFor", 150);
        jsonObject.put("dDVId", cdc.substring(cdc.length() - 1));

        JSONObject jsonParametro = new JSONObject(jsonEntrada);

        if (jsonParametro.has("dFecFirma")) {
            jsonObject.put("dFecFirma", jsonParametro.getString("dFecFirma"));
        } else {
            jsonObject.put("dFecFirma", ClienteNTP.getTimeFormat());
        }

        jsonObject.put("dSisFact", 1);

        return jsonObject.toString();
    }
}
