package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Empresa;

@Entity
@Table(name = "empresas")
public class EmpresaRol extends Empresa {

    public EmpresaRol() {
        super();
    }

    public EmpresaRol(int idEmpresa, String nombre, String direccion, String telefono, String email) {
        super(idEmpresa, nombre, direccion, telefono, email);
    }

    @Override
    public void gestionarPractica() {
        // Lógica específica del rol Empresa
    }
}
