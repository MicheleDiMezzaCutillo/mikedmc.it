package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.mikedmc.model.Changelog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChangelogRepository extends JpaRepository<Changelog, Integer> {
    // Trova il changelog per data
    Optional<Changelog> findByDate(LocalDate date);

    // Trova il changelog più recente
    Optional<Changelog> findTopByOrderByDateDesc();
    
    @Query("SELECT c FROM Changelog c WHERE c.date = (SELECT MAX(c2.date) FROM Changelog c2)")
    Optional<Changelog> findMostRecentChangelog();

    List<Changelog> findAllByOrderByDateDesc();
}
