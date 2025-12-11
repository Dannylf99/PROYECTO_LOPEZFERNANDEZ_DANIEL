package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.NotificacionRol;

public interface NotificacionRepository extends JpaRepository<NotificacionRol, Integer> {
}
