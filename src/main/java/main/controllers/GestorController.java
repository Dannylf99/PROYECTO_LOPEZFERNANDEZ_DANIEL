package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.repositories.PracticaRepository;
import main.roles.GestorRol;
import main.roles.PracticaRol;
import main.services.DocumentoService;
import main.services.GestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/gestor")
public class GestorController {

    @Autowired private GestorService gestorService;
    @Autowired private DocumentoService documentoService;
    @Autowired private PracticaRepository practicaRepository;

    @GetMapping("/inicio")
    public String inicioGestor(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        GestorRol gestor = (GestorRol) usuario;
        model.addAttribute("usuario", gestor);
        return "gestor/inicioGestor";
    }

    @GetMapping("/documentos")
    public String documentosGestor(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof GestorRol)) return "redirect:/web/login";
        GestorRol gestor = (GestorRol) usuario;

        model.addAttribute("usuario", gestor);
        model.addAttribute("pendientes",
                documentoService.findPendientesFirmaGestor(gestor.getIdEmpresa()));
        model.addAttribute("practicas",
                practicaRepository.findByEmpresaIdAndEstado(
                        gestor.getIdEmpresa(), PracticaRol.Estado.ACTIVA));
        model.addAttribute("todosDocumentos",
                documentoService.findByEmpresa(gestor.getIdEmpresa()));

        return "gestor/documentosGestor";
    }
}