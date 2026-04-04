package org.sifenboot.core.integration.de.structure;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.core.integration.de.Mapping;

import java.util.ArrayList;

public class DeXmlElement {

    private String nombre;
    private ArrayList<DeXmlElement> hijos = new ArrayList<DeXmlElement>();
    private ArrayList<XmlAttribute> atributos = new ArrayList <XmlAttribute>() ;
    private String valor ;

    public DeXmlElement (String nombre){
        this.nombre = nombre;
    }

    public DeXmlElement (String nombre, String valor){
        this.nombre = nombre;
        this.valor = valor;
    }

    public DeXmlElement (String nombre,  Mapping mapping,
                         String valor ){
        this.nombre = nombre;

        if (valor == null ){
            valor = mapping.getValorJson(nombre);
            //valor = valor.replaceAll("\""  , "");
        }
        this.valor = valor;
    }

    public DeXmlElement(String nombre, JsonNode jsonObject) {
        this.nombre = nombre;
        String valor = "";

        if (jsonObject != null && jsonObject.has(nombre)) {
            JsonNode node = jsonObject.get(nombre);
            if (!node.isNull()) {
                valor = node.asText("");
            }
        }
        this.valor = valor;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<DeXmlElement> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<DeXmlElement> hijos) {
        this.hijos = hijos;
    }

    public void recorrer() {
        this.recorrer(this, 0);
    }

    public void recorrer(DeXmlElement elemento,  Integer nivel) {

        String str = " ";
        if (elemento.getHijos().size() == 0 ){
            return ;
        }
        else{
            for ( int i=0; i < elemento.getHijos().size() ; i++) {
                this.recorrer(elemento.getHijos().get(i), nivel+1);
            }
        }
    }


    public ArrayList<XmlAttribute> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<XmlAttribute> atributos) {
        this.atributos = atributos;
    }

    public String strAtributos(){
        String ret = "";

        if (this.getAtributos().size() > 0){

            for ( int i=0; i < this.getAtributos().size() ; i++) {

                ret = ret +" " + this.getAtributos().get(i).getNombre() + "="
                    + "\""+this.getAtributos().get(i).getValor()+"\"";
            }
        }
        return ret;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


    public void hijos_add(Mapping mapping, String etiqueta,
                          JsonNode jsonobject) {

        if (jsonobject != null && jsonobject.has(etiqueta)) {
            this.getHijos().add(new DeXmlElement(etiqueta, mapping, null));
        }
    }


    public void hijos_add(String etiqueta, JsonNode jsonObject) {

        if (jsonObject != null && jsonObject.has(etiqueta)) {
            this.getHijos().add(new DeXmlElement(etiqueta, jsonObject));
        }
    }


}