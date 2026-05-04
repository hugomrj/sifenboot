package org.sifenboot.app.emisor.model;

import jakarta.persistence.*;
import org.sifenboot.app.departamento.model.Departamento;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emisores", schema = "public")
public class Emisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_emisor", unique = true, nullable = false)
    private String codEmisor;

    @Column(unique = true, nullable = false)
    private String ruc;

    @Column(name = "ruc_dv", nullable = false)
    private Integer rucDv;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "nombre_fantasia")
    private String nombreFantasia;

    @Column(name = "tipo_contribuyente", nullable = false)
    private Integer tipoContribuyente;

    @Column(name = "numero_timbrado", nullable = false)
    private String numeroTimbrado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_inicio_timbrado", nullable = false)
    private LocalDate fechaInicioTimbrado;

    @Column(nullable = false)
    private String direccion;

    @Column(name = "numero_casa")
    private Integer numeroCasa = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    private String telefono;
    private String email;

    @Column(name = "actividad_economica_codigo")
    private Integer actividadEconomicaCodigo;

    @Column(name = "actividad_economica_descripcion")
    private String actividadEconomicaDescripcion;

    // Relación con la nueva tabla de configuración
    @OneToOne(mappedBy = "emisor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmisorConfiguracion configuracion;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    public Emisor() {}

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodEmisor() { return codEmisor; }
    public void setCodEmisor(String codEmisor) { this.codEmisor = codEmisor; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public Integer getRucDv() { return rucDv; }
    public void setRucDv(Integer rucDv) { this.rucDv = rucDv; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getNombreFantasia() { return nombreFantasia; }
    public void setNombreFantasia(String nombreFantasia) { this.nombreFantasia = nombreFantasia; }

    public Integer getTipoContribuyente() { return tipoContribuyente; }
    public void setTipoContribuyente(Integer tipoContribuyente) { this.tipoContribuyente = tipoContribuyente; }

    public String getNumeroTimbrado() { return numeroTimbrado; }
    public void setNumeroTimbrado(String numeroTimbrado) { this.numeroTimbrado = numeroTimbrado; }

    public LocalDate getFechaInicioTimbrado() { return fechaInicioTimbrado; }
    public void setFechaInicioTimbrado(LocalDate fechaInicioTimbrado) { this.fechaInicioTimbrado = fechaInicioTimbrado; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Integer getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(Integer numeroCasa) { this.numeroCasa = numeroCasa; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getActividadEconomicaCodigo() { return actividadEconomicaCodigo; }
    public void setActividadEconomicaCodigo(Integer actividadEconomicaCodigo) { this.actividadEconomicaCodigo = actividadEconomicaCodigo; }

    public String getActividadEconomicaDescripcion() { return actividadEconomicaDescripcion; }
    public void setActividadEconomicaDescripcion(String actividadEconomicaDescripcion) { this.actividadEconomicaDescripcion = actividadEconomicaDescripcion; }

    public EmisorConfiguracion getConfiguracion() { return configuracion; }
    public void setConfiguracion(EmisorConfiguracion configuracion) { this.configuracion = configuracion; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
}