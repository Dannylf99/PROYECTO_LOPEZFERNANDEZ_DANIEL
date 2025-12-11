package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.EmpresaRol;
import main.services.EmpresaService;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public List<EmpresaRol> getAllEmpresas() {
        return empresaService.getAllEmpresas();
    }

    @PostMapping("/save")
    public EmpresaRol saveEmpresa(@RequestBody EmpresaRol empresa) {
        return empresaService.saveEmpresa(empresa);
    }
}