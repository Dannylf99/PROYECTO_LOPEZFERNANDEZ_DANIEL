package main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import main.services.LoginService;
import main.services.NotificacionService;
import main.services.PracticaService;
import main.repositories.GestorRepository;
import main.roles.*;

import java.util.List;

@Controller
@RequestMapping("/web")
public class LoginController {

    private final LoginService loginService;
    @Autowired private NotificacionService notificacionService;
    @Autowired private PracticaService practicaService;
    @Autowired private GestorRepository gestorRepository;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, HttpServletResponse response) {
        noCache(response);
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String contrasenya,
                               HttpSession session, Model model) {
        Object usuario = loginService.login(email, contrasenya);
        if (usuario == null) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
        session.setAttribute("usuario", usuario);

        if (usuario instanceof AlumnoRol) {
            session.setAttribute("rol", "ALUMNO");
            ((AlumnoRol) usuario).iniciarSesion();
            return "redirect:/web/inicioAlumno";
        }
        if (usuario instanceof CoordinadorRol) {
            session.setAttribute("rol", "COORDINADOR");
            ((CoordinadorRol) usuario).iniciarSesion();
            return "redirect:/web/inicioCoordinador";
        }
        if (usuario instanceof GestorRol) {
            session.setAttribute("rol", "GESTOR");
            ((GestorRol) usuario).iniciarSesion();
            return "redirect:/web/inicioGestor";
        }
        if (usuario instanceof AdministracionRol) {
            session.setAttribute("rol", "ADMINISTRACION");
            ((AdministracionRol) usuario).iniciarSesion();
            return "redirect:/web/inicioAdministracion";
        }
        return "redirect:/web/login";
    }

    @GetMapping("/inicioAlumno")
    public String inicioAlumno(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        AlumnoRol alumno = (AlumnoRol) usuario;
        model.addAttribute("usuario", alumno);
        model.addAttribute("notificacionesNoLeidas", notificacionService.countNoLeidas(alumno));
        return "alumno/inicioAlumno";
    }

    @GetMapping("/alumno/practicas")
    public String practicasAlumno(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        AlumnoRol alumno = (AlumnoRol) usuario;
        List<PracticaRol> practicas = practicaService.getPracticasByAlumno(alumno);

        java.util.Map<Integer, GestorRol> gestoresPorPractica = new java.util.HashMap<>();
        for (PracticaRol p : practicas) {
            gestorRepository.findByIdEmpresaAndActivoTrue(p.getEmpresa().getIdEmpresa())
                    .stream().findFirst().ifPresent(g -> gestoresPorPractica.put(p.getIdPractica(), g));
        }
        model.addAttribute("usuario", alumno);
        model.addAttribute("practicas", practicas);
        model.addAttribute("gestoresPorPractica", gestoresPorPractica);
        model.addAttribute("notificacionesNoLeidas", notificacionService.countNoLeidas(alumno));
        return "alumno/practicasAlumno";
    }

    @GetMapping("/inicioCoordinador")
    public String inicioCoordinador(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "coordinador/inicioCoordinador";
    }

    @GetMapping("/inicioGestor")
    public String inicioGestor(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "gestor/inicioGestor";
    }

    @GetMapping("/inicioAdministracion")
    public String inicioAdministracion(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AdministracionRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "administracion/inicioAdmin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof AlumnoRol)         ((AlumnoRol) usuario).cerrarSesion();
        if (usuario instanceof CoordinadorRol)    ((CoordinadorRol) usuario).cerrarSesion();
        if (usuario instanceof GestorRol)         ((GestorRol) usuario).cerrarSesion();
        if (usuario instanceof AdministracionRol) ((AdministracionRol) usuario).cerrarSesion();
        session.invalidate();
        noCache(response);
        return "redirect:/web/login";
    }

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}