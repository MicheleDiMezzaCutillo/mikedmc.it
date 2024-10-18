package it.mikedmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.mikedmc.model.PageView;
import jakarta.transaction.Transactional;

public interface PageViewRepository extends JpaRepository<PageView, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO page_view (page_url, view_count) VALUES (:pageUrl, 1) " +
                   "ON DUPLICATE KEY UPDATE view_count = view_count + 1", 
           nativeQuery = true)
    void upsertPageView(@Param("pageUrl") String pageUrl);
    
    // Query per ottenere i 3 record con più viewCount
    @Query(value = "SELECT * FROM page_view ORDER BY view_count DESC LIMIT 3", nativeQuery = true)
    List<PageView> findTop3ByViewCountDesc();

    // Query per ottenere i 3 record con meno viewCount
    @Query(value = "SELECT * FROM page_view ORDER BY view_count ASC LIMIT 3", nativeQuery = true)
    List<PageView> findTop3ByViewCountAsc();

    // Query per ottenere la somma totale dei viewCount
    @Query(value = "SELECT SUM(view_count) FROM page_view", nativeQuery = true)
    Long getTotalViewCount();
}
