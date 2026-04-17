package main.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.repositories.PracticaRepository;
import main.roles.*;
import main.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistroHorasController {

    @Autowired private RegistroHorasService registroService;
    @Autowired private PracticaService practicaService;
    @Autowired private PracticaRepository practicaRepository;
    @Autowired private NotificacionService notificacionService;

    @GetMapping("/web/alumno/horas")
    public String mostrarFormulario(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        AlumnoRol alumno = (AlumnoRol) usuario;

        List<PracticaRol> todasPracticas = practicaService.getPracticasByAlumno(alumno);

        // Verificar si alguna práctica está parada
        boolean practicaParada = todasPracticas.stream()
                .anyMatch(p -> p.getEstado() == PracticaRol.Estado.PARADA);

        List<PracticaRol> practicasActivas = todasPracticas.stream()
                .filter(p -> p.getEstado() == PracticaRol.Estado.ACTIVA)
                .toList();

        List<RegistroHorasRol> registros = new ArrayList<>();
        for (PracticaRol p : todasPracticas) {
            registros.addAll(registroService.getByPractica(p));
        }
        registros.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));

        model.addAttribute("usuario", alumno);
        model.addAttribute("practicas", practicasActivas);
        model.addAttribute("practicaParada", practicaParada);
        model.addAttribute("registros", registros);
        model.addAttribute("notificacionesNoLeidas", notificacionService.countNoLeidas(alumno));
        return "alumno/registrarHoras";
    }

    @PostMapping("/web/alumno/horas/registrar")
    public String registrar(@RequestParam int idPractica,
                            @RequestParam String fecha,
                            @RequestParam String horaInicio,
                            @RequestParam String horaFin,
                            @RequestParam(required = false) String pausaInicio,
                            @RequestParam(required = false) String pausaFin,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";

        try {
            PracticaRol practica = practicaRepository.findById(idPractica).orElseThrow();

            if (practica.getEstado() == PracticaRol.Estado.PARADA) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "No puedes registrar horas con una práctica parada.");
                return "redirect:/web/alumno/horas";
            }

            LocalTime pInicio = (pausaInicio != null && !pausaInicio.isBlank()) ? LocalTime.parse(pausaInicio) : null;
            LocalTime pFin    = (pausaFin    != null && !pausaFin.isBlank())    ? LocalTime.parse(pausaFin)    : null;

            registroService.registrar(practica, LocalDate.parse(fecha),
                    LocalTime.parse(horaInicio), LocalTime.parse(horaFin), pInicio, pFin);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Horas registradas correctamente. Pendientes de validación.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al registrar horas: " + e.getMessage());
        }
        return "redirect:/web/alumno/horas";
    }

    @GetMapping("/web/gestor/validarHoras")
    public String validarHorasGestor(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        noCache(response);
        GestorRol gestor = (GestorRol) usuario;

        List<PracticaRol> practicas = practicaRepository.findAll().stream()
                .filter(p -> p.getEmpresa().getIdEmpresa() == gestor.getIdEmpresa())
                .toList();

        model.addAttribute("usuario", gestor);
        model.addAttribute("pendientes", registroService.getPendientesByPracticas(practicas));
        return "gestor/validarHoras";
    }

    @GetMapping("/web/coordinador/validarHoras")
    public String validarHorasCoordinador(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        noCache(response);
        CoordinadorRol coordinador = (CoordinadorRol) usuario;

        List<PracticaRol> practicas = practicaRepository.findAll().stream()
                .filter(p -> p.getCoordinador().getIdUsuario() == coordinador.getIdUsuario())
                .toList();

        model.addAttribute("usuario", coordinador);
        model.addAttribute("pendientes", registroService.getPendientesByPracticas(practicas));
        return "coordinador/validarHoras";
    }

    @PostMapping("/web/horas/validar/{id}")
    public String validar(@PathVariable int id, @RequestParam String origen,
                          HttpSession session, RedirectAttributes redirectAttributes) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol) && !(usuario instanceof CoordinadorRol))
            return "redirect:/web/login";
        try {
            registroService.validar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Horas validadas correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al validar: " + e.getMessage());
        }
        return "redirect:/web/" + origen + "/validarHoras";
    }

    @PostMapping("/web/horas/rechazar/{id}")
    public String rechazar(@PathVariable int id, @RequestParam String origen,
                           HttpSession session, RedirectAttributes redirectAttributes) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol) && !(usuario instanceof CoordinadorRol))
            return "redirect:/web/login";
        try {
            registroService.rechazar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Horas rechazadas. Se ha notificado al alumno.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/web/" + origen + "/validarHoras";
    }

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}