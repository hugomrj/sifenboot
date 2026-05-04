package org.sifenboot.app.documento.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cdc;

    @Column(name = "tipo_documento", nullable = false)
    private Integer tipoDocumento;

    @Column(nullable = false, length = 3)
    private String establecimiento;

    @Column(name = "punto_expedicion", nullable = false, length = 3)
    private String puntoExpedicion;

    @Column(name = "numero_documento", nullable = false, length = 15)
    private String numeroDocumento;

    // 🔥 RELACIÓN (reemplaza estadoId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private EstadoDocumento estado;

    @Column(name = "numero_lote")
    private String numeroLote;

    @Column(name = "monto_total", precision = 15, scale = 2)
    private BigDecimal montoTotal;


    // Nuevos campos
    @Column(name = "nombre_receptor")
    private String nombreReceptor;

    @Column(name = "ruc_receptor")
    private String rucReceptor;


    @Column(name = "xml_enviado")
    private String xmlEnviado;

    @Column(name = "xml_respuesta")
    private String xmlRespuesta;

    @Column(name = "json_data", columnDefinition = "jsonb")
    private String jsonData;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    // 🔥 RELACIÓN 1:N (auditoría)
    @OneToMany(mappedBy = "documento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DocumentoRespuesta> respuestas;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCdc() { return cdc; }
    public void setCdc(String cdc) { this.cdc = cdc; }

    public Integer getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(Integer tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getEstablecimiento() { return establecimiento; }
    public void setEstablecimiento(String establecimiento) { this.establecimiento = establecimiento; }

    public String getPuntoExpedicion() { return puntoExpedicion; }
    public void setPuntoExpedicion(String puntoExpedicion) { this.puntoExpedicion = puntoExpedicion; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public EstadoDocumento getEstado() { return estado; }
    public void setEstado(EstadoDocumento estado) { this.estado = estado; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getXmlEnviado() { return xmlEnviado; }
    public void setXmlEnviado(String xmlEnviado) { this.xmlEnviado = xmlEnviado; }

    public String getXmlRespuesta() { return xmlRespuesta; }
    public void setXmlRespuesta(String xmlRespuesta) { this.xmlRespuesta = xmlRespuesta; }

    public String getJsonData() { return jsonData; }
    public void setJsonData(String jsonData) { this.jsonData = jsonData; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public List<DocumentoRespuesta> getRespuestas() { return respuestas; }
    public void setRespuestas(List<DocumentoRespuesta> respuestas) { this.respuestas = respuestas; }


    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }

    public String getRucReceptor() { return rucReceptor; }
    public void setRucReceptor(String rucReceptor) { this.rucReceptor = rucReceptor; }


}