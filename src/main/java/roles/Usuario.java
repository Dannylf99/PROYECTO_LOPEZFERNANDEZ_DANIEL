package roles;

public abstract class Usuario {
private int idUsuario;
private String nombre;
private String apellidos;
private String email;
private String contraseña;

public abstract void iniciarSesion();
public abstract void cerrarSesion();

public Usuario(int idUsuario, String nombre, String apellidos, String email, String contraseña) {
	super();
	this.idUsuario = idUsuario;
	this.nombre = nombre;
	this.apellidos = apellidos;
	this.email = email;
	this.contraseña = contraseña;
}


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
public String getContraseña() {
	return contraseña;
}
public void setContraseña(String contraseña) {
	this.contraseña = contraseña;
}
public int getIdUsuario() {
	return idUsuario;
}
public void setIdUsuario(int idUsuario) {
	this.idUsuario = idUsuario;
}
}
