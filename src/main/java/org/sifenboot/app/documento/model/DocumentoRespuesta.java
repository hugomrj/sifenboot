package org.sifenboot.app.documento.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_respuestas")
public class DocumentoRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con Documento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    @Column(name = "numero_lote")
    private String numeroLote;

    @Column(name = "tipo_operacion", nullable = false, length = 30)
    private String tipoOperacion; // ENVIO_LOTE, CONSULTA_LOTE

    @Column(name = "codigo_respuesta", length = 10)
    private String codigoRespuesta;

    @Column(name = "mensaje_respuesta")
    private String mensajeRespuesta;

    @Column(name = "xml_respuesta_cruda")
    private String xmlRespuestaCruda;

    @Column(name = "fecha_hora", insertable = false, updatable = false)
    private LocalDateTime fechaHora;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public Documento getDocumento() { return documento; }
    public void setDocumento(Documento documento) { this.documento = documento; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    public String getCodigoRespuesta() { return codigoRespuesta; }
    public void setCodigoRespuesta(String codigoRespuesta) { this.codigoRespuesta = codigoRespuesta; }

    public String getMensajeRespuesta() { return mensajeRespuesta; }
    public void setMensajeRespuesta(String mensajeRespuesta) { this.mensajeRespuesta = mensajeRespuesta; }

    public String getXmlRespuestaCruda() { return xmlRespuestaCruda; }
    public void setXmlRespuestaCruda(String xmlRespuestaCruda) { this.xmlRespuestaCruda = xmlRespuestaCruda; }

    public LocalDateTime getFechaHora() { return fechaHora; }
}