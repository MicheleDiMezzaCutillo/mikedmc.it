package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.Advertising;

public interface AdvertisingRepository extends JpaRepository<Advertising, Integer> {

}
