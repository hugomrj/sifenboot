package org.sifenboot.core.integration.sifen.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "sifen")
public class SifenProperties {

    private String ambiente;
    private String certPath;
    private String certPass;
    private String idCsc;
    private String csc;
    private int timeout; 

    private String urlConsultaQr;
    private String effectiveCsc;

    @PostConstruct
    public void init() {
        if ("prod".equalsIgnoreCase(ambiente)) {
            this.urlConsultaQr = "https://ekuatia.set.gov.py/consultas/qr?";
            this.effectiveCsc = this.csc;
        } else if ("test".equalsIgnoreCase(ambiente)) {
            this.urlConsultaQr = "https://ekuatia.set.gov.py/consultas-test/qr?";
            this.effectiveCsc = "ABCD0000000000000000000000000000";
        } else {
            throw new IllegalStateException("Ambiente inválido: " + ambiente);
        }

        System.out.println("========================================");
        System.out.println("        CONFIGURACIÓN CERTIFICADO.P12");
        System.out.println("Ambiente           : " + ambiente);
        System.out.println("Certificado (.p12) : " + certPath);
        System.out.println("========================================");




    }

    // Getters y setters
    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }

    public String getCertPath() { return certPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }

    public String getCertPass() { return certPass; }
    public void setCertPass(String certPass) { this.certPass = certPass; }

    public String getIdCsc() { return idCsc; }
    public void setIdCsc(String idCsc) { this.idCsc = idCsc; }

    public String getCsc() { return csc; }
    public void setCsc(String csc) { this.csc = csc; }

    public int getTimeout() { return timeout; }  // Getter para timeout
    public void setTimeout(int timeout) { this.timeout = timeout; } // Setter para timeout

    public String getUrlConsultaQr() { return urlConsultaQr; }
    public void setUrlConsultaQr(String urlConsultaQr) { this.urlConsultaQr = urlConsultaQr; }

    public String getEffectiveCsc() { return effectiveCsc; }
    public void setEffectiveCsc(String effectiveCsc) { this.effectiveCsc = effectiveCsc; }



}
