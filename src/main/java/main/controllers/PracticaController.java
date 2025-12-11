package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.PracticaRol;
import main.services.PracticaService;

import java.util.List;

@RestController
@RequestMapping("/practicas")
public class PracticaController {

    private final PracticaService practicaService;

    public PracticaController(PracticaService practicaService) {
        this.practicaService = practicaService;
    }

    @GetMapping
    public List<PracticaRol> getAllPracticas() {
        return practicaService.getAllPracticas();
    }

    @PostMapping("/save")
    public PracticaRol savePractica(@RequestBody PracticaRol practica) {
        return practicaService.savePractica(practica);
    }
}
