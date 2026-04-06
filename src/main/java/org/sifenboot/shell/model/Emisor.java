package org.sifenboot.shell.model;

import jakarta.persistence.*;
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

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "nombre_fantasia")
    private String nombreFantasia;

    @Column(name = "tipo_contribuyente", nullable = false)
    private Integer tipoContribuyente;

    @Column(name = "numero_timbrado", nullable = false)
    private String numeroTimbrado;

    @Column(name = "inicio_vigencia_timbrado", nullable = false)
    private LocalDate inicioVigenciaTimbrado;

    @Column(nullable = false)
    private String direccion;

    private String telefono;
    private String email;

    @Column(name = "actividad_economica_codigo")
    private Integer actividadEconomicaCodigo;

    @Column(name = "actividad_economica_descripcion")
    private String actividadEconomicaDescripcion;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    // Constructor vacío requerido por JPA
    public Emisor() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodEmisor() { return codEmisor; }
    public void setCodEmisor(String codEmisor) { this.codEmisor = codEmisor; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getNombreFantasia() { return nombreFantasia; }
    public void setNombreFantasia(String nombreFantasia) { this.nombreFantasia = nombreFantasia; }

    public Integer getTipoContribuyente() { return tipoContribuyente; }
    public void setTipoContribuyente(Integer tipoContribuyente) { this.tipoContribuyente = tipoContribuyente; }

    public String getNumeroTimbrado() { return numeroTimbrado; }
    public void setNumeroTimbrado(String numeroTimbrado) { this.numeroTimbrado = numeroTimbrado; }

    public LocalDate getInicioVigenciaTimbrado() { return inicioVigenciaTimbrado; }
    public void setInicioVigenciaTimbrado(LocalDate inicioVigenciaTimbrado) { this.inicioVigenciaTimbrado = inicioVigenciaTimbrado; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getActividadEconomicaCodigo() { return actividadEconomicaCodigo; }
    public void setActividadEconomicaCodigo(Integer codigo) { this.actividadEconomicaCodigo = codigo; }

    public String getActividadEconomicaDescripcion() { return actividadEconomicaDescripcion; }
    public void setActividadEconomicaDescripcion(String desc) { this.actividadEconomicaDescripcion = desc; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
}