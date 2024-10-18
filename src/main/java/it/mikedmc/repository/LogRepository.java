package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.Log;


public interface LogRepository extends JpaRepository<Log, Integer> {
}
