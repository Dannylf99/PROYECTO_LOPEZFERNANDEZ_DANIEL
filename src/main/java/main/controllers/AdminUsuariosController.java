package main.controllers;

import main.dtos.UsuarioListaDTO;
import main.dtos.CrearUsuarioDTO;
import main.repositories.*;
import main.roles.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

    @Autowired private AlumnoRepository alumnoRepository;
    @Autowired private CoordinadorRepository coordinadorRepository;
    @Autowired private GestorRepository gestorRepository;
    @Autowired private AdministracionRepository administracionRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // ── GET: lista de usuarios (solo activos) ───────────
    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioListaDTO> usuarios = new ArrayList<>();

        alumnoRepository.findByActivoTrue().forEach(a -> usuarios.add(
                new UsuarioListaDTO((long) a.getIdUsuario(),
                        a.getNombre(), a.getApellidos(), a.getDni(), a.getEmail(), "ALUMNO")));

        coordinadorRepository.findByActivoTrue().forEach(c -> usuarios.add(
                new UsuarioListaDTO((long) c.getIdUsuario(),
                        c.getNombre(), c.getApellidos(), c.getDni(), c.getEmail(), "COORDINADOR")));

        gestorRepository.findByActivoTrue().forEach(g -> usuarios.add(
                new UsuarioListaDTO((long) g.getIdUsuario(),
                        g.getNombre(), g.getApellidos(), g.getDni(), g.getEmail(), "GESTOR")));

        administracionRepository.findByActivoTrue().forEach(ad -> usuarios.add(
                new UsuarioListaDTO((long) ad.getIdUsuario(),
                        ad.getNombre(), ad.getApellidos(), ad.getDni(), ad.getEmail(), "ADMINISTRACION")));

        model.addAttribute("usuarios", usuarios);
        return "administracion/gestionUsuarios";
    }

    // ── GET: formulario de creación ─────────────────────
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("empresas", empresaRepository.findAll());
        model.addAttribute("usuario", new CrearUsuarioDTO());
        return "administracion/crearUsuario";
    }

    // ── POST: guardar nuevo usuario (o reactivar si DNI existe inactivo) ──
    @PostMapping("/crear")
    public String crearUsuario(@Valid @ModelAttribute("usuario") CrearUsuarioDTO dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("empresas", empresaRepository.findAll());
            return "administracion/crearUsuario";
        }

        // Si existe un usuario inactivo con ese DNI, se reactiva con los nuevos datos
        if (reactivarSiExiste(dto)) {
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "El usuario ya existía y ha sido reactivado con los nuevos datos.");
            return "redirect:/admin/usuarios";
        }

        // Verificar DNI duplicado en usuarios activos
        if (existeDniActivo(dto.getDni())) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ya existe un usuario activo con el DNI: " + dto.getDni());
            return "redirect:/admin/usuarios/nuevo";
        }

        // Verificar email duplicado en usuarios activos
        if (existeEmailActivo(dto.getEmail())) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ya existe un usuario activo con el email: " + dto.getEmail());
            return "redirect:/admin/usuarios/nuevo";
        }

        try {
            String contrasenaCifrada = passwordEncoder.encode(dto.getContrasenya());

            switch (dto.getTipo()) {
                case "ALUMNO" -> {
                    AlumnoRol a = new AlumnoRol();
                    a.setNombre(dto.getNombre());
                    a.setApellidos(dto.getApellidos());
                    a.setDni(dto.getDni());
                    a.setEmail(dto.getEmail());
                    a.setContrasenya(contrasenaCifrada);
                    a.setEmpresaAsignada(dto.getEmpresaAsignada());
                    a.setActivo(true);
                    alumnoRepository.save(a);
                }
                case "COORDINADOR" -> {
                    CoordinadorRol c = new CoordinadorRol();
                    c.setNombre(dto.getNombre());
                    c.setApellidos(dto.getApellidos());
                    c.setDni(dto.getDni());
                    c.setEmail(dto.getEmail());
                    c.setContrasenya(contrasenaCifrada);
                    c.setActivo(true);
                    coordinadorRepository.save(c);
                }
                case "GESTOR" -> {
                    GestorRol g = new GestorRol();
                    g.setNombre(dto.getNombre());
                    g.setApellidos(dto.getApellidos());
                    g.setDni(dto.getDni());
                    g.setEmail(dto.getEmail());
                    g.setContrasenya(contrasenaCifrada);
                    g.setIdEmpresa(dto.getIdEmpresa());
                    g.setActivo(true);
                    gestorRepository.save(g);
                }
                case "ADMINISTRACION" -> {
                    AdministracionRol ad = new AdministracionRol();
                    ad.setNombre(dto.getNombre());
                    ad.setApellidos(dto.getApellidos());
                    ad.setDni(dto.getDni());
                    ad.setEmail(dto.getEmail());
                    ad.setContrasenya(contrasenaCifrada);
                    ad.setActivo(true);
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

    // ── GET: formulario de edición ──────────────────────
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id,
                                           @RequestParam String tipo,
                                           Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            CrearUsuarioDTO dto = new CrearUsuarioDTO();

            switch (tipo) {
                case "ALUMNO" -> {
                    AlumnoRol a = alumnoRepository.findById(id.intValue()).orElseThrow();
                    dto.setNombre(a.getNombre());
                    dto.setApellidos(a.getApellidos());
                    dto.setDni(a.getDni());
                    dto.setEmail(a.getEmail());
                    dto.setTipo("ALUMNO");
                    dto.setEmpresaAsignada(a.getEmpresaAsignada());
                }
                case "COORDINADOR" -> {
                    CoordinadorRol c = coordinadorRepository.findById(id.intValue()).orElseThrow();
                    dto.setNombre(c.getNombre());
                    dto.setApellidos(c.getApellidos());
                    dto.setDni(c.getDni());
                    dto.setEmail(c.getEmail());
                    dto.setTipo("COORDINADOR");
                }
                case "GESTOR" -> {
                    GestorRol g = gestorRepository.findById(id.intValue()).orElseThrow();
                    dto.setNombre(g.getNombre());
                    dto.setApellidos(g.getApellidos());
                    dto.setDni(g.getDni());
                    dto.setEmail(g.getEmail());
                    dto.setTipo("GESTOR");
                    dto.setIdEmpresa(g.getIdEmpresa());
                }
                case "ADMINISTRACION" -> {
                    AdministracionRol ad = administracionRepository.findById(id.intValue()).orElseThrow();
                    dto.setNombre(ad.getNombre());
                    dto.setApellidos(ad.getApellidos());
                    dto.setDni(ad.getDni());
                    dto.setEmail(ad.getEmail());
                    dto.setTipo("ADMINISTRACION");
                }
            }

            model.addAttribute("usuario", dto);
            model.addAttribute("id", id);
            model.addAttribute("empresas", empresaRepository.findAll());
            return "administracion/editarUsuario";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        }
    }

    // ── POST: actualizar usuario ────────────────────────
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @ModelAttribute CrearUsuarioDTO dto,
                                    RedirectAttributes redirectAttributes) {
        try {
            switch (dto.getTipo()) {
                case "ALUMNO" -> {
                    AlumnoRol a = alumnoRepository.findById(id.intValue()).orElseThrow();
                    a.setNombre(dto.getNombre());
                    a.setApellidos(dto.getApellidos());
                    a.setDni(dto.getDni());
                    a.setEmail(dto.getEmail());
                    if (dto.getContrasenya() != null && !dto.getContrasenya().isEmpty()) {
                        a.setContrasenya(passwordEncoder.encode(dto.getContrasenya()));
                    }
                    a.setEmpresaAsignada(dto.getEmpresaAsignada());
                    alumnoRepository.save(a);
                }
                case "COORDINADOR" -> {
                    CoordinadorRol c = coordinadorRepository.findById(id.intValue()).orElseThrow();
                    c.setNombre(dto.getNombre());
                    c.setApellidos(dto.getApellidos());
                    c.setDni(dto.getDni());
                    c.setEmail(dto.getEmail());
                    if (dto.getContrasenya() != null && !dto.getContrasenya().isEmpty()) {
                        c.setContrasenya(passwordEncoder.encode(dto.getContrasenya()));
                    }
                    coordinadorRepository.save(c);
                }
                case "GESTOR" -> {
                    GestorRol g = gestorRepository.findById(id.intValue()).orElseThrow();
                    g.setNombre(dto.getNombre());
                    g.setApellidos(dto.getApellidos());
                    g.setDni(dto.getDni());
                    g.setEmail(dto.getEmail());
                    if (dto.getContrasenya() != null && !dto.getContrasenya().isEmpty()) {
                        g.setContrasenya(passwordEncoder.encode(dto.getContrasenya()));
                    }
                    g.setIdEmpresa(dto.getIdEmpresa());
                    gestorRepository.save(g);
                }
                case "ADMINISTRACION" -> {
                    AdministracionRol ad = administracionRepository.findById(id.intValue()).orElseThrow();
                    ad.setNombre(dto.getNombre());
                    ad.setApellidos(dto.getApellidos());
                    ad.setDni(dto.getDni());
                    ad.setEmail(dto.getEmail());
                    if (dto.getContrasenya() != null && !dto.getContrasenya().isEmpty()) {
                        ad.setContrasenya(passwordEncoder.encode(dto.getContrasenya()));
                    }
                    administracionRepository.save(ad);
                }
            }
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    // ── POST: baja lógica (activo = false) ──────────────
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  @RequestParam String tipo,
                                  RedirectAttributes redirectAttributes) {
        try {
            switch (tipo) {
                case "ALUMNO" -> {
                    AlumnoRol a = alumnoRepository.findById(id.intValue()).orElseThrow();
                    a.setActivo(false);
                    alumnoRepository.save(a);
                }
                case "COORDINADOR" -> {
                    CoordinadorRol c = coordinadorRepository.findById(id.intValue()).orElseThrow();
                    c.setActivo(false);
                    coordinadorRepository.save(c);
                }
                case "GESTOR" -> {
                    GestorRol g = gestorRepository.findById(id.intValue()).orElseThrow();
                    g.setActivo(false);
                    gestorRepository.save(g);
                }
                case "ADMINISTRACION" -> {
                    AdministracionRol ad = administracionRepository.findById(id.intValue()).orElseThrow();
                    ad.setActivo(false);
                    administracionRepository.save(ad);
                }
            }
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario dado de baja correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al dar de baja el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    // ── MÉTODOS AUXILIARES ─────────────────────────────

    // Busca un usuario inactivo con ese DNI y lo reactiva con los nuevos datos
    private boolean reactivarSiExiste(CrearUsuarioDTO dto) {
        String contrasenaCifrada = passwordEncoder.encode(dto.getContrasenya());

        Optional<AlumnoRol> alumno = alumnoRepository.findByDni(dto.getDni());
        if (alumno.isPresent() && !alumno.get().isActivo()) {
            AlumnoRol a = alumno.get();
            a.setNombre(dto.getNombre());
            a.setApellidos(dto.getApellidos());
            a.setEmail(dto.getEmail());
            a.setContrasenya(contrasenaCifrada);
            a.setEmpresaAsignada(dto.getEmpresaAsignada());
            a.setActivo(true);
            alumnoRepository.save(a);
            return true;
        }

        Optional<CoordinadorRol> coordinador = coordinadorRepository.findByDni(dto.getDni());
        if (coordinador.isPresent() && !coordinador.get().isActivo()) {
            CoordinadorRol c = coordinador.get();
            c.setNombre(dto.getNombre());
            c.setApellidos(dto.getApellidos());
            c.setEmail(dto.getEmail());
            c.setContrasenya(contrasenaCifrada);
            c.setActivo(true);
            coordinadorRepository.save(c);
            return true;
        }

        Optional<GestorRol> gestor = gestorRepository.findByDni(dto.getDni());
        if (gestor.isPresent() && !gestor.get().isActivo()) {
            GestorRol g = gestor.get();
            g.setNombre(dto.getNombre());
            g.setApellidos(dto.getApellidos());
            g.setEmail(dto.getEmail());
            g.setContrasenya(contrasenaCifrada);
            g.setIdEmpresa(dto.getIdEmpresa());
            g.setActivo(true);
            gestorRepository.save(g);
            return true;
        }

        Optional<AdministracionRol> admin = administracionRepository.findByDni(dto.getDni());
        if (admin.isPresent() && !admin.get().isActivo()) {
            AdministracionRol ad = admin.get();
            ad.setNombre(dto.getNombre());
            ad.setApellidos(dto.getApellidos());
            ad.setEmail(dto.getEmail());
            ad.setContrasenya(contrasenaCifrada);
            ad.setActivo(true);
            administracionRepository.save(ad);
            return true;
        }

        return false;
    }

    private boolean existeDniActivo(String dni) {
        return alumnoRepository.findByDni(dni).map(u -> u.isActivo()).orElse(false) ||
                coordinadorRepository.findByDni(dni).map(u -> u.isActivo()).orElse(false) ||
                gestorRepository.findByDni(dni).map(u -> u.isActivo()).orElse(false) ||
                administracionRepository.findByDni(dni).map(u -> u.isActivo()).orElse(false);
    }

    private boolean existeEmailActivo(String email) {
        return alumnoRepository.findByEmailAndActivoTrue(email).isPresent() ||
                coordinadorRepository.findByEmailAndActivoTrue(email).isPresent() ||
                gestorRepository.findByEmailAndActivoTrue(email).isPresent() ||
                administracionRepository.findByEmailAndActivoTrue(email).isPresent();
    }
}