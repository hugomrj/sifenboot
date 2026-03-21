package com.sifen.api.integration.de.structure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sifen.api.integration.de.Mapping;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Service
public class DeXmlStructure {

    private final ObjectFactory<Mapping> mappingFactory;
    private final ObjectMapper objectMapper;

    public DeXmlStructure(ObjectFactory<Mapping> mappingFactory,
                          ObjectMapper objectMapper) {
        this.mappingFactory = mappingFactory;
        this.objectMapper = objectMapper;
    }

    public DeXmlElement generar_esquema(String cdc) {

        Mapping mapping = mappingFactory.getObject();

        JsonNode jsonobject;
        try {
            jsonobject = objectMapper.readTree(mapping.getJson());
        } catch (Exception e) {
            throw new RuntimeException("Error parseando JSON", e);
        }

        DeXmlElement rDE = new DeXmlElement("rDE");

        rDE.getHijos().add(new DeXmlElement("dVerFor", mapping, null ) );

        DeXmlElement DE = new DeXmlElement("DE");
        DE.getAtributos().add(new XmlAttribute("Id", cdc ));
        rDE.getHijos().add(DE);

        DE.getHijos().add(new DeXmlElement("dDVId", mapping, null ) );
        DE.getHijos().add(new DeXmlElement("dFecFirma" , mapping, null));
        DE.getHijos().add(new DeXmlElement("dSisFact" , mapping, null ));

        DeXmlElement gOpeDE = new DeXmlElement("gOpeDE");
        DE.getHijos().add(gOpeDE);

        gOpeDE.hijos_add( mapping, "iTipEmi", jsonobject );
        gOpeDE.hijos_add( mapping, "dDesTipEmi", jsonobject );
        gOpeDE.hijos_add( mapping, "dCodSeg", jsonobject );

        gOpeDE.hijos_add( mapping, "dInfoEmi", jsonobject );
        gOpeDE.hijos_add( mapping, "dInfoFisc", jsonobject );


        DeXmlElement gTimb = new DeXmlElement("gTimb");
        DE.getHijos().add(gTimb);


        gTimb.getHijos().add(new DeXmlElement("iTiDE", mapping,  null ));
        gTimb.getHijos().add(new DeXmlElement("dDesTiDE", mapping,  null ));
        gTimb.getHijos().add(new DeXmlElement("dNumTim", mapping,  null ));
        gTimb.getHijos().add(new DeXmlElement("dEst", mapping,  null ));
        gTimb.getHijos().add(new DeXmlElement("dPunExp", mapping,  null ));
        gTimb.getHijos().add(new DeXmlElement("dNumDoc", mapping,  null ));

        gTimb.hijos_add( mapping, "dSerieNum", jsonobject );
        gTimb.hijos_add( mapping, "dFeIniT", jsonobject );

        DeXmlElement gDatGralOpe = new DeXmlElement("gDatGralOpe");
        DE.getHijos().add(gDatGralOpe);

        gDatGralOpe.getHijos().add(new DeXmlElement("dFeEmiDE", mapping,  null  ));

        DeXmlElement gOpeCom = new DeXmlElement("gOpeCom");
        gDatGralOpe.getHijos().add(gOpeCom);

        gOpeCom.hijos_add( mapping, "iTipTra", jsonobject );
        gOpeCom.hijos_add( mapping, "dDesTipTra", jsonobject );
        gOpeCom.hijos_add( mapping, "iTImp", jsonobject );
        gOpeCom.hijos_add( mapping, "dDesTImp", jsonobject );
        gOpeCom.hijos_add( mapping, "cMoneOpe", jsonobject );
        gOpeCom.hijos_add( mapping, "dDesMoneOpe", jsonobject );

        gOpeCom.hijos_add( mapping, "dCondTiCam", jsonobject );
        gOpeCom.hijos_add( mapping, "dTiCam", jsonobject );

        gOpeCom.hijos_add( mapping, "iCondAnt", jsonobject );
        gOpeCom.hijos_add( mapping, "dDesCondAnt", jsonobject );


        DeXmlElement gEmis = new DeXmlElement("gEmis");
        gDatGralOpe.getHijos().add(gEmis);

        gEmis.getHijos().add(new DeXmlElement("dRucEm", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("dDVEmi", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("iTipCont", mapping,  null ));

        gEmis.hijos_add( mapping, "cTipReg", jsonobject );

        gEmis.getHijos().add(new DeXmlElement("dNomEmi", mapping,  null ));

        gEmis.hijos_add( mapping, "dNomFanEmi", jsonobject );

        gEmis.getHijos().add(new DeXmlElement("dDirEmi", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("dNumCas", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("cDepEmi", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("dDesDepEmi", mapping,  null ));


        gEmis.hijos_add( mapping, "cDisEmi", jsonobject );
        gEmis.hijos_add( mapping, "dDesDisEmi", jsonobject );



        gEmis.getHijos().add(new DeXmlElement("cCiuEmi", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("dDesCiuEmi", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("dTelEmi", mapping,  null ));
        gEmis.getHijos().add(new DeXmlElement("dEmailE", mapping,  null ));

        DeXmlElement gActEco = new DeXmlElement("gActEco");
        gEmis.getHijos().add(gActEco);

        gActEco.getHijos().add(new DeXmlElement("cActEco", mapping,  null ));
        gActEco.getHijos().add(new DeXmlElement("dDesActEco", mapping,  null ));


        DeXmlElement gDatRec = new DeXmlElement("gDatRec");
        gDatGralOpe.getHijos().add(gDatRec);


        gDatRec.getHijos().add(new DeXmlElement("iNatRec", mapping,  null ));
        gDatRec.getHijos().add(new DeXmlElement("iTiOpe", mapping,  null ));
        gDatRec.getHijos().add(new DeXmlElement("cPaisRec", mapping,  null ));
        gDatRec.getHijos().add(new DeXmlElement("dDesPaisRe", mapping,  null ));


        gDatRec.hijos_add( mapping, "iTiContRec", jsonobject );
        gDatRec.hijos_add( mapping, "dRucRec", jsonobject );
        gDatRec.hijos_add( mapping, "dDVRec", jsonobject );
        gDatRec.hijos_add( mapping, "iTipIDRec", jsonobject );
        gDatRec.hijos_add( mapping, "dDTipIDRec", jsonobject );
        gDatRec.hijos_add( mapping, "dNumIDRec", jsonobject );
        gDatRec.hijos_add( mapping, "dNomRec", jsonobject );

        gDatRec.hijos_add( mapping, "dDirRec", jsonobject );
        gDatRec.hijos_add( mapping, "dNumCasRec", jsonobject );
        gDatRec.hijos_add( mapping, "cDepRec", jsonobject );
        gDatRec.hijos_add( mapping, "dDesDepRec", jsonobject );
        gDatRec.hijos_add( mapping, "cDisRec", jsonobject );
        gDatRec.hijos_add( mapping, "dDesDisRec", jsonobject );
        gDatRec.hijos_add( mapping, "cCiuRec", jsonobject );
        gDatRec.hijos_add( mapping, "dDesCiuRec", jsonobject );
        gDatRec.hijos_add( mapping, "dTelRec", jsonobject );
        gDatRec.hijos_add( mapping, "dCelRec", jsonobject );
        gDatRec.hijos_add( mapping, "dEmailRec", jsonobject );
        gDatRec.hijos_add( mapping, "dCodCliente", jsonobject );


        DeXmlElement gDtipDE = new DeXmlElement("gDtipDE");
        DE.getHijos().add(gDtipDE);

        DeXmlElement gCamFE = new DeXmlElement("gCamFE");
        gDtipDE.getHijos().add(gCamFE);

        gCamFE.hijos_add( mapping, "iIndPres", jsonobject );
        gCamFE.hijos_add( mapping, "dDesIndPres", jsonobject );
        gCamFE.hijos_add( mapping, "dFecEmNR", jsonobject );


        DeXmlElement gCompPub = new DeXmlElement("gCompPub");
        gCamFE.getHijos().add(gCompPub);

        gCompPub.hijos_add( mapping, "dModCont", jsonobject );
        gCompPub.hijos_add( mapping, "dEntCont", jsonobject );
        gCompPub.hijos_add( mapping, "dAnoCont", jsonobject );
        gCompPub.hijos_add( mapping, "dSecCont", jsonobject );
        gCompPub.hijos_add( mapping, "dFeCodCont", jsonobject );


        //  Campos que componen la Autofactura Electrónica AFE (E300-E399)
        DeXmlElement gCamAE = new DeXmlElement("gCamAE");
        gDtipDE.getHijos().add(gCamAE);

        gCamAE.hijos_add( mapping, "iNatVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDesNatVen", jsonobject );
        gCamAE.hijos_add( mapping, "iTipIDVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDTipIDVen", jsonobject );
        gCamAE.hijos_add( mapping, "dNumIDVen", jsonobject );
        gCamAE.hijos_add( mapping, "dNomVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDirVen", jsonobject );
        gCamAE.hijos_add( mapping, "dNumCasVen", jsonobject );
        gCamAE.hijos_add( mapping, "cDepVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDesDepVen", jsonobject );
        gCamAE.hijos_add( mapping, "cDisVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDesDisVen", jsonobject );
        gCamAE.hijos_add( mapping, "cCiuVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDesCiuVen", jsonobject );
        gCamAE.hijos_add( mapping, "dDirProv", jsonobject );
        gCamAE.hijos_add( mapping, "cDepProv", jsonobject );
        gCamAE.hijos_add( mapping, "dDesDepProv", jsonobject );
        gCamAE.hijos_add( mapping, "cDisProv", jsonobject );
        gCamAE.hijos_add( mapping, "dDesDisProv", jsonobject );
        gCamAE.hijos_add( mapping, "cCiuProv", jsonobject );
        gCamAE.hijos_add( mapping, "dDesCiuProv", jsonobject );


        //  Campos que componen la Nota de Crédito/Débito Electrónica NCE-NDE (E400-E499)
        DeXmlElement gCamNCDE = new DeXmlElement("gCamNCDE");
        gDtipDE.getHijos().add(gCamNCDE);

        gCamNCDE.hijos_add( mapping, "iMotEmi", jsonobject );
        gCamNCDE.hijos_add( mapping, "dDesMotEmi", jsonobject );



        //  E6. Campos que componen la Nota de Remisión Electrónica (E500-E599)
        DeXmlElement gCamNRE = new DeXmlElement("gCamNRE");
        gDtipDE.getHijos().add(gCamNRE);

        gCamNRE.hijos_add( mapping, "iMotEmiNR", jsonobject );
        gCamNRE.hijos_add( mapping, "dDesMotEmiNR", jsonobject );
        gCamNRE.hijos_add( mapping, "iRespEmiNR", jsonobject );
        gCamNRE.hijos_add( mapping, "dDesRespEmiNR", jsonobject );
        gCamNRE.hijos_add( mapping, "dKmR", jsonobject );
        gCamNRE.hijos_add( mapping, "dFecEm", jsonobject );


        //  E7. Campos que describen la condición de la operación (E600-E699)

        DeXmlElement gCamCond = new DeXmlElement("gCamCond");
        gDtipDE.getHijos().add(gCamCond);

        gCamCond.hijos_add( mapping, "iCondOpe", jsonobject );
        gCamCond.hijos_add( mapping, "dDCondOpe", jsonobject );


        DeXmlElement gPaConEIni = new DeXmlElement("gPaConEIni");
        gCamCond.getHijos().add(gPaConEIni);

        gPaConEIni.hijos_add( mapping, "iTiPago", jsonobject );
        gPaConEIni.hijos_add( mapping, "dDesTiPag", jsonobject );
        gPaConEIni.hijos_add( mapping, "dMonTiPag", jsonobject );
        gPaConEIni.hijos_add( mapping, "cMoneTiPag", jsonobject );
        gPaConEIni.hijos_add( mapping, "dDMoneTiPag", jsonobject );
        gPaConEIni.hijos_add( mapping, "dTiCamTiPag", jsonobject );


        DeXmlElement gPagTarCD = new DeXmlElement("gPagTarCD");
        gPaConEIni.getHijos().add(gPagTarCD);
        gPagTarCD.hijos_add( mapping, "iDenTarj", jsonobject );
        gPagTarCD.hijos_add( mapping, "dDesDenTarj", jsonobject );
        gPagTarCD.hijos_add( mapping, "dRSProTar", jsonobject );
        gPagTarCD.hijos_add( mapping, "iForProPa", jsonobject );
        gPagTarCD.hijos_add( mapping, "dCodAuOpe", jsonobject );
        gPagTarCD.hijos_add( mapping, "dNomTit", jsonobject );
        gPagTarCD.hijos_add( mapping, "dNumTarj", jsonobject );


        DeXmlElement gPagCheq = new DeXmlElement("gPagCheq");
        gPaConEIni.getHijos().add(gPagCheq);
        gPagCheq.hijos_add( mapping, "dNumCheq", jsonobject );
        gPagCheq.hijos_add( mapping, "dBcoEmi", jsonobject );


        //  E7.2. Campos que describen la operación a crédito (E640-E649)
        DeXmlElement gPagCred = new DeXmlElement("gPagCred");
        gCamCond.getHijos().add(gPagCred);

        gPagCred.hijos_add( mapping, "iCondCred", jsonobject );
        gPagCred.hijos_add( mapping, "dDCondCred", jsonobject );
        gPagCred.hijos_add( mapping, "dPlazoCre", jsonobject );
        gPagCred.hijos_add( mapping, "dCuotas", jsonobject );
        gPagCred.hijos_add( mapping, "dMonEnt", jsonobject );



        //  E7.2.1. Campos que describen las cuotas (E650-E659)
        DeXmlElement gCuotas = new DeXmlElement("gCuotas");
        gPagCred.getHijos().add(gCuotas);
        gCuotas.hijos_add( mapping, "cMoneCuo", jsonobject );
        gCuotas.hijos_add( mapping, "dDMoneCuo", jsonobject );
        gCuotas.hijos_add( mapping, "dMonCuota", jsonobject );
        gCuotas.hijos_add( mapping, "dVencCuo", jsonobject );


        JsonNode arrayDetalles = jsonobject.path("Detalles");
        int cantdet = arrayDetalles.isArray() ? arrayDetalles.size() : 0;




        for (int i = 0; i < cantdet ; i++) {

            //JsonObject jsonObject = arrayDetalles.get(i).getAsJsonObject();
            JsonNode jsonObject = arrayDetalles.get(i);

            DeXmlElement gCamItem = new DeXmlElement("gCamItem");
            gDtipDE.getHijos().add(gCamItem);


            gCamItem.getHijos().add(new DeXmlElement("dCodInt", jsonObject));

            gCamItem.getHijos().add(new DeXmlElement("dDesProSer", jsonObject));
            gCamItem.getHijos().add(new DeXmlElement("cUniMed", jsonObject));
            gCamItem.getHijos().add(new DeXmlElement("dDesUniMed", jsonObject));
            gCamItem.getHijos().add(new DeXmlElement("dCantProSer", jsonObject));


            DeXmlElement gValorItem = new DeXmlElement("gValorItem");
            gCamItem.getHijos().add(gValorItem);

            gValorItem.getHijos().add(new DeXmlElement("dPUniProSer", jsonObject));
            gValorItem.getHijos().add(new DeXmlElement("dTiCamIt", jsonObject));
            gValorItem.getHijos().add(new DeXmlElement("dTotBruOpeItem", jsonObject));

            DeXmlElement gValorRestaItem = new DeXmlElement("gValorRestaItem");
            gValorItem.getHijos().add(gValorRestaItem);

            //gValorRestaItem.getHijos().add(new DeXmlElement(, jsonObject));
            gValorRestaItem.hijos_add("dDescItem", jsonObject);
            gValorRestaItem.hijos_add("dPorcDesIt", jsonObject);
            gValorRestaItem.hijos_add("dDescGloItem", jsonObject);
            gValorRestaItem.hijos_add("dAntPreUniIt", jsonObject);
            gValorRestaItem.hijos_add("dTotOpeItem", jsonObject);
            gValorRestaItem.hijos_add("dTotOpeGs", jsonObject);


            DeXmlElement gCamIVA = new DeXmlElement("gCamIVA");
            gCamItem.getHijos().add(gCamIVA);

            gCamIVA.getHijos().add(new DeXmlElement("iAfecIVA", jsonObject));
            gCamIVA.getHijos().add(new DeXmlElement("dDesAfecIVA" , jsonObject));
            gCamIVA.getHijos().add(new DeXmlElement("dPropIVA" , jsonObject));
            gCamIVA.getHijos().add(new DeXmlElement("dTasaIVA" , jsonObject));

            gCamIVA.getHijos().add(new DeXmlElement("dBasGravIVA" , jsonObject));
            gCamIVA.getHijos().add(new DeXmlElement("dLiqIVAItem" , jsonObject));
            gCamIVA.getHijos().add(new DeXmlElement("dBasExe" , jsonObject));


        }
        // fin detelles



        DeXmlElement gCamEsp = new DeXmlElement("gCamEsp");
        gDtipDE.getHijos().add(gCamEsp);

        // E9.2. Sector Energía Eléctrica (E791-E799)
        DeXmlElement gGrupEner = new DeXmlElement("gGrupEner");
        gCamEsp.getHijos().add(gGrupEner);
        gGrupEner.hijos_add( mapping, "dNroMed", jsonobject );
        gGrupEner.hijos_add( mapping, "dActiv", jsonobject );
        gGrupEner.hijos_add( mapping, "dCateg", jsonobject );
        gGrupEner.hijos_add( mapping, "dLecAnt", jsonobject );
        gGrupEner.hijos_add( mapping, "dLecAct", jsonobject );
        gGrupEner.hijos_add( mapping, "dConKwh", jsonobject );


        // E9.4. Sector de Supermercados (E810-E819)
        DeXmlElement gGrupSup = new DeXmlElement("gGrupSup");
        gCamEsp.getHijos().add(gGrupSup);
        gGrupSup.hijos_add( mapping, "dNomCaj", jsonobject );
        gGrupSup.hijos_add( mapping, "dEfectivo", jsonobject );
        gGrupSup.hijos_add( mapping, "dVuelto", jsonobject );
        gGrupSup.hijos_add( mapping, "dDonac", jsonobject );
        gGrupSup.hijos_add( mapping, "dDesDonac", jsonobject );


        //  E9.5. Grupo de datos adicionales de uso comercial (E820-E829)
        DeXmlElement gGrupAdi = new DeXmlElement("gGrupAdi");
        gCamEsp.getHijos().add(gGrupAdi);

        gGrupAdi.hijos_add( mapping, "dCiclo", jsonobject );
        gGrupAdi.hijos_add( mapping, "dFecIniC", jsonobject );
        gGrupAdi.hijos_add( mapping, "dFecFinC", jsonobject );
        gGrupAdi.hijos_add( mapping, "dVencPag", jsonobject );
        gGrupAdi.hijos_add( mapping, "dContrato", jsonobject );
        gGrupAdi.hijos_add( mapping, "dSalAnt", jsonobject );


        //  E10. Campos que describen el transporte de las mercaderías (E900-E999)
        DeXmlElement gTransp = new DeXmlElement("gTransp");
        gDtipDE.getHijos().add(gTransp);


        gTransp.hijos_add( mapping, "iTipTrans", jsonobject );
        gTransp.hijos_add( mapping, "dDesTipTrans", jsonobject );
        gTransp.hijos_add( mapping, "iModTrans", jsonobject );
        gTransp.hijos_add( mapping, "dDesModTrans", jsonobject );
        gTransp.hijos_add( mapping, "iRespFlete", jsonobject );
        gTransp.hijos_add( mapping, "cCondNeg", jsonobject );
        gTransp.hijos_add( mapping, "dNuManif", jsonobject );
        gTransp.hijos_add( mapping, "dNuManif", jsonobject );
        gTransp.hijos_add( mapping, "dNuDespImp", jsonobject );
        gTransp.hijos_add( mapping, "dNuDespImp", jsonobject );
        gTransp.hijos_add( mapping, "dIniTras", jsonobject );
        gTransp.hijos_add( mapping, "dFinTras", jsonobject );
        gTransp.hijos_add( mapping, "cPaisDest", jsonobject );
        gTransp.hijos_add( mapping, "dDesPaisDest", jsonobject );


        // E10.1. Campos que identifican el local de salida de las mercaderías (E920-E939)
        DeXmlElement gCamSal = new DeXmlElement("gCamSal");
        gTransp.getHijos().add(gCamSal);

        gCamSal.hijos_add( mapping, "dDirLocSal", jsonobject );
        gCamSal.hijos_add( mapping, "dNumCasSal", jsonobject );
        gCamSal.hijos_add( mapping, "dComp1Sal", jsonobject );
        gCamSal.hijos_add( mapping, "dComp2Sal", jsonobject );
        gCamSal.hijos_add( mapping, "cDepSal", jsonobject );
        gCamSal.hijos_add( mapping, "dDesDepSal", jsonobject );
        gCamSal.hijos_add( mapping, "cDisSal", jsonobject );
        gCamSal.hijos_add( mapping, "dDesDisSal", jsonobject );
        gCamSal.hijos_add( mapping, "cCiuSal", jsonobject );
        gCamSal.hijos_add( mapping, "dDesCiuSal", jsonobject );
        gCamSal.hijos_add( mapping, "dTelSal", jsonobject );



        // E10.2. Campos que identifican el local de entrega de las mercaderías (E940-E959)
        DeXmlElement gCamEnt = new DeXmlElement("gCamEnt");
        gTransp.getHijos().add(gCamEnt);

        gCamEnt.hijos_add( mapping, "dDirLocEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "dNumCasEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "dComp1Ent", jsonobject );
        gCamEnt.hijos_add( mapping, "dComp2Ent", jsonobject );
        gCamEnt.hijos_add( mapping, "cDepEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "dDesDepEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "cDisEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "dDesDisEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "cCiuEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "dDesCiuEnt", jsonobject );
        gCamEnt.hijos_add( mapping, "dTelEnt", jsonobject );


        // E10.3. Campos que identifican el vehículo de traslado de mercaderías (E960-E979)
        DeXmlElement gVehTras = new DeXmlElement("gVehTras");
        gTransp.getHijos().add(gVehTras);

        gVehTras.hijos_add( mapping, "dTiVehTras", jsonobject );
        gVehTras.hijos_add( mapping, "dMarVeh", jsonobject );
        gVehTras.hijos_add( mapping, "dTipIdenVeh", jsonobject );
        gVehTras.hijos_add( mapping, "dNroIDVeh", jsonobject );
        gVehTras.hijos_add( mapping, "dAdicVeh", jsonobject );
        gVehTras.hijos_add( mapping, "dNroMatVeh", jsonobject );
        gVehTras.hijos_add( mapping, "dNroVuelo", jsonobject );

        // E10.4. Campos que identifican al transportista (persona física o jurídica) (E980-E999)
        DeXmlElement gCamTrans = new DeXmlElement("gCamTrans");
        gTransp.getHijos().add(gCamTrans);

        gCamTrans.hijos_add( mapping, "iNatTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dNomTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dRucTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dDVTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "iTipIDTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dDTipIDTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dNumIDTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "cNacTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dDesNacTrans", jsonobject );
        gCamTrans.hijos_add( mapping, "dNumIDChof", jsonobject );
        gCamTrans.hijos_add( mapping, "dNomChof", jsonobject );
        gCamTrans.hijos_add( mapping, "dDomFisc", jsonobject );
        gCamTrans.hijos_add( mapping, "dDirChof", jsonobject );
        gCamTrans.hijos_add( mapping, "dNombAg", jsonobject );
        gCamTrans.hijos_add( mapping, "dRucAg", jsonobject );
        gCamTrans.hijos_add( mapping, "dDVAg", jsonobject );
        gCamTrans.hijos_add( mapping, "dDirAge", jsonobject );



        DeXmlElement gTotSub = new DeXmlElement("gTotSub");
        DE.getHijos().add(gTotSub);

        gTotSub.getHijos().add(new DeXmlElement("dSubExe", mapping,  null ));
        gTotSub.hijos_add( mapping, "dSubExo", jsonobject );
        gTotSub.hijos_add( mapping, "dSub5", jsonobject );


        gTotSub.getHijos().add(new DeXmlElement("dSub10", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dTotOpe", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dTotDesc", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dTotDescGlotem", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dTotAntItem", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dTotAnt", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dPorcDescTotal", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dDescTotal", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dAnticipo", mapping,  null));

        gTotSub.getHijos().add(new DeXmlElement("dRedon", mapping,  null));

        gTotSub.hijos_add( mapping, "dComi", jsonobject );


        gTotSub.getHijos().add(new DeXmlElement("dTotGralOpe", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dIVA5", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dIVA10", mapping,  null));

        gTotSub.hijos_add( mapping, "dLiqTotIVA5", jsonobject );
        gTotSub.hijos_add( mapping, "dLiqTotIVA10", jsonobject );

        gTotSub.hijos_add( mapping, "dIVAComi", jsonobject );

        gTotSub.getHijos().add(new DeXmlElement("dTotIVA", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dBaseGrav5", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dBaseGrav10", mapping,  null));
        gTotSub.getHijos().add(new DeXmlElement("dTBasGraIVA", mapping,  null));

        gTotSub.hijos_add( mapping, "dTotalGs", jsonobject );

        DeXmlElement gCamDEAsoc = new DeXmlElement("gCamDEAsoc");
        DE.getHijos().add(gCamDEAsoc);

        gCamDEAsoc.hijos_add( mapping, "iTipDocAso", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dDesTipDocAso", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dCdCDERef", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dNTimDI", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dEstDocAso", jsonobject );

        gCamDEAsoc.hijos_add( mapping, "dPExpDocAso", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dNumDocAso", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "iTipoDocAso", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dDTipoDocAso", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dFecEmiDI", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dNumComRet", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dNumResCF", jsonobject );

        gCamDEAsoc.hijos_add( mapping, "iTipCons", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dDesTipCons", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dNumCons", jsonobject );
        gCamDEAsoc.hijos_add( mapping, "dNumControl", jsonobject );


        return rDE;
    }



}
