package main.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.repositories.AlumnoRepository;
import main.repositories.PracticaRepository;
import main.repositories.RegistroHorasRepository;
import main.roles.*;
import main.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/web/coordinador")
public class CoordinadorController {

    @Autowired private PracticaRepository practicaRepository;
    @Autowired private AlumnoRepository alumnoRepository;
    @Autowired private RegistroHorasRepository registroHorasRepository;
    @Autowired private DocumentoService documentoService;

    @GetMapping("/alumnos")
    public String listaAlumnos(HttpSession session, Model model,
                               HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        noCache(response);
        CoordinadorRol coordinador = (CoordinadorRol) usuario;

        List<PracticaRol> practicas = practicaRepository.findAll().stream()
                .filter(p -> p.getCoordinador().getIdUsuario() == coordinador.getIdUsuario())
                .toList();

        model.addAttribute("usuario", coordinador);
        model.addAttribute("practicas", practicas);
        return "coordinador/alumnosCoordinador";
    }

    @GetMapping("/alumnos/{idAlumno}")
    public String detalleAlumno(@PathVariable int idAlumno,
                                HttpSession session, Model model,
                                HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        noCache(response);
        CoordinadorRol coordinador = (CoordinadorRol) usuario;

        AlumnoRol alumno = alumnoRepository.findByIdUsuario(idAlumno).orElseThrow();

        List<PracticaRol> practicas = practicaRepository.findAll().stream()
                .filter(p -> p.getAlumno().getIdUsuario() == idAlumno
                        && p.getCoordinador().getIdUsuario() == coordinador.getIdUsuario())
                .toList();

        // Registros de horas de todas sus prácticas
        List<RegistroHorasRol> registros = practicas.stream()
                .flatMap(p -> registroHorasRepository.findByPractica(p).stream())
                .sorted((a, b) -> b.getFecha().compareTo(a.getFecha()))
                .toList();

        // Documentos del alumno
        List<DocumentoRol> documentos = documentoService.findByAlumno(alumno);

        model.addAttribute("usuario", coordinador);
        model.addAttribute("alumno", alumno);
        model.addAttribute("practicas", practicas);
        model.addAttribute("registros", registros);
        model.addAttribute("documentos", documentos);
        return "coordinador/detalleAlumno";
    }

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}