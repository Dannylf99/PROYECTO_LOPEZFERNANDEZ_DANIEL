package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.CoordinadorRol;
import main.services.CoordinadorService;

import java.util.List;

@RestController
@RequestMapping("/coordinadores")
public class CoordinadorController {

    private final CoordinadorService coordinadorService;

    public CoordinadorController(CoordinadorService coordinadorService) {
        this.coordinadorService = coordinadorService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String contrasenya) {
        CoordinadorRol coordinador = coordinadorService.login(email, contrasenya);
        if (coordinador == null) return "Credenciales incorrectas";
        return "Login exitoso: Coordinador";
    }

    @GetMapping
    public List<CoordinadorRol> getAllCoordinadores() {
        return coordinadorService.getAllCoordinadores();
    }
}
