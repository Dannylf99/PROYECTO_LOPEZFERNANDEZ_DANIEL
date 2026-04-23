package main.services;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import main.repositories.AlumnoRepository;
import main.repositories.PracticaRepository;
import main.repositories.RegistroHorasRepository;
import main.roles.AlumnoRol;
import main.roles.PracticaRol;
import main.roles.RegistroHorasRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InformeService {

    @Autowired private AlumnoRepository alumnoRepository;
    @Autowired private PracticaRepository practicaRepository;
    @Autowired private RegistroHorasRepository registroHorasRepository;

    // ─── INFORME 1: Listado de alumnos ───────────────────────────────────────
    public byte[] generarInformeAlumnos(String curso, String estado,
                                        String fechaDesde, String fechaHasta) throws Exception {

        List<AlumnoRol> alumnos = alumnoRepository.findAll();

        // Filtro por curso
        if (curso != null && !curso.isEmpty()) {
            AlumnoRol.Curso cursoEnum = AlumnoRol.Curso.valueOf(curso);
            alumnos = alumnos.stream()
                    .filter(a -> cursoEnum.equals(a.getCurso()))
                    .collect(Collectors.toList());
        }

        LocalDate desde = parseFecha(fechaDesde);
        LocalDate hasta = parseFecha(fechaHasta);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(new PdfDocument(new PdfWriter(baos)));

        doc.add(new Paragraph("Informe de alumnos — Estado de prácticas")
                .setBold().setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(4));
        doc.add(new Paragraph("Fecha de generación: " + LocalDate.now())
                .setFontSize(10).setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6));

        String filtrosAplicados = "Filtros — Curso: " + nvl(curso, "Todos")
                + " | Estado: " + nvl(estado, "Todos")
                + " | Desde: " + (desde != null ? desde : "-")
                + " | Hasta: " + (hasta != null ? hasta : "-");
        doc.add(new Paragraph(filtrosAplicados)
                .setFontSize(9).setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{1.5f, 2, 3, 1.5f, 2}))
                .setWidth(UnitValue.createPercentValue(100));

        for (String col : new String[]{"DNI", "Nombre", "Apellidos", "Curso", "Estado prácticas"}) {
            tabla.addHeaderCell(new Cell().add(new Paragraph(col).setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        }

        for (AlumnoRol a : alumnos) {
            // Usar query por ID para evitar problemas de contexto JPA
            List<PracticaRol> practicas = practicaRepository.findByAlumnoId(a.getIdUsuario());

            // Filtro por estado
            if (estado != null && !estado.isEmpty()) {
                PracticaRol.Estado estadoEnum = PracticaRol.Estado.valueOf(estado);
                practicas = practicas.stream()
                        .filter(p -> estadoEnum.equals(p.getEstado()))
                        .collect(Collectors.toList());
            }

            // Filtro por fechas
            if (desde != null) {
                practicas = practicas.stream()
                        .filter(p -> p.getFechaInicio() != null &&
                                !p.getFechaInicio().toLocalDate().isBefore(desde))
                        .collect(Collectors.toList());
            }
            if (hasta != null) {
                practicas = practicas.stream()
                        .filter(p -> p.getFechaInicio() != null &&
                                !p.getFechaInicio().toLocalDate().isAfter(hasta))
                        .collect(Collectors.toList());
            }

            // Si hay filtro de estado/fecha y no quedan prácticas, saltar este alumno
            if (practicas.isEmpty() && (tieneValor(estado) || desde != null || hasta != null)) continue;

            String estadoPractica = practicas.isEmpty()
                    ? "Sin práctica"
                    : practicas.get(0).getEstado().toString();

            tabla.addCell(nvl(a.getDni(), "-"));
            tabla.addCell(nvl(a.getNombre(), "-"));
            tabla.addCell(nvl(a.getApellidos(), "-"));
            tabla.addCell(a.getCurso() != null ? a.getCurso().toString() : "-");
            tabla.addCell(estadoPractica);
        }

        doc.add(tabla);
        doc.close();
        return baos.toByteArray();
    }

    // ─── INFORME 2: Horas de un alumno ───────────────────────────────────────
    public byte[] generarInformeHoras(int idAlumno, String estadoHoras,
                                      String fechaDesde, String fechaHasta) throws Exception {

        AlumnoRol alumno = alumnoRepository.findById(idAlumno).orElseThrow();
        // Buscar por ID para evitar problemas de contexto JPA
        List<PracticaRol> practicas = practicaRepository.findByAlumnoId(idAlumno);

        LocalDate desde = parseFecha(fechaDesde);
        LocalDate hasta = parseFecha(fechaHasta);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(new PdfDocument(new PdfWriter(baos)));

        doc.add(new Paragraph("Informe de horas — " + alumno.getNombre() + " " + alumno.getApellidos())
                .setBold().setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(4));
        doc.add(new Paragraph("Fecha de generación: " + LocalDate.now())
                .setFontSize(10).setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

        if (!practicas.isEmpty()) {
            PracticaRol practica = practicas.get(0);

            doc.add(new Paragraph("Empresa: " + practica.getEmpresa().getNombre()).setFontSize(11));
            doc.add(new Paragraph("Coordinador: " + practica.getCoordinador().getNombre()
                    + " " + practica.getCoordinador().getApellidos()).setFontSize(11));
            doc.add(new Paragraph("Estado práctica: " + practica.getEstado()).setFontSize(11));
            doc.add(new Paragraph("Horas totales: " + practica.getHorasTotales()).setFontSize(11));
            doc.add(new Paragraph("Horas realizadas: " + practica.getHorasHechas())
                    .setFontSize(11).setMarginBottom(16));

            Table tabla = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 1.5f, 2}))
                    .setWidth(UnitValue.createPercentValue(100));

            for (String col : new String[]{"Fecha", "Entrada", "Salida", "Horas", "Estado"}) {
                tabla.addHeaderCell(new Cell().add(new Paragraph(col).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            List<RegistroHorasRol> registros = registroHorasRepository.findByPractica(practica);

            if (tieneValor(estadoHoras)) {
                RegistroHorasRol.Estado est = RegistroHorasRol.Estado.valueOf(estadoHoras);
                registros = registros.stream()
                        .filter(r -> est.equals(r.getEstado()))
                        .collect(Collectors.toList());
            }
            if (desde != null) registros = registros.stream()
                    .filter(r -> !r.getFecha().isBefore(desde)).collect(Collectors.toList());
            if (hasta != null) registros = registros.stream()
                    .filter(r -> !r.getFecha().isAfter(hasta)).collect(Collectors.toList());

            if (registros.isEmpty()) {
                doc.add(new Paragraph("No hay registros de horas con los filtros aplicados.")
                        .setFontColor(ColorConstants.GRAY));
            } else {
                for (RegistroHorasRol r : registros) {
                    tabla.addCell(r.getFecha().toString());
                    tabla.addCell(r.getHoraInicio().toString());
                    tabla.addCell(r.getHoraFin().toString());
                    tabla.addCell(r.getHoras().toString());
                    tabla.addCell(r.getEstado().toString());
                }
                doc.add(tabla);
            }
        } else {
            doc.add(new Paragraph("El alumno no tiene prácticas registradas.")
                    .setFontColor(ColorConstants.GRAY));
        }

        doc.close();
        return baos.toByteArray();
    }

    // ─── INFORME 3: General de prácticas ─────────────────────────────────────
    public byte[] generarInformeGeneral(String curso, String estado,
                                        String fechaDesde, String fechaHasta) throws Exception {

        List<PracticaRol> practicas = practicaRepository.findAll();

        LocalDate desde = parseFecha(fechaDesde);
        LocalDate hasta = parseFecha(fechaHasta);

        if (tieneValor(estado)) {
            PracticaRol.Estado est = PracticaRol.Estado.valueOf(estado);
            practicas = practicas.stream()
                    .filter(p -> est.equals(p.getEstado()))
                    .collect(Collectors.toList());
        }
        if (tieneValor(curso)) {
            AlumnoRol.Curso cur = AlumnoRol.Curso.valueOf(curso);
            practicas = practicas.stream()
                    .filter(p -> p.getAlumno() != null && cur.equals(p.getAlumno().getCurso()))
                    .collect(Collectors.toList());
        }
        if (desde != null) practicas = practicas.stream()
                .filter(p -> p.getFechaInicio() != null &&
                        !p.getFechaInicio().toLocalDate().isBefore(desde))
                .collect(Collectors.toList());
        if (hasta != null) practicas = practicas.stream()
                .filter(p -> p.getFechaInicio() != null &&
                        !p.getFechaInicio().toLocalDate().isAfter(hasta))
                .collect(Collectors.toList());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(new PdfDocument(new PdfWriter(baos)));

        doc.add(new Paragraph("Informe general de prácticas")
                .setBold().setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(4));
        doc.add(new Paragraph("Fecha de generación: " + LocalDate.now())
                .setFontSize(10).setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6));

        String filtrosAplicados = "Filtros — Curso: " + nvl(curso, "Todos")
                + " | Estado: " + nvl(estado, "Todos")
                + " | Desde: " + (desde != null ? desde : "-")
                + " | Hasta: " + (hasta != null ? hasta : "-");
        doc.add(new Paragraph(filtrosAplicados)
                .setFontSize(9).setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

        if (practicas.isEmpty()) {
            doc.add(new Paragraph("No hay prácticas con los filtros aplicados.")
                    .setFontColor(ColorConstants.GRAY));
        } else {
            Table tabla = new Table(UnitValue.createPercentArray(new float[]{2.5f, 2.5f, 2.5f, 1.5f, 1.5f, 1.5f}))
                    .setWidth(UnitValue.createPercentValue(100));

            for (String col : new String[]{"Alumno", "Empresa", "Coordinador", "Inicio", "Fin", "Estado"}) {
                tabla.addHeaderCell(new Cell().add(new Paragraph(col).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            for (PracticaRol p : practicas) {
                tabla.addCell(p.getAlumno().getNombre() + " " + p.getAlumno().getApellidos());
                tabla.addCell(p.getEmpresa().getNombre());
                tabla.addCell(p.getCoordinador().getNombre() + " " + p.getCoordinador().getApellidos());
                tabla.addCell(p.getFechaInicio() != null ? p.getFechaInicio().toString() : "-");
                tabla.addCell(p.getFechaFin() != null ? p.getFechaFin().toString() : "-");
                tabla.addCell(p.getEstado().toString());
            }

            doc.add(tabla);
        }

        doc.close();
        return baos.toByteArray();
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────
    private LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) return null;
        try { return LocalDate.parse(fecha); } catch (Exception e) { return null; }
    }

    private boolean tieneValor(String s) {
        return s != null && !s.isEmpty();
    }

    private String nvl(String s, String defecto) {
        return (s != null && !s.isEmpty()) ? s : defecto;
    }
}