package main.dtos;

public class CrearUsuarioDTO {

    private String nombre;
    private String apellidos;
    private String email;
    private String contrasenya;
    private String tipo;

    // Para ALUMNO
    private Integer empresaAsignada;

    // Para GESTOR
    private Integer idEmpresa;

    // Constructor vacío (IMPORTANTE para Spring)
    public CrearUsuarioDTO() {
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getEmpresaAsignada() {
        return empresaAsignada;
    }

    public void setEmpresaAsignada(Integer empresaAsignada) {
        this.empresaAsignada = empresaAsignada;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public String toString() {
        return "CrearUsuarioDTO{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", tipo='" + tipo + '\'' +
                ", empresaAsignada=" + empresaAsignada +
                ", idEmpresa=" + idEmpresa +
                '}';
    }
}