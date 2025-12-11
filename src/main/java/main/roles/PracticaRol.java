package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Alumno;
import main.entities.Empresa;
import main.entities.Practica;

@Entity
@Table(name = "practicas")
public class PracticaRol extends Practica {

    public PracticaRol() {
        super();
    }

    public PracticaRol(int idPractica, Alumno alumno, Empresa empresa, java.sql.Date fechaInicio, java.sql.Date fechaFin, int horasTotales) {
        super(idPractica, alumno, empresa, fechaInicio, fechaFin, horasTotales);
    }

    @Override
    public void iniciarPractica() {
        // Lógica específica para iniciar la práctica
    }

    @Override
    public void finalizarPractica() {
        // Lógica específica para finalizar la práctica
    }
}

