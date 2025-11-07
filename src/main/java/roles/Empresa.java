package roles;

public class Empresa {
private int idEmpresa;
private String nombre;
private String cif;
private String direccion;



public Empresa(int idEmpresa, String nombre, String cif, String direccion) {
	super();
	this.idEmpresa = idEmpresa;
	this.nombre = nombre;
	this.cif = cif;
	this.direccion = direccion;
}
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
public void obtenerAlumnos() {
	
}

}
