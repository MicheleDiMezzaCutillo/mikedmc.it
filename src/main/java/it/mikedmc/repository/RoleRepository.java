package it.mikedmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByDescription(String description);
    
    List<Role> findTop3ByOrderByIdDesc();
}