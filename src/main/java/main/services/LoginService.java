package main.services;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import main.roles.*;

@Service
public class LoginService {

    private final AlumnoService alumnoService;
    private final CoordinadorService coordinadorService;
    private final GestorService gestorService;
    private final AdministracionService adminService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(AlumnoService alumnoService,
                        CoordinadorService coordinadorService,
                        GestorService gestorService,
                        AdministracionService adminService,
                        PasswordEncoder passwordEncoder) {
        this.alumnoService = alumnoService;
        this.coordinadorService = coordinadorService;
        this.gestorService = gestorService;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    public Object login(String email, String contrasenya) {
        System.out.println("🔍 LoginService - Buscando usuario: " + email);

        Object usuario;

        // Buscar alumno por email
        usuario = alumnoService.findByEmail(email);
        if (usuario != null) {
            AlumnoRol alumno = (AlumnoRol) usuario;
            if (passwordEncoder.matches(contrasenya, alumno.getContrasenya())) {
                System.out.println("✅ Alumno encontrado y contraseña correcta!");
                return alumno;
            }
            System.out.println("❌ Alumno encontrado pero contraseña incorrecta");
        }

        // Buscar coordinador por email
        usuario = coordinadorService.findByEmail(email);
        if (usuario != null) {
            CoordinadorRol coordinador = (CoordinadorRol) usuario;
            if (passwordEncoder.matches(contrasenya, coordinador.getContrasenya())) {
                System.out.println("✅ Coordinador encontrado y contraseña correcta!");
                return coordinador;
            }
            System.out.println("❌ Coordinador encontrado pero contraseña incorrecta");
        }

        // Buscar gestor por email
        usuario = gestorService.findByEmail(email);
        if (usuario != null) {
            GestorRol gestor = (GestorRol) usuario;
            if (passwordEncoder.matches(contrasenya, gestor.getContrasenya())) {
                System.out.println("✅ Gestor encontrado y contraseña correcta!");
                return gestor;
            }
            System.out.println("❌ Gestor encontrado pero contraseña incorrecta");
        }

        // Buscar admin por email
        usuario = adminService.findByEmail(email);
        if (usuario != null) {
            AdministracionRol admin = (AdministracionRol) usuario;
            if (passwordEncoder.matches(contrasenya, admin.getContrasenya())) {
                System.out.println("✅ Admin encontrado y contraseña correcta!");
                return admin;
            }
            System.out.println("❌ Admin encontrado pero contraseña incorrecta");
        }

        System.out.println("❌ Usuario no encontrado con email: " + email);
        return null;
    }
}