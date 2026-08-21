package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.repository.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    // Get all chapters
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    // Get chapter by ID
    public Optional<Chapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }

    // Get chapters belonging to a manga
    public List<Chapter> getChaptersByManga(Long mangaId) {
        return chapterRepository
                .findByMangaIdOrderByChapterNumberAsc(mangaId);
    }

    // Save chapter
    public Chapter saveChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    // Delete chapter
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }

    // Check if chapter already exists
    public boolean chapterExists(Long mangaId, Integer chapterNumber) {
        return chapterRepository
                .existsByMangaIdAndChapterNumber(
                        mangaId,
                        chapterNumber
                );
    }
}