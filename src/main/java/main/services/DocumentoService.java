package main.services;

import main.repositories.DocumentoRepository;
import main.repositories.PracticaRepository;
import main.roles.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private static final String UPLOAD_DIR = "uploads/documentos/";

    @Autowired private DocumentoRepository documentoRepository;
    @Autowired private PracticaRepository practicaRepository;
    @Autowired private NotificacionService notificacionService;

    // ── Subir documento ──────────────────────────────────────────────────────
    public DocumentoRol subirDocumento(AlumnoRol alumno, int idPractica,
                                       DocumentoRol.Tipo tipo,
                                       MultipartFile archivo) throws IOException {

        PracticaRol practica = practicaRepository.findById(idPractica).orElseThrow();

        // Guardar archivo en disco
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String nombreArchivo = "doc_" + alumno.getIdUsuario() + "_"
                + tipo.name() + "_" + System.currentTimeMillis() + ".pdf";
        Path ruta = Paths.get(UPLOAD_DIR + nombreArchivo);
        Files.write(ruta, archivo.getBytes());

        // Crear entidad
        DocumentoRol doc = new DocumentoRol();
        doc.setAlumno(alumno);
        doc.setPractica(practica);
        doc.setTipo(tipo);
        doc.setFechaSubida(LocalDateTime.now());
        doc.setRutaArchivo(nombreArchivo);

        // El convenio empieza en firma gestor, el resto va directo a validación
        if (DocumentoRol.Tipo.CONVENIO.equals(tipo)) {
            doc.setEstado(DocumentoRol.Estado.PENDIENTE_FIRMA_GESTOR);
        } else {
            doc.setEstado(DocumentoRol.Estado.PENDIENTE_VALIDACION);
        }

        documentoRepository.save(doc);

        // Notificación al alumno confirmando la subida
        notificacionService.crearNotificacion(alumno,
                "Has subido correctamente el documento: " + tipo.name().replace("_", " "));

        return doc;
    }

    // ── Firmar (gestor) ──────────────────────────────────────────────────────
    public void firmarGestor(int idDocumento) {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();
        doc.setFechaFirmaGestor(LocalDateTime.now());
        doc.setEstado(DocumentoRol.Estado.PENDIENTE_FIRMA_COORDINADOR);
        documentoRepository.save(doc);

        notificacionService.crearNotificacion(doc.getAlumno(),
                "El gestor de tu empresa ha firmado tu convenio. " +
                        "Ahora está pendiente de firma del coordinador.");
    }

    // ── Firmar (coordinador) ─────────────────────────────────────────────────
    public void firmarCoordinador(int idDocumento) {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();
        doc.setFechaFirmaCoordinador(LocalDateTime.now());
        doc.setEstado(DocumentoRol.Estado.PENDIENTE_FIRMA_ALUMNO);
        documentoRepository.save(doc);

        notificacionService.crearNotificacion(doc.getAlumno(),
                "Tu coordinador ha firmado tu convenio. " +
                        "Ahora necesitas firmarlo tú para enviarlo a validación.");
    }

    // ── Firmar (alumno) ──────────────────────────────────────────────────────
    public void firmarAlumno(int idDocumento) {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();
        doc.setFechaFirmaAlumno(LocalDateTime.now());
        doc.setEstado(DocumentoRol.Estado.PENDIENTE_VALIDACION);
        documentoRepository.save(doc);

        notificacionService.crearNotificacion(doc.getAlumno(),
                "Has firmado tu convenio. Está pendiente de validación por la administración.");
    }

    // ── Validar (administración) ─────────────────────────────────────────────
    public void validarDocumento(int idDocumento) {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();
        doc.setEstado(DocumentoRol.Estado.VALIDADO);
        doc.setFechaValidacion(LocalDateTime.now());
        documentoRepository.save(doc);

        notificacionService.crearNotificacion(doc.getAlumno(),
                "Tu documento " + doc.getTipo().name().replace("_", " ") +
                        " ha sido validado por la administración.");
    }

    // ── Rechazar (administración) ────────────────────────────────────────────
    public void rechazarDocumento(int idDocumento) {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();
        doc.setEstado(DocumentoRol.Estado.RECHAZADO);
        documentoRepository.save(doc);

        notificacionService.crearNotificacion(doc.getAlumno(),
                "Tu documento " + doc.getTipo().name().replace("_", " ") +
                        " ha sido rechazado. Contacta con tu coordinador.");
    }

    // ── Resubir documento ────────────────────────────────────────────────────
    public void resubirDocumento(int idDocumento, MultipartFile archivo) throws IOException {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();

        // Borrar archivo anterior si existe
        Path rutaAnterior = Paths.get(UPLOAD_DIR + doc.getRutaArchivo());
        Files.deleteIfExists(rutaAnterior);

        // Guardar nuevo archivo
        String nombreArchivo = "doc_" + doc.getAlumno().getIdUsuario() + "_"
                + doc.getTipo().name() + "_" + System.currentTimeMillis() + ".pdf";
        Path nuevaRuta = Paths.get(UPLOAD_DIR + nombreArchivo);
        Files.write(nuevaRuta, archivo.getBytes());

        doc.setRutaArchivo(nombreArchivo);
        doc.setFechaSubida(LocalDateTime.now());

        // Reiniciar el flujo si es convenio
        if (doc.isConvenio()) {
            doc.setEstado(DocumentoRol.Estado.PENDIENTE_FIRMA_GESTOR);
            doc.setFechaFirmaGestor(null);
            doc.setFechaFirmaCoordinador(null);
            doc.setFechaFirmaAlumno(null);
        } else {
            doc.setEstado(DocumentoRol.Estado.PENDIENTE_VALIDACION);
        }

        documentoRepository.save(doc);
    }

    // ── Descargar ────────────────────────────────────────────────────────────
    public byte[] descargarDocumento(int idDocumento) throws IOException {
        DocumentoRol doc = documentoRepository.findById(idDocumento).orElseThrow();
        Path ruta = Paths.get(UPLOAD_DIR + doc.getRutaArchivo());
        return Files.readAllBytes(ruta);
    }

    public Optional<DocumentoRol> findById(int id) {
        return documentoRepository.findById(id);
    }

    public List<DocumentoRol> findByAlumno(AlumnoRol alumno) {
        return documentoRepository.findByAlumno(alumno);
    }

    public List<DocumentoRol> findPendientesValidacion() {
        return documentoRepository.findByEstado(DocumentoRol.Estado.PENDIENTE_VALIDACION);
    }

    public List<DocumentoRol> findPendientesFirmaGestor(int idEmpresa) {
        return documentoRepository.findPendientesFirmaGestor(idEmpresa);
    }

    public List<DocumentoRol> findPendientesFirmaCoordinador(int idCoordinador) {
        return documentoRepository.findPendientesFirmaCoordinador(idCoordinador);
    }

    public boolean puedeSubir(String rol, DocumentoRol.Tipo tipo) {
        return switch (tipo) {
            case CONVENIO, MEMORIA_FINAL -> rol.equals("ALUMNO");
            case INFORME_SEGUIMIENTO, EVALUACION -> rol.equals("GESTOR") || rol.equals("ADMINISTRACION");
        };
    }

    public DocumentoRol subirDocumentoGestor(PracticaRol practica,
                                             DocumentoRol.Tipo tipo,
                                             MultipartFile archivo) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String nombreArchivo = "doc_" + practica.getIdPractica() + "_"
                + tipo.name() + "_" + System.currentTimeMillis() + ".pdf";
        Path ruta = Paths.get(UPLOAD_DIR + nombreArchivo);
        Files.write(ruta, archivo.getBytes());

        DocumentoRol doc = new DocumentoRol();
        doc.setAlumno(practica.getAlumno());
        doc.setPractica(practica);
        doc.setTipo(tipo);
        doc.setFechaSubida(LocalDateTime.now());
        doc.setRutaArchivo(nombreArchivo);
        doc.setEstado(DocumentoRol.Estado.PENDIENTE_VALIDACION);

        documentoRepository.save(doc);


        // Notificar al alumno
        notificacionService.crearNotificacion(practica.getAlumno(),
                "Se ha subido un nuevo documento de tipo " +
                        tipo.name().replace("_", " ") + " a tu expediente.");

        return doc;
    }

    public List<DocumentoRol> findByCoordinador(int idCoordinador) {
        return documentoRepository.findByCoordinadorId(idCoordinador);
    }

    public List<DocumentoRol> findByEmpresa(int idEmpresa) {
        return documentoRepository.findByEmpresaId(idEmpresa);
    }

    public List<DocumentoRol> buscarConFiltros(String tipo, String estado,
                                               String idAlumno, String idEmpresa) {
        DocumentoRol.Tipo   tipoEnum   = (tipo     != null && !tipo.isEmpty())
                ? DocumentoRol.Tipo.valueOf(tipo)       : null;
        DocumentoRol.Estado estadoEnum = (estado   != null && !estado.isEmpty())
                ? DocumentoRol.Estado.valueOf(estado)   : null;
        Integer alumnoId   = (idAlumno  != null && !idAlumno.isEmpty())
                ? Integer.parseInt(idAlumno)  : null;
        Integer empresaId  = (idEmpresa != null && !idEmpresa.isEmpty())
                ? Integer.parseInt(idEmpresa) : null;

        return documentoRepository.findWithFiltros(tipoEnum, estadoEnum, alumnoId, empresaId);
    }
}