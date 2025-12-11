package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.GestorRol;
import main.services.GestorService;

import java.util.List;

@RestController
@RequestMapping("/gestores")
public class GestorController {

    private final GestorService gestorService;

    public GestorController(GestorService gestorService) {
        this.gestorService = gestorService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String contrasenya) {
        GestorRol gestor = gestorService.login(email, contrasenya);
        if (gestor == null) return "Credenciales incorrectas";
        return "Login exitoso: Gestor";
    }

    @GetMapping
    public List<GestorRol> getAllGestores() {
        return gestorService.getAllGestores();
    }
}
