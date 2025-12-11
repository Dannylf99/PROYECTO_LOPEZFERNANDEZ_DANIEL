package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.AdministracionRol;
import main.services.AdministracionService;

import java.util.List;

@RestController
@RequestMapping("/administracion")
public class AdministracionController {

    private final AdministracionService adminService;

    public AdministracionController(AdministracionService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String contrasenya) {
        AdministracionRol admin = adminService.login(email, contrasenya);
        if (admin == null) return "Credenciales incorrectas";
        return "Login exitoso: Administracion";
    }

    @GetMapping
    public List<AdministracionRol> getAllAdministradores() {
        return adminService.getAllAdministradores();
    }
}
