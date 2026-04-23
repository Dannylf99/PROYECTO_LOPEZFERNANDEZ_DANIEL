package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.repositories.AlumnoRepository;
import java.util.Comparator;
import java.util.stream.Collectors;

import main.roles.AlumnoRol;
import main.services.InformeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/informes")
public class InformeController {

    @Autowired private InformeService informeService;
    @Autowired private AlumnoRepository alumnoRepository;

    private boolean tieneAcceso(HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        return rol != null && (rol.equals("COORDINADOR") || rol.equals("ADMINISTRACION"));
    }

    @GetMapping("/seleccion")
    public String seleccion(HttpSession session, Model model) {
        if (!tieneAcceso(session)) return "redirect:/web/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("rol", session.getAttribute("rol"));
        model.addAttribute("alumnos", alumnoRepository.findAll().stream()
                .sorted(Comparator.comparing(AlumnoRol::getApellidos)
                        .thenComparing(AlumnoRol::getNombre))
                .collect(Collectors.toList()));
        return "informes/seleccionInformes";
    }

    @GetMapping("/alumnos")
    public ResponseEntity<byte[]> informeAlumnos(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            HttpSession session) throws Exception {

        if (!tieneAcceso(session)) return ResponseEntity.status(403).build();

        byte[] pdf = informeService.generarInformeAlumnos(curso, estado, fechaDesde, fechaHasta);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe_alumnos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/horas")
    public ResponseEntity<byte[]> informeHoras(
            @RequestParam int idAlumno,
            @RequestParam(required = false) String estadoHoras,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            HttpSession session) throws Exception {

        if (!tieneAcceso(session)) return ResponseEntity.status(403).build();

        byte[] pdf = informeService.generarInformeHoras(idAlumno, estadoHoras, fechaDesde, fechaHasta);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe_horas_" + idAlumno + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/general")
    public ResponseEntity<byte[]> informeGeneral(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            HttpSession session) throws Exception {

        if (!tieneAcceso(session)) return ResponseEntity.status(403).build();

        byte[] pdf = informeService.generarInformeGeneral(curso, estado, fechaDesde, fechaHasta);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe_general.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}