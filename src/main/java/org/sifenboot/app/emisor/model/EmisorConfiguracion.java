package org.sifenboot.app.emisor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "emisores_configuraciones", schema = "public")
public class EmisorConfiguracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "emisor_id", nullable = false)
    private Emisor emisor;

    @Column(nullable = false)
    private String ambiente = "test";

    @Column(name = "id_csc", nullable = false)
    private String idCsc;

    @Column(nullable = false)
    private String csc;

    @Column(name = "api_token")
    private String apiToken;


    public EmisorConfiguracion() {}

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Emisor getEmisor() { return emisor; }
    public void setEmisor(Emisor emisor) { this.emisor = emisor; }

    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }

    public String getIdCsc() { return idCsc; }
    public void setIdCsc(String idCsc) { this.idCsc = idCsc; }

    public String getCsc() { return csc; }
    public void setCsc(String csc) { this.csc = csc; }

    public String getApiToken() { return apiToken; }
    public void setApiToken(String apiToken) { this.apiToken = apiToken; }


}