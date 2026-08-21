package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.service.MangaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UpdatesController {

    private final MangaService mangaService;


    public UpdatesController(
            MangaService mangaService
    ) {
        this.mangaService = mangaService;
    }


    // =========================================
    // UPDATES PAGE
    // =========================================

    @GetMapping("/updates")
    public String updates(

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            ) int page,

            HttpSession session,
            Model model
    ) {

        // =========================================
        // PREVENT NEGATIVE PAGE NUMBER
        // =========================================

        if (page < 0) {
            page = 0;
        }


        // =========================================
        // PAGINATION
        // 15 MANGA PER PAGE
        // 5 MANGA PER ROW
        // NEWEST DATABASE ID FIRST
        // =========================================

        Pageable pageable =
                PageRequest.of(
                        page,
                        15,
                        Sort.by(
                                Sort.Direction.DESC,
                                "id"
                        )
                );


        // =========================================
        // GET PAGINATED MANGA
        // =========================================

        Page<Manga> mangaPage =
                mangaService
                        .getMangaPage(
                                pageable
                        );


        // =========================================
        // INVALID PAGE NUMBER
        // =========================================

        if (
                mangaPage.getTotalPages() > 0
                        &&
                        page >= mangaPage.getTotalPages()
        ) {

            return "redirect:/updates?page="
                    + (mangaPage.getTotalPages() - 1);
        }


        // =========================================
        // SEND MANGA TO THYMELEAF
        // =========================================

        model.addAttribute(
                "mangaPage",
                mangaPage
        );


        model.addAttribute(
                "mangaList",
                mangaPage.getContent()
        );


        // =========================================
        // PAGINATION INFORMATION
        // =========================================

        model.addAttribute(
                "currentPage",
                page
        );


        model.addAttribute(
                "totalPages",
                mangaPage.getTotalPages()
        );


        model.addAttribute(
                "totalItems",
                mangaPage.getTotalElements()
        );


        model.addAttribute(
                "hasPrevious",
                mangaPage.hasPrevious()
        );


        model.addAttribute(
                "hasNext",
                mangaPage.hasNext()
        );


        // =========================================
        // USER INFORMATION
        // =========================================

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


        return "updates";
    }
}