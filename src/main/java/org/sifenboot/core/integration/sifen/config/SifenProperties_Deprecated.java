package org.sifenboot.core.integration.sifen.config;

import org.springframework.stereotype.Component;

/**
 * @deprecated Esta clase ya no debe usarse para configuraciones globales.
 * La configuración ahora es dinámica y proviene de la tabla 'certificados'.
 * Se mantiene temporalmente para evitar errores de compilación durante la migración.
 */
@Deprecated
@Component
// 1. Eliminamos @ConfigurationProperties para que NO lea el application.yml ni el .env
public class SifenProperties_Deprecated {

    private String ambiente;
    private String certPath;
    private String certPass;
    private String idCsc;
    private String csc;

    // Dejamos un timeout por defecto para no romper sockets
    private int timeout = 30000;

    private String urlConsultaQr;
    private String effectiveCsc;

    /**
     * 2. Constructor vacío.
     * Se eliminó el @PostConstruct para que la App encienda aunque no haya datos.
     */
    public SifenProperties_Deprecated() {
    }

    // --- GETTERS Y SETTERS MANUALES ---
    // Se mantienen para que el resto del código siga compilando

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

    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }

    public String getUrlConsultaQr() { return urlConsultaQr; }
    public void setUrlConsultaQr(String urlConsultaQr) { this.urlConsultaQr = urlConsultaQr; }

    public String getEffectiveCsc() { return effectiveCsc; }
    public void setEffectiveCsc(String effectiveCsc) { this.effectiveCsc = effectiveCsc; }
}