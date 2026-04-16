package main;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProyectoLopezfernandezDanielApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoLopezfernandezDanielApplication.class, args);
    }
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        System.out.println("HASH: " + passwordEncoder.encode("Prueba123!"));
    }
}
