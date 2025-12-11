package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.*;
import main.services.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AlumnoService alumnoService;
    private final CoordinadorService coordinadorService;
    private final GestorService gestorService;
    private final AdministracionService adminService;

    public LoginController(AlumnoService alumnoService,
                           CoordinadorService coordinadorService,
                           GestorService gestorService,
                           AdministracionService adminService) {
        this.alumnoService = alumnoService;
        this.coordinadorService = coordinadorService;
        this.gestorService = gestorService;
        this.adminService = adminService;
    }

    @PostMapping
    public Map<String, String> login(@RequestParam String email, @RequestParam String contrasenya) {
        Map<String, String> response = new HashMap<>();

        if (alumnoService.login(email, contrasenya) != null) {
            response.put("rol", "Alumno");
            response.put("mensaje", "Login exitoso");
        } else if (coordinadorService.login(email, contrasenya) != null) {
            response.put("rol", "Coordinador");
            response.put("mensaje", "Login exitoso");
        } else if (gestorService.login(email, contrasenya) != null) {
            response.put("rol", "Gestor");
            response.put("mensaje", "Login exitoso");
        } else if (adminService.login(email, contrasenya) != null) {
            response.put("rol", "Administracion");
            response.put("mensaje", "Login exitoso");
        } else {
            response.put("rol", "Ninguno");
            response.put("mensaje", "Credenciales incorrectas");
        }

        return response;
    }
}
