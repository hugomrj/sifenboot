package org.sifenboot.core.lote.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoteErrorResponse {

    private String dFecProc;
    private String dMsgResLot;
    private String dCodResLot;

    /* getters y setters */

    public String getDFecProc() {
        return dFecProc;
    }

    public void setDFecProc(String dFecProc) {
        this.dFecProc = dFecProc;
    }

    public String getDMsgResLot() {
        return dMsgResLot;
    }

    public void setDMsgResLot(String dMsgResLot) {
        this.dMsgResLot = dMsgResLot;
    }

    public String getDCodResLot() {
        return dCodResLot;
    }

    public void setDCodResLot(String dCodResLot) {
        this.dCodResLot = dCodResLot;
    }
}
