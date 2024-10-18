package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}