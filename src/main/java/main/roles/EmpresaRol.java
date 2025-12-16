package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "empresa")
public class EmpresaRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private int idEmpresa;

    private String nombre;
    
    private String cif;
    
    private String direccion;

    public EmpresaRol() {}

    public EmpresaRol(int idEmpresa, String nombre, String cif, String direccion) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
    }

    // Getters y Setters
    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Método específico
    public void gestionarPractica() {
        System.out.println("Empresa " + nombre + " gestionando práctica");
    }
}