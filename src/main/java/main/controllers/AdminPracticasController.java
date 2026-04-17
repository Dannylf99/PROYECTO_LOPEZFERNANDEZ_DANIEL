package main.controllers;

import main.repositories.*;
import main.roles.*;
import main.services.PracticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;

@Controller
@RequestMapping("/admin/practicas")
public class AdminPracticasController {

    @Autowired private PracticaService practicaService;
    @Autowired private AlumnoRepository alumnoRepository;
    @Autowired private CoordinadorRepository coordinadorRepository;
    @Autowired private GestorRepository gestorRepository;
    @Autowired private EmpresaRepository empresaRepository;

    @GetMapping
    public String listarPracticas(Model model) {
        // Mostrar todas excepto CANCELADAS
        model.addAttribute("practicas", practicaService.getAllPracticas().stream()
                .filter(p -> p.getEstado() != PracticaRol.Estado.CANCELADA)
                .toList());
        return "administracion/gestionPracticas";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("alumnos", alumnoRepository.findByActivoTrue());
        model.addAttribute("coordinadores", coordinadorRepository.findByActivoTrue());
        model.addAttribute("gestores", gestorRepository.findByActivoTrue());
        model.addAttribute("empresas", empresaRepository.findAll());
        return "administracion/crearPractica";
    }

    @PostMapping("/crear")
    public String crearPractica(@RequestParam int idAlumno,
                                @RequestParam int idCoordinador,
                                @RequestParam int idEmpresa,
                                @RequestParam String fechaInicio,
                                @RequestParam String fechaFin,
                                @RequestParam int horasTotales,
                                RedirectAttributes redirectAttributes) {
        try {
            AlumnoRol alumno = alumnoRepository.findById(idAlumno).orElseThrow();
            if (practicaService.tienesPracticaEnCurso(alumno)) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "El alumno " + alumno.getNombre() + " " + alumno.getApellidos() +
                                " ya tiene una práctica activa o preparada.");
                return "redirect:/admin/practicas/nueva";
            }
            CoordinadorRol coordinador = coordinadorRepository.findById(idCoordinador).orElseThrow();
            EmpresaRol empresa = empresaRepository.findById(idEmpresa).orElseThrow();

            PracticaRol practica = new PracticaRol();
            practica.setAlumno(alumno);
            practica.setCoordinador(coordinador);
            practica.setEmpresa(empresa);
            practica.setFechaInicio(Date.valueOf(fechaInicio));
            practica.setFechaFin(Date.valueOf(fechaFin));
            practica.setHorasTotales(horasTotales);
            practica.setHorasHechas(0);
            practica.setEstado(PracticaRol.Estado.PREPARADA);

            practicaService.savePractica(practica);
            redirectAttributes.addFlashAttribute("mensajeExito", "Práctica creada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear la práctica: " + e.getMessage());
            return "redirect:/admin/practicas/nueva";
        }
        return "redirect:/admin/practicas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try {
            PracticaRol practica = practicaService.getAllPracticas().stream()
                    .filter(p -> p.getIdPractica() == id).findFirst().orElseThrow();
            model.addAttribute("practica", practica);
            model.addAttribute("alumnos", alumnoRepository.findByActivoTrue());
            model.addAttribute("coordinadores", coordinadorRepository.findByActivoTrue());
            model.addAttribute("empresas", empresaRepository.findAll());
            model.addAttribute("estados", new PracticaRol.Estado[]{
                    PracticaRol.Estado.PREPARADA, PracticaRol.Estado.ACTIVA,
                    PracticaRol.Estado.PARADA, PracticaRol.Estado.FINALIZADA
            });
            return "administracion/editarPractica";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Práctica no encontrada.");
            return "redirect:/admin/practicas";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPractica(@PathVariable int id,
                                     @RequestParam int idAlumno,
                                     @RequestParam int idCoordinador,
                                     @RequestParam int idEmpresa,
                                     @RequestParam String fechaInicio,
                                     @RequestParam String fechaFin,
                                     @RequestParam int horasTotales,
                                     @RequestParam String estado,
                                     RedirectAttributes redirectAttributes) {
        try {
            PracticaRol practica = practicaService.getAllPracticas().stream()
                    .filter(p -> p.getIdPractica() == id).findFirst().orElseThrow();

            AlumnoRol alumno = alumnoRepository.findById(idAlumno).orElseThrow();
            if (practica.getAlumno().getIdUsuario() != idAlumno
                    && practicaService.tienesPracticaEnCurso(alumno)) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "El alumno ya tiene una práctica activa o preparada.");
                return "redirect:/admin/practicas/editar/" + id;
            }

            practica.setAlumno(alumno);
            practica.setCoordinador(coordinadorRepository.findById(idCoordinador).orElseThrow());
            practica.setEmpresa(empresaRepository.findById(idEmpresa).orElseThrow());
            practica.setFechaInicio(Date.valueOf(fechaInicio));
            practica.setFechaFin(Date.valueOf(fechaFin));
            practica.setHorasTotales(horasTotales);
            practica.setEstado(PracticaRol.Estado.valueOf(estado));

            practicaService.savePractica(practica);
            redirectAttributes.addFlashAttribute("mensajeExito", "Práctica actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/admin/practicas";
    }

    // ── Acciones de estado ──────────────────────────────
    @PostMapping("/parar/{id}")
    public String parar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            practicaService.parar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Práctica pausada. Alumno notificado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/admin/practicas";
    }

    @PostMapping("/reanudar/{id}")
    public String reanudar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            practicaService.reanudar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Práctica reanudada. Alumno notificado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/admin/practicas";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            practicaService.cancelar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Práctica cancelada. Alumno notificado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/admin/practicas";
    }
}