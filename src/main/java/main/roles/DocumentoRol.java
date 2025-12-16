package main.roles;

import jakarta.persistence.*;
import main.enums.Estado;
import java.time.LocalDate;

@Entity
@Table(name = "documento")
public class DocumentoRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private int idDocumento;

    @Column(name = "id_usuario")
    private int idUsuario;

    private String tipo;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    public DocumentoRol() {}

    public DocumentoRol(int idDocumento, int idUsuario, String tipo, LocalDate fechaSubida, Estado estado, String rutaArchivo) {
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

    // Método específico
    public void procesarDocumento() {
        System.out.println("Procesando documento: " + tipo);
    }
}