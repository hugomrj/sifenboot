package com.sifen.api.dto.Lote.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoteOkResponse {

    private String dProtConsLote;

    private String dFecProc;

    private Integer dTpoProces;

    private String dMsgRes;

    private String dCodRes;

    private String cdc;


    // 👇 campo interno tuyo
    private String xmlFinal;



    /* getters y setters */

    public String getDProtConsLote() {
        return dProtConsLote;
    }

    public void setDProtConsLote(String dProtConsLote) {
        this.dProtConsLote = dProtConsLote;
    }

    public String getDFecProc() {
        return dFecProc;
    }

    public void setDFecProc(String dFecProc) {
        this.dFecProc = dFecProc;
    }

    public Integer getDTpoProces() {
        return dTpoProces;
    }

    public void setDTpoProces(Integer dTpoProces) {
        this.dTpoProces = dTpoProces;
    }

    public String getDMsgRes() {
        return dMsgRes;
    }

    public void setDMsgRes(String dMsgRes) {
        this.dMsgRes = dMsgRes;
    }

    public String getDCodRes() {
        return dCodRes;
    }

    public void setDCodRes(String dCodRes) {
        this.dCodRes = dCodRes;
    }

    public String getXmlFinal() {
        return xmlFinal;
    }

    public void setXmlFinal(String xmlFinal) {
        this.xmlFinal = xmlFinal;
    }

    public String getCdc() {
        return cdc;
    }

    public void setCdc(String cdc) {
        this.cdc = cdc;
    }
}
