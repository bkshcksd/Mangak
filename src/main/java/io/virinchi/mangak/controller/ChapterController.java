package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.ChapterPage;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.service.ChapterPageService;
import io.virinchi.mangak.service.ChapterService;
import io.virinchi.mangak.service.ReadingHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ChapterController {

    private final ChapterService chapterService;
    private final ChapterPageService chapterPageService;
    private final ReadingHistoryService readingHistoryService;


    public ChapterController(
            ChapterService chapterService,
            ChapterPageService chapterPageService,
            ReadingHistoryService readingHistoryService
    ) {
        this.chapterService = chapterService;
        this.chapterPageService = chapterPageService;
        this.readingHistoryService = readingHistoryService;
    }


    // =========================================
    // CHAPTER READER
    // =========================================

    @GetMapping("/chapter/{id}")
    public String readChapter(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {

        Optional<Chapter> chapterOptional =
                chapterService.getChapterById(id);


        if (chapterOptional.isEmpty()) {

            return "redirect:/mangak";
        }


        Chapter chapter =
                chapterOptional.get();


        Manga manga =
                chapter.getManga();


        // =========================================
        // SAVE / UPDATE READING HISTORY
        // =========================================

        Long userId =
                (Long) session.getAttribute("userId");


        if (userId != null) {

            readingHistoryService.recordReading(
                    userId,
                    chapter
            );
        }


        // =========================================
        // ALL CHAPTERS BELONGING TO MANGA
        // =========================================

        List<Chapter> chapters =
                chapterService.getChaptersByManga(
                        manga.getId()
                );


        // =========================================
        // ACTUAL PAGE IMAGES
        // =========================================

        List<ChapterPage> pages =
                chapterPageService.getPagesByChapter(
                        chapter.getId()
                );


        // =========================================
        // PREVIOUS / NEXT
        // =========================================

        Chapter previousChapter = null;
        Chapter nextChapter = null;


        for (int i = 0; i < chapters.size(); i++) {

            Chapter current =
                    chapters.get(i);


            if (current.getId().equals(id)) {

                if (i > 0) {

                    previousChapter =
                            chapters.get(i - 1);
                }


                if (i < chapters.size() - 1) {

                    nextChapter =
                            chapters.get(i + 1);
                }


                break;
            }
        }


        // =========================================
        // MODEL
        // =========================================

        model.addAttribute(
                "chapter",
                chapter
        );


        model.addAttribute(
                "manga",
                manga
        );


        model.addAttribute(
                "chapters",
                chapters
        );


        model.addAttribute(
                "pages",
                pages
        );


        model.addAttribute(
                "previousChapter",
                previousChapter
        );


        model.addAttribute(
                "nextChapter",
                nextChapter
        );


        model.addAttribute(
                "username",
                session.getAttribute("username")
        );


        model.addAttribute(
                "role",
                session.getAttribute("role")
        );


        return "chapter-reader";
    }


    // =========================================
    // SERVE PAGE IMAGE FROM DATABASE
    // =========================================

    @GetMapping("/chapter-page/{id}/image")
    public ResponseEntity<byte[]> getPageImage(
            @PathVariable Long id
    ) {

        Optional<ChapterPage> pageOptional =
                chapterPageService.getPageById(id);


        if (pageOptional.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        ChapterPage page =
                pageOptional.get();


        MediaType mediaType;


        try {

            mediaType =
                    MediaType.parseMediaType(
                            page.getContentType()
                    );

        } catch (Exception e) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }


        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(page.getImageData());
    }
}