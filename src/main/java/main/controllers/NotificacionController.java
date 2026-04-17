package main.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.roles.AlumnoRol;
import main.roles.NotificacionRol;
import main.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NotificacionController {

    @Autowired private NotificacionService notificacionService;

    // ── Página de notificaciones del alumno ─────────────
    @GetMapping("/web/alumno/notificaciones")
    public String verNotificaciones(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        AlumnoRol alumno = (AlumnoRol) usuario;

        List<NotificacionRol> todas = notificacionService.getNotificacionesByAlumno(alumno);
        List<NotificacionRol> noLeidas = todas.stream().filter(n -> !n.isLeida()).toList();
        List<NotificacionRol> leidas   = todas.stream().filter(NotificacionRol::isLeida).toList();

        model.addAttribute("usuario", alumno);
        model.addAttribute("noLeidas", noLeidas);
        model.addAttribute("leidas", leidas);
        model.addAttribute("notificacionesNoLeidas", notificacionService.countNoLeidas(alumno));
        return "alumno/notificaciones";
    }

    // ── Marcar como leída ───────────────────────────────
    @PostMapping("/web/alumno/notificaciones/leer/{id}")
    public String marcarLeida(@PathVariable int id, HttpSession session) {
        if (!(session.getAttribute("usuario") instanceof AlumnoRol)) return "redirect:/web/login";
        notificacionService.marcarLeida(id);
        return "redirect:/web/alumno/notificaciones";
    }

    // ── Borrar (baja lógica) ────────────────────────────
    @PostMapping("/web/alumno/notificaciones/borrar/{id}")
    public String borrar(@PathVariable int id, HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (!(session.getAttribute("usuario") instanceof AlumnoRol)) return "redirect:/web/login";
        notificacionService.borrar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Notificación eliminada.");
        return "redirect:/web/alumno/notificaciones";
    }

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}