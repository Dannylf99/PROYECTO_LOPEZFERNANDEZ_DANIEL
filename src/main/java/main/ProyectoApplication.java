package main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import main.repositories.AlumnoRepository;

@SpringBootApplication
public class ProyectoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(AlumnoRepository alumnoRepo) {
        return args -> alumnoRepo.findAll().forEach(System.out::println);
    }
}
