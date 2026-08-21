package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.service.BookmarkService;
import io.virinchi.mangak.service.ChapterService;
import io.virinchi.mangak.service.MangaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class MangaController {

    private final MangaService mangaService;
    private final ChapterService chapterService;
    private final BookmarkService bookmarkService;


    public MangaController(
            MangaService mangaService,
            ChapterService chapterService,
            BookmarkService bookmarkService
    ) {
        this.mangaService = mangaService;
        this.chapterService = chapterService;
        this.bookmarkService = bookmarkService;
    }


    @GetMapping("/manga/{id}")
    public String mangaDetails(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {

        Optional<Manga> mangaOptional =
                mangaService.getMangaById(id);


        if (mangaOptional.isEmpty()) {
            return "redirect:/mangak";
        }


        Manga manga =
                mangaOptional.get();


        List<Chapter> chapters =
                chapterService.getChaptersByManga(id);


        Long userId =
                (Long) session.getAttribute("userId");


        boolean isBookmarked = false;


        if (userId != null) {

            isBookmarked =
                    bookmarkService.isBookmarked(
                            userId,
                            manga.getId()
                    );
        }


        model.addAttribute(
                "manga",
                manga
        );


        model.addAttribute(
                "chapters",
                chapters
        );


        model.addAttribute(
                "isBookmarked",
                isBookmarked
        );


        model.addAttribute(
                "username",
                session.getAttribute("username")
        );


        model.addAttribute(
                "role",
                session.getAttribute("role")
        );


        return "manga-details";
    }
}