package main.controllers;

import main.dtos.UsuarioListaDTO;
import main.dtos.CrearUsuarioDTO;
import main.repositories.*;

import main.entities.*;

import main.roles.AdministracionRol;
import main.roles.AlumnoRol;
import main.roles.CoordinadorRol;
import main.roles.GestorRol;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

    @Autowired private AlumnoRepository alumnoRepository;
    @Autowired private CoordinadorRepository coordinadorRepository;
    @Autowired private GestorRepository gestorRepository;
    @Autowired private AdministracionRepository administracionRepository;
    @Autowired private EmpresaRepository empresaRepository;

    // ── GET: lista de usuarios ──────────────────────────
    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioListaDTO> usuarios = new ArrayList<>();

        alumnoRepository.findAll().forEach(a -> usuarios.add(
                new UsuarioListaDTO((long) a.getIdUsuario(),
                        a.getNombre(), a.getApellidos(), a.getEmail(), "ALUMNO")));

        coordinadorRepository.findAll().forEach(c -> usuarios.add(
                new UsuarioListaDTO((long) c.getIdUsuario(),
                        c.getNombre(), c.getApellidos(), c.getEmail(), "COORDINADOR")));

        gestorRepository.findAll().forEach(g -> usuarios.add(
                new UsuarioListaDTO((long) g.getIdUsuario(),
                        g.getNombre(), g.getApellidos(), g.getEmail(), "GESTOR")));

        administracionRepository.findAll().forEach(ad -> usuarios.add(
                new UsuarioListaDTO((long) ad.getIdUsuario(),
                        ad.getNombre(), ad.getApellidos(), ad.getEmail(), "ADMINISTRACION")));

        model.addAttribute("usuarios", usuarios);
        return "administracion/gestionUsuarios";
    }

    // ── GET: formulario de creación ─────────────────────
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("empresas", empresaRepository.findAll());
        return "administracion/crearUsuario";
    }

    // ── POST: guardar nuevo usuario ─────────────────────
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute CrearUsuarioDTO dto,
                               RedirectAttributes redirectAttributes) {
        try {
            switch (dto.getTipo()) {
                case "ALUMNO" -> {
                    AlumnoRol a = new AlumnoRol();
                    a.setNombre(dto.getNombre());
                    a.setApellidos(dto.getApellidos());
                    a.setEmail(dto.getEmail());
                    a.setContrasenya(dto.getContrasenya());
                    if (dto.getEmpresaAsignada() != null)
                        a.setEmpresaAsignada(empresaRepository
                                .findById(dto.getEmpresaAsignada()).orElse(null).getIdEmpresa());
                    alumnoRepository.save(a);
                }
                case "COORDINADOR" -> {
                    CoordinadorRol c = new CoordinadorRol();
                    c.setNombre(dto.getNombre());
                    c.setApellidos(dto.getApellidos());
                    c.setEmail(dto.getEmail());
                    c.setContrasenya(dto.getContrasenya());
                    coordinadorRepository.save(c);
                }
                case "GESTOR" -> {
                    GestorRol g = new GestorRol();
                    g.setNombre(dto.getNombre());
                    g.setApellidos(dto.getApellidos());
                    g.setEmail(dto.getEmail());
                    g.setContrasenya(dto.getContrasenya());
                    if (dto.getIdEmpresa() != null)
                        g.setIdEmpresa(empresaRepository
                                .findById(dto.getIdEmpresa()).orElse(null).getIdEmpresa());
                    gestorRepository.save(g);
                }
                case "ADMINISTRACION" -> {
                    AdministracionRol ad = new AdministracionRol();
                    ad.setNombre(dto.getNombre());
                    ad.setApellidos(dto.getApellidos());
                    ad.setEmail(dto.getEmail());
                    ad.setContrasenya(dto.getContrasenya());
                    administracionRepository.save(ad);
                }
            }
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario creado correctamente.");
            return "redirect:/admin/usuarios";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al crear el usuario: " + e.getMessage());
            return "redirect:/admin/usuarios/nuevo";
        }
    }

    // ── POST: eliminar usuario ──────────────────────────
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  @RequestParam String tipo,
                                  RedirectAttributes redirectAttributes) {
        try {
            switch (tipo) {
                case "ALUMNO"         -> alumnoRepository.deleteById(id.intValue());
                case "COORDINADOR"    -> coordinadorRepository.deleteById(id.intValue());
                case "GESTOR"         -> gestorRepository.deleteById(id.intValue());
                case "ADMINISTRACION" -> administracionRepository.deleteById(id.intValue());
            }
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}

