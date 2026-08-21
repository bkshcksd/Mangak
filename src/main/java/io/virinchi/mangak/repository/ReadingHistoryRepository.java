package io.virinchi.mangak.repository;

import io.virinchi.mangak.model.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingHistoryRepository
        extends JpaRepository<ReadingHistory, Long> {


    // =========================================
    // GET USER HISTORY
    // NEWEST FIRST
    // =========================================

    List<ReadingHistory>
    findByUserIdOrderByLastReadAtDesc(
            Long userId
    );


    // =========================================
    // GET HISTORY FOR SPECIFIC MANGA
    // =========================================

    Optional<ReadingHistory>
    findByUserIdAndMangaId(
            Long userId,
            Long mangaId
    );


    // =========================================
    // CHECK HISTORY
    // =========================================

    boolean existsByUserIdAndMangaId(
            Long userId,
            Long mangaId
    );


    // =========================================
    // DELETE ONE HISTORY ITEM
    // =========================================

    void deleteByUserIdAndMangaId(
            Long userId,
            Long mangaId
    );


    // =========================================
    // DELETE ALL USER HISTORY
    // =========================================

    void deleteByUserId(
            Long userId
    );
}