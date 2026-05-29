package org.sifenboot.app.admin.documento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estados_documento", schema = "public")
public class EstadoDocumento {

    public static final short RECIBIDO = 1;
    public static final short APROBADO = 2;
    public static final short RECHAZADO = 3;


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