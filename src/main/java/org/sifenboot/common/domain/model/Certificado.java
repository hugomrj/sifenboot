package org.sifenboot.common.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emisor_id", nullable = false, unique = true)
    private Integer emisorId;

    @Lob
    @Column(name = "p12_contenido", nullable = false)
    private byte[] p12Contenido;

    @Column(name = "p12_password", nullable = false)
    private String p12Password;

    @Column(nullable = false)
    private String ambiente;

    @Column(name = "id_csc", nullable = false)
    private String idCsc;

    @Column(nullable = false)
    private String csc;

    // Getters y Setters Manuales
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getEmisorId() { return emisorId; }
    public void setEmisorId(Integer emisorId) { this.emisorId = emisorId; }

    public byte[] getP12Contenido() { return p12Contenido; }
    public void setP12Contenido(byte[] p12Contenido) { this.p12Contenido = p12Contenido; }

    public String getP12Password() { return p12Password; }
    public void setP12Password(String p12Password) { this.p12Password = p12Password; }

    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }

    public String getIdCsc() { return idCsc; }
    public void setIdCsc(String idCsc) { this.idCsc = idCsc; }

    public String getCsc() { return csc; }
    public void setCsc(String csc) { this.csc = csc; }
}