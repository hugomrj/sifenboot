package org.sifenboot.app.admin.referencia_geografica.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "localidades", schema = "public")
public class Localidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "distrito_id", nullable = false)
    private Distrito distrito;

    @Column(name = "codigo_localidad", nullable = false)
    private Integer codigoLocalidad;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    public Localidad() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Distrito getDistrito() { return distrito; }
    public void setDistrito(Distrito distrito) { this.distrito = distrito; }

    public Integer getCodigoLocalidad() { return codigoLocalidad; }
    public void setCodigoLocalidad(Integer codigoLocalidad) { this.codigoLocalidad = codigoLocalidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}