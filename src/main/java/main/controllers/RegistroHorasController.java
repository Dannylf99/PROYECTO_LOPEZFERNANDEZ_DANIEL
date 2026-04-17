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

    // ── GET: formulario de registro de horas (alumno) ───
    @GetMapping("/web/alumno/horas")
    public String mostrarFormulario(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        AlumnoRol alumno = (AlumnoRol) usuario;

        List<PracticaRol> practicas = practicaService.getPracticasByAlumno(alumno).stream()
                .filter(p -> p.getEstado() == PracticaRol.Estado.ACTIVA)
                .toList();

        // Cargar todos los registros de las prácticas del alumno para el historial
        List<RegistroHorasRol> registros = new ArrayList<>();
        for (PracticaRol p : practicaService.getPracticasByAlumno(alumno)) {
            registros.addAll(registroService.getByPractica(p));
        }
        // Ordenar por fecha descendente
        registros.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));

        model.addAttribute("usuario", alumno);
        model.addAttribute("practicas", practicas);
        model.addAttribute("registros", registros);
        return "alumno/registrarHoras";
    }

    // ── POST: guardar registro de horas (alumno) ────────
    @PostMapping("/web/alumno/horas/registrar")
    public String registrar(@RequestParam int idPractica,
                            @RequestParam String fecha,
                            @RequestParam String horaInicio,
                            @RequestParam String horaFin,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";

        try {
            PracticaRol practica = practicaRepository.findById(idPractica).orElseThrow();
            registroService.registrar(practica,
                    LocalDate.parse(fecha),
                    LocalTime.parse(horaInicio),
                    LocalTime.parse(horaFin));
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Horas registradas correctamente. Pendientes de validación.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al registrar horas: " + e.getMessage());
        }
        return "redirect:/web/alumno/horas";
    }

    // ── GET: validar horas (gestor) ─────────────────────
    @GetMapping("/web/gestor/validarHoras")
    public String validarHorasGestor(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        noCache(response);
        GestorRol gestor = (GestorRol) usuario;

        List<PracticaRol> practicas = practicaRepository.findAll().stream()
                .filter(p -> p.getEmpresa().getIdEmpresa() == gestor.getIdEmpresa())
                .toList();
        List<RegistroHorasRol> pendientes = registroService.getPendientesByPracticas(practicas);

        model.addAttribute("usuario", gestor);
        model.addAttribute("pendientes", pendientes);
        return "gestor/validarHoras";
    }

    // ── GET: validar horas (coordinador) ────────────────
    @GetMapping("/web/coordinador/validarHoras")
    public String validarHorasCoordinador(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        noCache(response);
        CoordinadorRol coordinador = (CoordinadorRol) usuario;

        List<PracticaRol> practicas = practicaRepository.findAll().stream()
                .filter(p -> p.getCoordinador().getIdUsuario() == coordinador.getIdUsuario())
                .toList();
        List<RegistroHorasRol> pendientes = registroService.getPendientesByPracticas(practicas);

        model.addAttribute("usuario", coordinador);
        model.addAttribute("pendientes", pendientes);
        return "coordinador/validarHoras";
    }

    // ── POST: validar un registro ───────────────────────
    @PostMapping("/web/horas/validar/{id}")
    public String validar(@PathVariable int id,
                          @RequestParam String origen,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
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

    // ── POST: rechazar un registro ──────────────────────
    @PostMapping("/web/horas/rechazar/{id}")
    public String rechazar(@PathVariable int id,
                           @RequestParam String origen,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
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