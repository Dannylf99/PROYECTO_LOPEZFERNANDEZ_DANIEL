package main.services;

import org.springframework.stereotype.Service;
import main.roles.*;

@Service
public class LoginService {

    private final AlumnoService alumnoService;
    private final CoordinadorService coordinadorService;
    private final GestorService gestorService;
    private final AdministracionService adminService;

    public LoginService(AlumnoService alumnoService,
                        CoordinadorService coordinadorService,
                        GestorService gestorService,
                        AdministracionService adminService) {
        this.alumnoService = alumnoService;
        this.coordinadorService = coordinadorService;
        this.gestorService = gestorService;
        this.adminService = adminService;
    }

    public Object login(String email, String contrasenya) {
        Object usuario;

        usuario = alumnoService.login(email, contrasenya);
        if (usuario != null) return usuario;

        usuario = coordinadorService.login(email, contrasenya);
        if (usuario != null) return usuario;

        usuario = gestorService.login(email, contrasenya);
        if (usuario != null) return usuario;

        usuario = adminService.login(email, contrasenya);
        if (usuario != null) return usuario;

        return null; // usuario no encontrado
    }
}
