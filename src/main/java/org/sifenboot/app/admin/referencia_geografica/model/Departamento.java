package org.sifenboot.app.admin.referencia_geografica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departamentos", schema = "public")
public class Departamento {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    // Constructor vacío obligatorio para JPA
    public Departamento() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}