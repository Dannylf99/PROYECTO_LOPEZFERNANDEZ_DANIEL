package main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import main.services.LoginService;
import main.roles.*;

@Controller
@RequestMapping("/web")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 1️⃣ Mostrar formulario de login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Thymeleaf buscará login.html
    }

    // 2️⃣ Procesar login
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String contrasenya,
                               HttpSession session,
                               Model model) {

        Object usuario = loginService.login(email, contrasenya);

        if (usuario == null) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login"; // vuelve a login con mensaje
        }

        // Ejecutar mensaje de login según el rol
        if (usuario instanceof AlumnoRol) ((AlumnoRol) usuario).iniciarSesion();
        if (usuario instanceof CoordinadorRol) ((CoordinadorRol) usuario).iniciarSesion();
        if (usuario instanceof GestorRol) ((GestorRol) usuario).iniciarSesion();
        if (usuario instanceof AdministracionRol) ((AdministracionRol) usuario).iniciarSesion();

        // Guardamos el usuario en sesión
        session.setAttribute("usuario", usuario);

        // Redirigir a la página de inicio según el rol
        if (usuario instanceof AlumnoRol) return "redirect:/web/inicioAlumno";
        if (usuario instanceof CoordinadorRol) return "redirect:/web/inicioCoordinador";
        if (usuario instanceof GestorRol) return "redirect:/web/inicioGestor";
        if (usuario instanceof AdministracionRol) return "redirect:/web/inicioAdministracion";

        return "login"; // fallback
    }

    // 3️⃣ Página de inicio del alumno
    @GetMapping("/inicioAlumno")
    public String inicioAlumno(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null) return "redirect:/web/login";
        model.addAttribute("usuario", usuario);
        return "inicioAlumno";
    }

    // 4️⃣ Página de inicio del coordinador
    @GetMapping("/inicioCoordinador")
    public String inicioCoordinador(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null) return "redirect:/web/login";
        model.addAttribute("usuario", usuario);
        return "inicioCoordinador";
    }

    // 5️⃣ Página de inicio del gestor
    @GetMapping("/inicioGestor")
    public String inicioGestor(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null) return "redirect:/web/login";
        model.addAttribute("usuario", usuario);
        return "inicioGestor";
    }

    // 6️⃣ Página de inicio de administración
    @GetMapping("/inicioAdministracion")
    public String inicioAdministracion(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null) return "redirect:/web/login";
        model.addAttribute("usuario", usuario);
        return "inicioAdministracion";
    }
}
