package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.DocumentoRol;
import main.services.DocumentoService;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService docService;

    public DocumentoController(DocumentoService docService) {
        this.docService = docService;
    }

    @GetMapping
    public List<DocumentoRol> getAllDocumentos() {
        return docService.getAllDocumentos();
    }

    @PostMapping("/save")
    public DocumentoRol saveDocumento(@RequestBody DocumentoRol documento) {
        return docService.saveDocumento(documento);
    }
}
