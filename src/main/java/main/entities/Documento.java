package main.entities;

import jakarta.persistence.*;
import main.enums.Estado;
import java.time.LocalDate;

@MappedSuperclass
public abstract class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDocumento;

    private int idUsuario; // O puedes hacer ManyToOne con Alumno o Usuario
    private String tipo;
    private LocalDate fechaSubida;
    private Estado estado;
    private String rutaArchivo;

    public Documento() {}

    public Documento(int idDocumento, int idUsuario, String tipo, LocalDate fechaSubida, Estado estado, String rutaArchivo) {
        this.idDocumento = idDocumento;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.fechaSubida = fechaSubida;
        this.estado = estado;
        this.rutaArchivo = rutaArchivo;
    }

    // Getters y Setters
    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Método abstracto que cada tipo de documento implementará
    public abstract void procesarDocumento();
}
