package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.AlumnoRol;
import main.services.AlumnoService;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String contrasenya) {
        AlumnoRol alumno = alumnoService.login(email, contrasenya);
        if (alumno == null) return "Credenciales incorrectas";
        return "Login exitoso: Alumno";
    }

    @GetMapping
    public List<AlumnoRol> getAllAlumnos() {
        return alumnoService.getAllAlumnos();
    }
}
