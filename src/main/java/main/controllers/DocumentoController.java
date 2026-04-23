package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.repositories.PracticaRepository;
import main.roles.*;
import main.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/web/documentos")
public class DocumentoController {

    @Autowired private DocumentoService documentoService;
    @Autowired private PracticaRepository practicaRepository;

    // ── ALUMNO ───────────────────────────────────────────────────────────────

    @GetMapping("/alumno")
    public String vistaAlumno(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        AlumnoRol alumno = (AlumnoRol) usuario;
        model.addAttribute("usuario", alumno);
        model.addAttribute("documentos", documentoService.findByAlumno(alumno));
        return "alumno/documentosAlumno";
    }

    @PostMapping("/alumno/subir")
    public String subirDocumento(@RequestParam int idPractica,
                                 @RequestParam String tipo,
                                 @RequestParam MultipartFile archivo,
                                 HttpSession session) throws Exception {
        if (!(session.getAttribute("usuario") instanceof AlumnoRol))
            return "redirect:/web/login";
        AlumnoRol alumno = (AlumnoRol) session.getAttribute("usuario");
        DocumentoRol.Tipo tipoEnum = DocumentoRol.Tipo.valueOf(tipo);

        // Solo convenio y memoria pueden subirlos los alumnos
        if (!tipoEnum.equals(DocumentoRol.Tipo.CONVENIO) &&
                !tipoEnum.equals(DocumentoRol.Tipo.MEMORIA_FINAL)) {
            return "redirect:/web/documentos/alumno";
        }

        documentoService.subirDocumento(alumno, idPractica, tipoEnum, archivo);
        return "redirect:/web/documentos/alumno";
    }

    @PostMapping("/alumno/firmar/{id}")
    public String firmarAlumno(@PathVariable int id, HttpSession session) {
        if (!(session.getAttribute("usuario") instanceof AlumnoRol))
            return "redirect:/web/login";
        documentoService.firmarAlumno(id);
        return "redirect:/web/documentos/alumno";
    }

    @PostMapping("/alumno/resubir/{id}")
    public String resubirAlumno(@PathVariable int id,
                                @RequestParam MultipartFile archivo,
                                HttpSession session) throws Exception {
        if (!(session.getAttribute("usuario") instanceof AlumnoRol))
            return "redirect:/web/login";
        documentoService.resubirDocumento(id, archivo);
        return "redirect:/web/documentos/alumno";
    }

    // ── GESTOR ───────────────────────────────────────────────────────────────

    @GetMapping("/gestor")
    public String vistaGestor(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        GestorRol gestor = (GestorRol) usuario;

        model.addAttribute("usuario", gestor);
        model.addAttribute("pendientes",
                documentoService.findPendientesFirmaGestor(gestor.getIdEmpresa()));
        model.addAttribute("practicas",
                practicaRepository.findByEmpresaIdAndEstado(
                        gestor.getIdEmpresa(), PracticaRol.Estado.ACTIVA));
        model.addAttribute("todosDocumentos",
                documentoService.findByEmpresa(gestor.getIdEmpresa()));
        return "gestor/documentosGestor";
    }

    @PostMapping("/gestor/subir")
    public String subirGestor(@RequestParam int idPractica,
                              @RequestParam String tipo,
                              @RequestParam MultipartFile archivo,
                              HttpSession session) throws Exception {
        if (!(session.getAttribute("usuario") instanceof GestorRol))
            return "redirect:/web/login";
        DocumentoRol.Tipo tipoEnum = DocumentoRol.Tipo.valueOf(tipo);

        // El gestor solo puede subir informe de seguimiento y evaluación
        if (!tipoEnum.equals(DocumentoRol.Tipo.INFORME_SEGUIMIENTO) &&
                !tipoEnum.equals(DocumentoRol.Tipo.EVALUACION)) {
            return "redirect:/web/documentos/gestor";
        }

        PracticaRol practica = practicaRepository.findById(idPractica).orElseThrow();
        documentoService.subirDocumentoGestor(practica, tipoEnum, archivo);
        return "redirect:/web/documentos/gestor";
    }

    @PostMapping("/gestor/firmar/{id}")
    public String firmarGestor(@PathVariable int id, HttpSession session) {
        if (!(session.getAttribute("usuario") instanceof GestorRol))
            return "redirect:/web/login";
        documentoService.firmarGestor(id);
        return "redirect:/web/documentos/gestor";
    }

    // ── COORDINADOR ──────────────────────────────────────────────────────────

    @GetMapping("/coordinador")
    public String vistaCoordinador(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        CoordinadorRol coordinador = (CoordinadorRol) usuario;

        model.addAttribute("usuario", coordinador);
        model.addAttribute("documentos",
                documentoService.findByCoordinador(coordinador.getIdUsuario()));
        return "coordinador/documentosCoordinador";
    }

    // ── ADMINISTRACIÓN ───────────────────────────────────────────────────────

    @GetMapping("/admin")
    public String vistaAdmin(HttpSession session, Model model) {
        if (!(session.getAttribute("usuario") instanceof AdministracionRol))
            return "redirect:/web/login";
        AdministracionRol admin = (AdministracionRol) session.getAttribute("usuario");

        model.addAttribute("usuario", admin);
        model.addAttribute("pendientes",
                documentoService.findPendientesValidacion());
        model.addAttribute("practicas",
                practicaRepository.findAll());
        return "administracion/documentosAdmin";
    }

    @PostMapping("/admin/subir")
    public String subirAdmin(@RequestParam int idPractica,
                             @RequestParam String tipo,
                             @RequestParam MultipartFile archivo,
                             HttpSession session) throws Exception {
        if (!(session.getAttribute("usuario") instanceof AdministracionRol))
            return "redirect:/web/login";
        DocumentoRol.Tipo tipoEnum = DocumentoRol.Tipo.valueOf(tipo);
        PracticaRol practica = practicaRepository.findById(idPractica).orElseThrow();
        documentoService.subirDocumentoGestor(practica, tipoEnum, archivo);
        return "redirect:/web/documentos/admin";
    }

    @PostMapping("/admin/validar/{id}")
    public String validar(@PathVariable int id, HttpSession session) {
        if (!(session.getAttribute("usuario") instanceof AdministracionRol))
            return "redirect:/web/login";
        documentoService.validarDocumento(id);
        return "redirect:/web/documentos/admin";
    }

    @PostMapping("/admin/rechazar/{id}")
    public String rechazar(@PathVariable int id, HttpSession session) {
        if (!(session.getAttribute("usuario") instanceof AdministracionRol))
            return "redirect:/web/login";
        documentoService.rechazarDocumento(id);
        return "redirect:/web/documentos/admin";
    }

    // ── DESCARGA (todos los roles) ───────────────────────────────────────────

    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargar(@PathVariable int id,
                                            HttpSession session) throws Exception {
        if (session.getAttribute("usuario") == null)
            return ResponseEntity.status(403).build();

        byte[] bytes = documentoService.descargarDocumento(id);
        String nombreArchivo = documentoService.findById(id)
                .map(d -> d.getTipo().name().toLowerCase() + "_" +
                        d.getAlumno().getApellidos().toLowerCase() + ".pdf")
                .orElse("documento.pdf");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}