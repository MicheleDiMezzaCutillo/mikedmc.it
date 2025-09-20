package it.mikedmc.repository;

import it.mikedmc.model.Documentation;
import it.mikedmc.model.DocumentationContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
    public Documentation findByCode(String code);
    List<Documentation> findByIsPublicTrueAndIsActiveTrue();
}
