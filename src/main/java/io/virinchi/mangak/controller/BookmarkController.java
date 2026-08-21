package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Bookmark;
import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.ReadingHistory;
import io.virinchi.mangak.service.BookmarkService;
import io.virinchi.mangak.service.ChapterService;
import io.virinchi.mangak.service.ReadingHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final ChapterService chapterService;
    private final ReadingHistoryService readingHistoryService;


    public BookmarkController(
            BookmarkService bookmarkService,
            ChapterService chapterService,
            ReadingHistoryService readingHistoryService
    ) {
        this.bookmarkService = bookmarkService;
        this.chapterService = chapterService;
        this.readingHistoryService = readingHistoryService;
    }


    // =========================================
    // MY LIST PAGE
    // =========================================

    @GetMapping("/my-list")
    public String myList(
            HttpSession session,
            Model model
    ) {

        Long userId =
                (Long) session
                        .getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        List<Bookmark> bookmarks =
                bookmarkService
                        .getBookmarksByUser(
                                userId
                        );


        model.addAttribute(
                "bookmarks",
                bookmarks
        );


        model.addAttribute(
                "username",
                session.getAttribute(
                        "username"
                )
        );


        model.addAttribute(
                "role",
                session.getAttribute(
                        "role"
                )
        );


        return "my-list";
    }


    // =========================================
    // ADD TO MY LIST
    // =========================================

    @PostMapping("/my-list/add/{mangaId}")
    public String addToMyList(
            @PathVariable Long mangaId,
            HttpSession session
    ) {

        Long userId =
                (Long) session
                        .getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        bookmarkService.addBookmark(
                userId,
                mangaId
        );


        return "redirect:/manga/"
                + mangaId;
    }


    // =========================================
    // REMOVE FROM MY LIST
    // =========================================

    @PostMapping("/my-list/remove/{mangaId}")
    public String removeFromMyList(
            @PathVariable Long mangaId,
            HttpSession session
    ) {

        Long userId =
                (Long) session
                        .getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        bookmarkService.removeBookmark(
                userId,
                mangaId
        );


        return "redirect:/my-list";
    }


    // =========================================
    // TOGGLE BOOKMARK
    // =========================================

    @PostMapping("/my-list/toggle/{mangaId}")
    public String toggleBookmark(
            @PathVariable Long mangaId,
            HttpSession session
    ) {

        Long userId =
                (Long) session
                        .getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        bookmarkService.toggleBookmark(
                userId,
                mangaId
        );


        return "redirect:/manga/"
                + mangaId;
    }


    // =========================================
    // UPDATE READING STATUS
    // =========================================

    @PostMapping("/my-list/status/{mangaId}")
    public String updateReadingStatus(
            @PathVariable Long mangaId,
            @RequestParam String status,
            HttpSession session
    ) {

        Long userId =
                (Long) session
                        .getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        bookmarkService.updateReadingStatus(
                userId,
                mangaId,
                status
        );


        return "redirect:/my-list";
    }


    // =========================================
    // CONTINUE READING
    // =========================================

    @GetMapping("/my-list/continue/{mangaId}")
    public String continueReading(
            @PathVariable Long mangaId,
            HttpSession session
    ) {

        Long userId =
                (Long) session
                        .getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        // =========================================
        // CHECK READING HISTORY FIRST
        // =========================================

        Optional<ReadingHistory> historyOptional =
                readingHistoryService.getHistory(
                        userId,
                        mangaId
                );


        if (historyOptional.isPresent()) {

            ReadingHistory history =
                    historyOptional.get();


            return "redirect:/chapter/"
                    + history
                    .getChapter()
                    .getId();
        }


        // =========================================
        // NO HISTORY -> FIRST CHAPTER
        // =========================================

        List<Chapter> chapters =
                chapterService
                        .getChaptersByManga(
                                mangaId
                        );


        if (chapters.isEmpty()) {

            return "redirect:/manga/"
                    + mangaId;
        }


        Chapter firstChapter =
                chapters.get(0);


        return "redirect:/chapter/"
                + firstChapter.getId();
    }
}