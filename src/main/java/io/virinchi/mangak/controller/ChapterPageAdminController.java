package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.ChapterPage;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.service.ChapterPageService;
import io.virinchi.mangak.service.ChapterService;
import io.virinchi.mangak.service.MangaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ChapterPageAdminController {

    private final ChapterService chapterService;
    private final ChapterPageService chapterPageService;
    private final MangaService mangaService;


    public ChapterPageAdminController(
            ChapterService chapterService,
            ChapterPageService chapterPageService,
            MangaService mangaService
    ) {
        this.chapterService = chapterService;
        this.chapterPageService = chapterPageService;
        this.mangaService = mangaService;
    }


    // =========================================
    // CHAPTER PAGE MANAGER
    // =========================================

    @GetMapping("/chapter/{chapterId}/pages")
    public String chapterPages(
            @PathVariable Long chapterId,
            HttpSession session,
            Model model
    ) {

        Optional<Chapter> chapterOptional =
                chapterService.getChapterById(
                        chapterId
                );


        if (chapterOptional.isEmpty()) {
            return "redirect:/mangak";
        }


        Chapter chapter =
                chapterOptional.get();


        Manga manga =
                chapter.getManga();


        // =====================================
        // GET CHAPTER PAGES
        // =====================================

        List<ChapterPage> pages =
                chapterPageService
                        .getPagesByChapter(
                                chapterId
                        );


        // =====================================
        // GET ALL MANGA
        // =====================================

        List<Manga> allManga =
                mangaService.getAllManga();


        // =====================================
        // GET CHAPTERS FOR CURRENT MANGA
        // =====================================

        List<Chapter> mangaChapters =
                chapterService
                        .getChaptersByManga(
                                manga.getId()
                        );


        // =====================================
        // SEND DATA TO HTML
        // =====================================

        model.addAttribute(
                "chapter",
                chapter
        );


        model.addAttribute(
                "manga",
                manga
        );


        model.addAttribute(
                "pages",
                pages
        );


        model.addAttribute(
                "allManga",
                allManga
        );


        model.addAttribute(
                "mangaChapters",
                mangaChapters
        );


        model.addAttribute(
                "username",
                session.getAttribute("username")
        );


        return "chapter-pages-admin";
    }


    // =========================================
    // SWITCH TO ANOTHER MANGA
    // =========================================

    @GetMapping("/manga/{mangaId}/chapters")
    public String mangaChapters(
            @PathVariable Long mangaId
    ) {

        List<Chapter> chapters =
                chapterService
                        .getChaptersByManga(
                                mangaId
                        );


        /*
         * If this manga has no chapters,
         * return to its manga details page.
         */
        if (chapters.isEmpty()) {

            return "redirect:/manga/"
                    + mangaId;
        }


        /*
         * Open the first chapter belonging
         * to the selected manga.
         */
        Chapter firstChapter =
                chapters.get(0);


        return "redirect:/admin/chapter/"
                + firstChapter.getId()
                + "/pages";
    }


    // =========================================
    // UPLOAD MULTIPLE IMAGES
    // =========================================

    @PostMapping("/chapter/{chapterId}/pages")
    public String uploadPages(
            @PathVariable Long chapterId,

            @RequestParam("files")
            MultipartFile[] files,

            Model model
    ) {

        Optional<Chapter> chapterOptional =
                chapterService.getChapterById(
                        chapterId
                );


        if (chapterOptional.isEmpty()) {
            return "redirect:/mangak";
        }


        try {

            chapterPageService.uploadPages(
                    chapterOptional.get(),
                    files
            );

        } catch (IOException e) {

            model.addAttribute(
                    "error",
                    "Failed to upload chapter pages."
            );


            return "redirect:/admin/chapter/"
                    + chapterId
                    + "/pages";
        }


        return "redirect:/admin/chapter/"
                + chapterId
                + "/pages";
    }


    // =========================================
    // DELETE PAGE
    // =========================================

    @PostMapping("/chapter-page/{pageId}/delete")
    public String deletePage(
            @PathVariable Long pageId,
            @RequestParam Long chapterId
    ) {

        chapterPageService.deletePage(
                pageId
        );


        return "redirect:/admin/chapter/"
                + chapterId
                + "/pages";
    }
}