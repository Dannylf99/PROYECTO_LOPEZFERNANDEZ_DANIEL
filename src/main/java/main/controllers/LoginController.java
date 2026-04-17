package main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import main.services.LoginService;
import main.services.PracticaService;
import main.repositories.GestorRepository;
import main.roles.*;

import java.util.List;

@Controller
@RequestMapping("/web")
public class LoginController {

    private final LoginService loginService;

    @Autowired private PracticaService practicaService;
    @Autowired private GestorRepository gestorRepository;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 1️⃣ Mostrar formulario de login
    @GetMapping("/login")
    public String showLoginForm(HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "login";
    }

    // 2️⃣ Procesar login
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String contrasenya,
                               HttpSession session,
                               Model model) {
        System.out.println("🔍 Intentando login con email: " + email);
        Object usuario = loginService.login(email, contrasenya);

        if (usuario == null) {
            System.out.println("❌ Login fallido");
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }

        System.out.println("✅ Login exitoso: " + usuario.getClass().getSimpleName());
        session.setAttribute("usuario", usuario);

        if (usuario instanceof AlumnoRol) {
            ((AlumnoRol) usuario).iniciarSesion();
            return "redirect:/web/inicioAlumno";
        }
        if (usuario instanceof CoordinadorRol) {
            ((CoordinadorRol) usuario).iniciarSesion();
            return "redirect:/web/inicioCoordinador";
        }
        if (usuario instanceof GestorRol) {
            ((GestorRol) usuario).iniciarSesion();
            return "redirect:/web/inicioGestor";
        }
        if (usuario instanceof AdministracionRol) {
            ((AdministracionRol) usuario).iniciarSesion();
            return "redirect:/web/inicioAdministracion";
        }

        return "redirect:/web/login";
    }

    // 3️⃣ Inicio alumno
    @GetMapping("/inicioAlumno")
    public String inicioAlumno(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "alumno/inicioAlumno";
    }

    // 4️⃣ Prácticas del alumno
    @GetMapping("/alumno/practicas")
    public String practicasAlumno(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return "redirect:/web/login";
        noCache(response);
        AlumnoRol alumno = (AlumnoRol) usuario;
        List<PracticaRol> practicas = practicaService.getPracticasByAlumno(alumno);

        // Para cada práctica obtenemos el primer gestor activo de la empresa
        java.util.Map<Integer, GestorRol> gestoresPorPractica = new java.util.HashMap<>();
        for (PracticaRol p : practicas) {
            gestorRepository.findByIdEmpresaAndActivoTrue(p.getEmpresa().getIdEmpresa())
                    .stream().findFirst().ifPresent(g -> gestoresPorPractica.put(p.getIdPractica(), g));
        }

        model.addAttribute("usuario", alumno);
        model.addAttribute("practicas", practicas);
        model.addAttribute("gestoresPorPractica", gestoresPorPractica);
        return "alumno/practicasAlumno";
    }

    // 5️⃣ Inicio coordinador
    @GetMapping("/inicioCoordinador")
    public String inicioCoordinador(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "coordinador/inicioCoordinador";
    }

    // 6️⃣ Inicio gestor
    @GetMapping("/inicioGestor")
    public String inicioGestor(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "gestor/inicioGestor";
    }

    // 7️⃣ Inicio administración
    @GetMapping("/inicioAdministracion")
    public String inicioAdministracion(HttpSession session, Model model, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AdministracionRol)) return "redirect:/web/login";
        noCache(response);
        model.addAttribute("usuario", usuario);
        return "administracion/inicioAdmin";
    }

    // 8️⃣ Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof AlumnoRol) ((AlumnoRol) usuario).cerrarSesion();
        if (usuario instanceof CoordinadorRol) ((CoordinadorRol) usuario).cerrarSesion();
        if (usuario instanceof GestorRol) ((GestorRol) usuario).cerrarSesion();
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