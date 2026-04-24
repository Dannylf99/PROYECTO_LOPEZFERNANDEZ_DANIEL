package main.repositories;

import main.roles.AlumnoRol;
import main.roles.DocumentoRol;
import main.roles.PracticaRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<DocumentoRol, Integer> {

    List<DocumentoRol> findByAlumno(AlumnoRol alumno);

    List<DocumentoRol> findByPractica(PracticaRol practica);

    List<DocumentoRol> findByEstado(DocumentoRol.Estado estado);

    @Query("SELECT d FROM DocumentoRol d WHERE d.practica.empresa.idEmpresa = :idEmpresa " +
            "AND d.estado = 'PENDIENTE_FIRMA_GESTOR'")
    List<DocumentoRol> findPendientesFirmaGestor(@Param("idEmpresa") int idEmpresa);

    @Query("SELECT d FROM DocumentoRol d WHERE d.practica.coordinador.idUsuario = :idCoordinador " +
            "AND d.estado = 'PENDIENTE_FIRMA_COORDINADOR'")
    List<DocumentoRol> findPendientesFirmaCoordinador(@Param("idCoordinador") int idCoordinador);

    @Query("SELECT d FROM DocumentoRol d WHERE d.practica.coordinador.idUsuario = :idCoordinador")
    List<DocumentoRol> findByCoordinadorId(@Param("idCoordinador") int idCoordinador);

    @Query("SELECT d FROM DocumentoRol d WHERE d.practica.empresa.idEmpresa = :idEmpresa")
    List<DocumentoRol> findByEmpresaId(@Param("idEmpresa") int idEmpresa);

    @Query("SELECT d FROM DocumentoRol d WHERE d.alumno.idUsuario = :idAlumno " +
            "AND d.estado = 'PENDIENTE_FIRMA_ALUMNO'")
    List<DocumentoRol> findPendientesFirmaAlumno(@Param("idAlumno") int idAlumno);

    @Query("SELECT d FROM DocumentoRol d WHERE " +
            "(:tipo IS NULL OR d.tipo = :tipo) AND " +
            "(:estado IS NULL OR d.estado = :estado) AND " +
            "(:idAlumno IS NULL OR d.alumno.idUsuario = :idAlumno) AND " +
            "(:idEmpresa IS NULL OR d.practica.empresa.idEmpresa = :idEmpresa)")
    List<DocumentoRol> findWithFiltros(
            @Param("tipo")     DocumentoRol.Tipo tipo,
            @Param("estado")   DocumentoRol.Estado estado,
            @Param("idAlumno") Integer idAlumno,
            @Param("idEmpresa") Integer idEmpresa);
}