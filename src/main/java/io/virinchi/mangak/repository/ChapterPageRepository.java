package io.virinchi.mangak.repository;

import io.virinchi.mangak.model.ChapterPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterPageRepository
        extends JpaRepository<ChapterPage, Long> {

    List<ChapterPage>
    findByChapterIdOrderByPageNumberAsc(Long chapterId);

    boolean existsByChapterIdAndPageNumber(
            Long chapterId,
            Integer pageNumber
    );
}