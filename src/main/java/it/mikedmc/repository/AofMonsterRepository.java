package it.mikedmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.AofMonster;

public interface AofMonsterRepository extends JpaRepository<AofMonster,Long>{
    List<AofMonster> findAllByOrderByRegioneAsc();

}
