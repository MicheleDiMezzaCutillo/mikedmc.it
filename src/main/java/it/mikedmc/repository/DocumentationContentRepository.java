package it.mikedmc.repository;

import it.mikedmc.model.Documentation;
import it.mikedmc.model.DocumentationContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentationContentRepository extends JpaRepository<DocumentationContent, Long> {
    List<DocumentationContent> findByDocumentationCodeOrderByPositionAsc(String code);
}
