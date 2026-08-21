package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.ChapterPage;
import io.virinchi.mangak.repository.ChapterPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterPageService {

    private final ChapterPageRepository chapterPageRepository;

    public ChapterPageService(
            ChapterPageRepository chapterPageRepository
    ) {
        this.chapterPageRepository = chapterPageRepository;
    }


    // Get all pages belonging to a chapter
    public List<ChapterPage> getPagesByChapter(
            Long chapterId
    ) {
        return chapterPageRepository
                .findByChapterIdOrderByPageNumberAsc(
                        chapterId
                );
    }


    // Get page by ID
    public Optional<ChapterPage> getPageById(
            Long id
    ) {
        return chapterPageRepository.findById(id);
    }


    // Save page
    public ChapterPage savePage(
            ChapterPage page
    ) {
        return chapterPageRepository.save(page);
    }


    // Delete page
    public void deletePage(
            Long id
    ) {
        chapterPageRepository.deleteById(id);
    }


    // Check page number
    public boolean pageExists(
            Long chapterId,
            Integer pageNumber
    ) {
        return chapterPageRepository
                .existsByChapterIdAndPageNumber(
                        chapterId,
                        pageNumber
                );
    }


    // =========================================
    // UPLOAD MULTIPLE CHAPTER PAGES
    // =========================================

    public void uploadPages(
            Chapter chapter,
            MultipartFile[] files
    ) throws IOException {

        List<ChapterPage> existingPages =
                getPagesByChapter(
                        chapter.getId()
                );

        int pageNumber =
                existingPages.size() + 1;


        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                continue;
            }

            String contentType =
                    file.getContentType();


            // Only allow images
            if (
                    contentType == null ||
                            !contentType.startsWith("image/")
            ) {
                continue;
            }


            ChapterPage page =
                    new ChapterPage();

            page.setChapter(chapter);

            page.setPageNumber(
                    pageNumber
            );

            page.setImageData(
                    file.getBytes()
            );

            page.setContentType(
                    contentType
            );


            chapterPageRepository.save(page);

            pageNumber++;
        }
    }
}