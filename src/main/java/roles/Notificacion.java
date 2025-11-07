package roles;

import java.sql.Date;

public class Notificacion {
	
private int idNotificacion;
private String mensaje;
private Date fecha;
private boolean leida;

public Notificacion(int idNotificacion, String mensaje, Date fecha, boolean leida) {
	super();
	this.idNotificacion = idNotificacion;
	this.mensaje = mensaje;
	this.fecha = fecha;
	this.leida = leida;
}

public int getIdNotificacion() {
	return idNotificacion;
}

public void setIdNotificacion(int idNotificacion) {
	this.idNotificacion = idNotificacion;
}

public String getMensaje() {
	return mensaje;
}

public void setMensaje(String mensaje) {
	this.mensaje = mensaje;
}

public Date getFecha() {
	return fecha;
}

public void setFecha(Date fecha) {
	this.fecha = fecha;
}

public boolean isLeida() {
	return leida;
}

public void setLeida(boolean leida) {
	this.leida = leida;
}

public void enviar() {
	
}

public void marcarComoLeida() {
	
}

}
