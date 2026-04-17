package main.dtos;

import jakarta.validation.constraints.*;

public class CrearUsuarioDTO {

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Formato DNI inválido")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    private String contrasenya;

    // Alumno
    private Integer empresaAsignada;
    private String curso;
    private String horario;

    // Gestor
    private Integer idEmpresa;

    // Getters y Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public Integer getEmpresaAsignada() { return empresaAsignada; }
    public void setEmpresaAsignada(Integer empresaAsignada) { this.empresaAsignada = empresaAsignada; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public Integer getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Integer idEmpresa) { this.idEmpresa = idEmpresa; }
}