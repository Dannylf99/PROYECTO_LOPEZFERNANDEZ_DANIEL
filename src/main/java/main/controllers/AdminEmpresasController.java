package main.controllers;

import main.repositories.EmpresaRepository;
import main.repositories.GestorRepository;
import main.roles.EmpresaRol;
import main.roles.GestorRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/empresas")
public class AdminEmpresasController {

    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private GestorRepository gestorRepository;

    // ── GET: listado ────────────────────────────────────
    @GetMapping
    public String listarEmpresas(Model model) {
        List<EmpresaRol> empresas = empresaRepository.findAll();

        // Usar HashMap normal para permitir valores null
        Map<Integer, GestorRol> gestoresPorEmpresa = new HashMap<>();
        for (EmpresaRol e : empresas) {
            List<GestorRol> gestores = gestorRepository.findByIdEmpresaAndActivoTrue(e.getIdEmpresa());
            gestoresPorEmpresa.put(e.getIdEmpresa(), gestores.isEmpty() ? null : gestores.get(0));
        }

        model.addAttribute("empresas", empresas);
        model.addAttribute("gestoresPorEmpresa", gestoresPorEmpresa);
        return "administracion/gestionEmpresas";
    }

    // ── GET: formulario de creación ─────────────────────
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("gestores", gestorRepository.findByActivoTrue());
        return "administracion/crearEmpresa";
    }

    // ── POST: guardar nueva empresa ─────────────────────
    @PostMapping("/crear")
    public String crearEmpresa(@RequestParam String nombre,
                               @RequestParam String cif,
                               @RequestParam String direccion,
                               RedirectAttributes redirectAttributes) {
        if (empresaRepository.findByCif(cif).isPresent()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ya existe una empresa con el CIF: " + cif);
            return "redirect:/admin/empresas/nueva";
        }
        if (empresaRepository.findByNombre(nombre).isPresent()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ya existe una empresa con el nombre: " + nombre);
            return "redirect:/admin/empresas/nueva";
        }
        try {
            EmpresaRol empresa = new EmpresaRol();
            empresa.setNombre(nombre);
            empresa.setCif(cif);
            empresa.setDireccion(direccion);
            empresaRepository.save(empresa);
            redirectAttributes.addFlashAttribute("mensajeExito", "Empresa creada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al crear la empresa: " + e.getMessage());
            return "redirect:/admin/empresas/nueva";
        }
        return "redirect:/admin/empresas";
    }

    // ── GET: formulario de edición ──────────────────────
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            EmpresaRol empresa = empresaRepository.findById(id).orElseThrow();
            List<GestorRol> gestoresEmpresa = gestorRepository.findByIdEmpresaAndActivoTrue(id);
            GestorRol gestorPrincipal = gestoresEmpresa.isEmpty() ? null : gestoresEmpresa.get(0);

            model.addAttribute("empresa", empresa);
            model.addAttribute("gestoresEmpresa", gestoresEmpresa);
            model.addAttribute("gestorPrincipal", gestorPrincipal);
            return "administracion/editarEmpresa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Empresa no encontrada.");
            return "redirect:/admin/empresas";
        }
    }

    // ── POST: actualizar empresa ────────────────────────
    @PostMapping("/actualizar/{id}")
    public String actualizarEmpresa(@PathVariable int id,
                                    @RequestParam String nombre,
                                    @RequestParam String cif,
                                    @RequestParam String direccion,
                                    RedirectAttributes redirectAttributes) {
        try {
            EmpresaRol empresa = empresaRepository.findById(id).orElseThrow();

            Optional<EmpresaRol> porCif = empresaRepository.findByCif(cif);
            if (porCif.isPresent() && porCif.get().getIdEmpresa() != id) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Ya existe una empresa con el CIF: " + cif);
                return "redirect:/admin/empresas/editar/" + id;
            }

            Optional<EmpresaRol> porNombre = empresaRepository.findByNombre(nombre);
            if (porNombre.isPresent() && porNombre.get().getIdEmpresa() != id) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Ya existe una empresa con el nombre: " + nombre);
                return "redirect:/admin/empresas/editar/" + id;
            }

            empresa.setNombre(nombre);
            empresa.setCif(cif);
            empresa.setDireccion(direccion);
            empresaRepository.save(empresa);
            redirectAttributes.addFlashAttribute("mensajeExito", "Empresa actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
            return "redirect:/admin/empresas/editar/" + id;
        }
        return "redirect:/admin/empresas";
    }
}