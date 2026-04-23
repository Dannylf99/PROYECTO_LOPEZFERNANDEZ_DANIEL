package main.roles;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento")
public class DocumentoRol {

    public enum Estado {
        PENDIENTE_FIRMA_GESTOR,
        PENDIENTE_FIRMA_COORDINADOR,
        PENDIENTE_FIRMA_ALUMNO,
        PENDIENTE_VALIDACION,
        VALIDADO,
        RECHAZADO
    }

    public enum Tipo {
        CONVENIO,
        MEMORIA_FINAL,
        INFORME_SEGUIMIENTO,
        EVALUACION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private int idDocumento;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private AlumnoRol alumno;

    @ManyToOne
    @JoinColumn(name = "id_practica")
    private PracticaRol practica;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Column(name = "fecha_firma_gestor")
    private LocalDateTime fechaFirmaGestor;

    @Column(name = "fecha_firma_coordinador")
    private LocalDateTime fechaFirmaCoordinador;

    @Column(name = "fecha_firma_alumno")
    private LocalDateTime fechaFirmaAlumno;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    public DocumentoRol() {}

    // Getters y setters
    public int getIdDocumento() { return idDocumento; }
    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }

    public AlumnoRol getAlumno() { return alumno; }
    public void setAlumno(AlumnoRol alumno) { this.alumno = alumno; }

    public PracticaRol getPractica() { return practica; }
    public void setPractica(PracticaRol practica) { this.practica = practica; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public LocalDateTime getFechaFirmaGestor() { return fechaFirmaGestor; }
    public void setFechaFirmaGestor(LocalDateTime fechaFirmaGestor) { this.fechaFirmaGestor = fechaFirmaGestor; }

    public LocalDateTime getFechaFirmaCoordinador() { return fechaFirmaCoordinador; }
    public void setFechaFirmaCoordinador(LocalDateTime fechaFirmaCoordinador) { this.fechaFirmaCoordinador = fechaFirmaCoordinador; }

    public LocalDateTime getFechaFirmaAlumno() { return fechaFirmaAlumno; }
    public void setFechaFirmaAlumno(LocalDateTime fechaFirmaAlumno) { this.fechaFirmaAlumno = fechaFirmaAlumno; }

    public LocalDateTime getFechaValidacion() { return fechaValidacion; }
    public void setFechaValidacion(LocalDateTime fechaValidacion) { this.fechaValidacion = fechaValidacion; }

    // Helpers útiles para las vistas
    public boolean isConvenio() { return Tipo.CONVENIO.equals(this.tipo); }
    public boolean isValidado() { return Estado.VALIDADO.equals(this.estado); }
    public boolean isRechazado() { return Estado.RECHAZADO.equals(this.estado); }
}