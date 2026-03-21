package com.sifen.api.integration.builder;

import com.sifen.api.integration.de.Mapping;
import com.sifen.api.integration.sifen.util.SifenDvCalculator;
import com.sifen.api.integration.util.io.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class CdcBuilder {

    public static String obtenerCDC(String json) throws Exception {

        Mapping mapping = new Mapping();
        mapping.setJson(json);

        String iTiDE   = mapping.getValorJson("iTiDE");
        String dRucEm  = mapping.getValorJson("dRucEm");
        String dDVEmi  = mapping.getValorJson("dDVEmi");
        String dEst    = mapping.getValorJson("dEst");
        String dPunExp = mapping.getValorJson("dPunExp");
        String dNumDoc = mapping.getValorJson("dNumDoc");
        String iTipCont= mapping.getValorJson("iTipCont");
        String dFeEmiDE= mapping.getValorJson("dFeEmiDE");
        String iTipEmi = mapping.getValorJson("iTipEmi");
        String dCodSeg = mapping.getValorJson("dCodSeg");

        SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fecha = sd1.parse(dFeEmiDE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String cdc = StringUtils.leftPad(iTiDE.trim(), '0', 2)
                + StringUtils.leftPad(dRucEm.trim(), '0', 8)
                + dDVEmi
                + dEst
                + dPunExp
                + StringUtils.leftPad(dNumDoc.trim(), '0', 7)
                + iTipCont
                + sdf.format(fecha)
                + iTipEmi
                + StringUtils.leftPad(dCodSeg.trim(), '0', 9);

        // agrega el dígito verificador
        return cdc + SifenDvCalculator.generateDvCdc(cdc);
    }

}
