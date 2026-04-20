package org.sifenboot.shell.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emisores")
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

    @Column(nullable = false)
    private String ambiente = "test";

    @Column(name = "id_csc", nullable = false)
    private String idCsc;

    @Column(nullable = false)
    private String csc;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    public Emisor() {}

    // Getters y Setters
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
    public void setTipoContribuyente(Integer tipo) { this.tipoContribuyente = tipo; }
    public String getNumeroTimbrado() { return numeroTimbrado; }
    public void setNumeroTimbrado(String num) { this.numeroTimbrado = num; }
    public LocalDate getFechaInicioTimbrado() { return fechaInicioTimbrado; }
    public void setFechaInicioTimbrado(LocalDate fecha) { this.fechaInicioTimbrado = fecha; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String dir) { this.direccion = dir; }
    public Integer getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(Integer num) { this.numeroCasa = num; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento dep) { this.departamento = dep; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String tel) { this.telefono = tel; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getActividadEconomicaCodigo() { return actividadEconomicaCodigo; }
    public void setActividadEconomicaCodigo(Integer cod) { this.actividadEconomicaCodigo = cod; }
    public String getActividadEconomicaDescripcion() { return actividadEconomicaDescripcion; }
    public void setActividadEconomicaDescripcion(String desc) { this.actividadEconomicaDescripcion = desc; }
    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String amb) { this.ambiente = amb; }
    public String getIdCsc() { return idCsc; }
    public void setIdCsc(String id) { this.idCsc = id; }
    public String getCsc() { return csc; }
    public void setCsc(String csc) { this.csc = csc; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}