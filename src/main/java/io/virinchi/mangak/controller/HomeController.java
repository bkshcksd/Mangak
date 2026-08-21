package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.model.ReadingHistory;
import io.virinchi.mangak.service.MangaService;
import io.virinchi.mangak.service.ReadingHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final MangaService mangaService;
    private final ReadingHistoryService readingHistoryService;


    public HomeController(
            MangaService mangaService,
            ReadingHistoryService readingHistoryService
    ) {
        this.mangaService = mangaService;
        this.readingHistoryService = readingHistoryService;
    }


    // =========================================
    // MANGAK HOME PAGE
    // =========================================

    @GetMapping("/mangak")
    public String home(

            @RequestParam(
                    name = "updatePage",
                    defaultValue = "0"
            ) int updatePage,

            HttpSession session,
            Model model
    ) {

        // =========================================
        // SESSION USER INFORMATION
        // =========================================

        Long userId =
                (Long) session.getAttribute("userId");


        String username =
                (String) session.getAttribute("username");


        String role =
                (String) session.getAttribute("role");


        // =========================================
        // GET ALL MANGA FROM TiDB
        // =========================================
        // Used by:
        // New Chapter
        // Most Popular
        // Adapted To Anime
        // Recently Added
        // Ongoing
        // =========================================

        List<Manga> mangaList =
                mangaService.getAllManga();


        // =========================================
        // GET USER READING HISTORY
        // =========================================

        List<ReadingHistory> readingHistoryList =
                new ArrayList<>();


        if (userId != null) {

            readingHistoryList =
                    readingHistoryService
                            .getUserHistory(
                                    userId
                            );
        }


        // =========================================
        // HOMEPAGE UPDATES PAGINATION
        // =========================================

        if (updatePage < 0) {
            updatePage = 0;
        }


        Pageable updatesPageable =
                PageRequest.of(
                        updatePage,
                        15,
                        Sort.by(
                                Sort.Direction.DESC,
                                "id"
                        )
                );


        Page<Manga> updatesPage =
                mangaService
                        .getMangaPage(
                                updatesPageable
                        );


        // =========================================
        // CHECK INVALID PAGE NUMBER
        // =========================================

        if (
                updatesPage.getTotalPages() > 0
                        &&
                        updatePage >= updatesPage.getTotalPages()
        ) {

            return "redirect:/mangak?updatePage="
                    + (updatesPage.getTotalPages() - 1);
        }


        // =========================================
        // SEND USER INFORMATION TO THYMELEAF
        // =========================================

        model.addAttribute(
                "userId",
                userId
        );


        model.addAttribute(
                "username",
                username
        );


        model.addAttribute(
                "role",
                role
        );


        // =========================================
        // SEND ALL MANGA TO THYMELEAF
        // =========================================

        model.addAttribute(
                "mangaList",
                mangaList
        );


        // =========================================
        // SEND READING HISTORY TO THYMELEAF
        // =========================================

        model.addAttribute(
                "readingHistoryList",
                readingHistoryList
        );


        // =========================================
        // SEND HOMEPAGE UPDATES TO THYMELEAF
        // =========================================

        model.addAttribute(
                "updatesList",
                updatesPage.getContent()
        );


        model.addAttribute(
                "updateCurrentPage",
                updatePage
        );


        model.addAttribute(
                "updateTotalPages",
                updatesPage.getTotalPages()
        );


        model.addAttribute(
                "updateTotalItems",
                updatesPage.getTotalElements()
        );


        return "mangak";
    }
}