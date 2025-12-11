package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Documento;
import main.enums.Estado;
import java.time.LocalDate;

@Entity
@Table(name = "documentos")
public class DocumentoRol extends Documento {

    public DocumentoRol() {
        super();
    }

    public DocumentoRol(int idDocumento, int idUsuario, String tipo, LocalDate fechaSubida, Estado estado, String rutaArchivo) {
        super(idDocumento, idUsuario, tipo, fechaSubida, estado, rutaArchivo);
    }

    @Override
    public void procesarDocumento() {
        // Lógica específica para procesar el documento
    }
}
