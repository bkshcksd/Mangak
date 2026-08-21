package io.virinchi.mangak.repository;

import io.virinchi.mangak.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository
        extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserIdOrderByIdDesc(Long userId);

    Optional<Bookmark> findByUserIdAndMangaId(
            Long userId,
            Long mangaId
    );

    boolean existsByUserIdAndMangaId(
            Long userId,
            Long mangaId
    );

    void deleteByUserIdAndMangaId(
            Long userId,
            Long mangaId
    );
}