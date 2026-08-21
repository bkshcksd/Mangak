package io.virinchi.mangak.repository;

import io.virinchi.mangak.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MangaRepository
        extends JpaRepository<Manga, Long>,
        JpaSpecificationExecutor<Manga> {

    boolean existsByTitle(String title);

    Optional<Manga> findByTitleIgnoreCase(String title);
}