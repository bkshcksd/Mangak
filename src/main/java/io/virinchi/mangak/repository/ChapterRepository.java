package io.virinchi.mangak.repository;

import io.virinchi.mangak.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository
        extends JpaRepository<Chapter, Long> {

    List<Chapter> findByMangaIdOrderByChapterNumberAsc(
            Long mangaId
    );

    boolean existsByMangaIdAndChapterNumber(
            Long mangaId,
            Integer chapterNumber
    );
}