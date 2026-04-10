package org.sifenboot.shell.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "emisor_id", nullable = false, unique = true)
    private Emisor emisor;


    @Lob // Indica que es un objeto grande
    @JdbcType(VarbinaryJdbcType.class) // Fuerza a que sea tratado como bytea de Postgres
    @Column(name = "p12_contenido")
    private byte[] p12Contenido;


    @Column(name = "p12_password", nullable = false)
    private String p12Password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_vencimiento_p12")
    private LocalDate fechaVencimientoP12;

    private Boolean activo = true;

    public Certificado() {}

    // Getters y Setters Simplificados
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Emisor getEmisor() { return emisor; }
    public void setEmisor(Emisor emisor) { this.emisor = emisor; }

    public byte[] getP12Contenido() { return p12Contenido; }
    public void setP12Contenido(byte[] p12Contenido) { this.p12Contenido = p12Contenido; }

    public String getP12Password() { return p12Password; }
    public void setP12Password(String p12Password) { this.p12Password = p12Password; }

    public LocalDate getFechaVencimientoP12() { return fechaVencimientoP12; }
    public void setFechaVencimientoP12(LocalDate fecha) { this.fechaVencimientoP12 = fecha; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}