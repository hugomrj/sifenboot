package org.sifenboot.app.documento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estados_documento", schema = "public")
public class EstadoDocumento {

    @Id
    private Short id;

    @Column(nullable = false, unique = true, length = 25)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String descripcion;

    // ===== GETTERS & SETTERS =====

    public Short getId() { return id; }
    public void setId(Short id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}