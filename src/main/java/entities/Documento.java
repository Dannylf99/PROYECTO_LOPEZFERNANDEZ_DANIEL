package entities;

import java.sql.Date;

import enums.Estado;

public abstract class Documento {

    private int idDocumento;
    private int idUsuario;
    private String tipo;
    private Date fechaSubida;
    private Estado estado;
    private String rutaArchivo;

    // Constructor vacío opcional (útil si luego quieres usar JPA o frameworks)
    public Documento() {}

    public Documento(int idDocumento, int idUsuario, String tipo, Date fechaSubida, 
                     Estado estado, String rutaArchivo) {
        this.idDocumento = idDocumento;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.fechaSubida = fechaSubida;
        this.estado = estado;
        this.rutaArchivo = rutaArchivo;
    }

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

    public Date getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(Date fechaSubida) {
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

    // Métodos comunes
    public void validar() {
        this.estado = Estado.VALIDADO;
    }

    public void firmar() {
        this.estado = Estado.FIRMADO;
    }

    public void eliminar() {
        this.estado = Estado.ELIMINADO;
    }

    // Métodos abstractos si quieres que cada documento los implemente
    public abstract void procesarDocumento();
}
