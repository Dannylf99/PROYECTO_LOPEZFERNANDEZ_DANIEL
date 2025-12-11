package main.services;

import org.springframework.stereotype.Service;

import main.repositories.EmpresaRepository;
import main.roles.EmpresaRol;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepo;

    public EmpresaService(EmpresaRepository empresaRepo) {
        this.empresaRepo = empresaRepo;
    }

    public List<EmpresaRol> getAllEmpresas() {
        return empresaRepo.findAll();
    }

    public EmpresaRol saveEmpresa(EmpresaRol empresa) {
        return empresaRepo.save(empresa);
    }
}
