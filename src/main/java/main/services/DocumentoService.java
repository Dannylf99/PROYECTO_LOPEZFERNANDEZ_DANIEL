package main.services;

import org.springframework.stereotype.Service;

import main.repositories.DocumentoRepository;
import main.roles.DocumentoRol;

import java.util.List;

@Service
public class DocumentoService {

    private final DocumentoRepository docRepo;

    public DocumentoService(DocumentoRepository docRepo) {
        this.docRepo = docRepo;
    }

    public List<DocumentoRol> getAllDocumentos() {
        return docRepo.findAll();
    }

    public DocumentoRol saveDocumento(DocumentoRol documento) {
        return docRepo.save(documento);
    }
}
