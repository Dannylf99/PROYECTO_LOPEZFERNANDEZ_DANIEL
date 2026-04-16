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
        return "login"; // Thymeleaf buscará templates/login.html
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

        // Guardamos el usuario en sesión
        session.setAttribute("usuario", usuario);

        // Ejecutar mensaje de login según el rol y redirigir
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

        return "redirect:/web/login"; // fallback
    }

    // 3️⃣ Página de inicio del alumno
    @GetMapping("/inicioAlumno")
    public String inicioAlumno(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) {
            return "redirect:/web/login";
        }
        model.addAttribute("usuario", usuario);
        return "alumno/inicioAlumno"; // Busca en templates/alumno/inicioAlumno.html
    }

    // 4️⃣ Página de inicio del coordinador
    @GetMapping("/inicioCoordinador")
    public String inicioCoordinador(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof CoordinadorRol)) {
            return "redirect:/web/login";
        }
        model.addAttribute("usuario", usuario);
        return "coordinador/inicioCoordinador"; // Busca en templates/coordinador/inicioCoordinador.html
    }

    // 5️⃣ Página de inicio del gestor
    @GetMapping("/inicioGestor")
    public String inicioGestor(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) {
            return "redirect:/web/login";
        }
        model.addAttribute("usuario", usuario);
        return "gestor/inicioGestor"; // Busca en templates/gestor/inicioGestor.html
    }

    // 6️⃣ Página de inicio de administración
    @GetMapping("/inicioAdministracion")
    public String inicioAdministracion(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AdministracionRol)) {
            return "redirect:/web/login";
        }
        model.addAttribute("usuario", usuario);
        return "administracion/inicioAdmin"; // Busca en templates/administracion/inicioAdministracion.html
    }

    // 7️⃣ Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Object usuario = session.getAttribute("usuario");

        // Ejecutar metodo de cerrar sesión según el rol
        if (usuario instanceof AlumnoRol) ((AlumnoRol) usuario).cerrarSesion();
        if (usuario instanceof CoordinadorRol) ((CoordinadorRol) usuario).cerrarSesion();
        if (usuario instanceof GestorRol) ((GestorRol) usuario).cerrarSesion();
        if (usuario instanceof AdministracionRol) ((AdministracionRol) usuario).cerrarSesion();

        session.invalidate();
        return "redirect:/web/login";
    }
}
