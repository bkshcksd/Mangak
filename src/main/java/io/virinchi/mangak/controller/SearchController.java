package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Genre;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.model.Tag;
import io.virinchi.mangak.service.GenreService;
import io.virinchi.mangak.service.MangaService;
import io.virinchi.mangak.service.TagService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private final MangaService mangaService;
    private final GenreService genreService;
    private final TagService tagService;


    public SearchController(
            MangaService mangaService,
            GenreService genreService,
            TagService tagService
    ) {
        this.mangaService = mangaService;
        this.genreService = genreService;
        this.tagService = tagService;
    }


    // =========================================
    // QUICK SEARCH FROM NAVBAR
    // =========================================

    @GetMapping("/quick-search")
    public String quickSearch(
            @RequestParam(required = false)
            String keyword
    ) {

        // EMPTY SEARCH

        if (
                keyword == null
                        ||
                        keyword.trim().isEmpty()
        ) {

            return "redirect:/search";
        }


        String searchKeyword =
                keyword.trim();


        // =========================================
        // TRY EXACT TITLE
        // =========================================

        Optional<Manga> manga =
                mangaService.getMangaByExactTitle(
                        searchKeyword
                );


        // =========================================
        // EXACT MATCH FOUND
        // =========================================

        if (manga.isPresent()) {

            return "redirect:/manga/"
                    + manga.get().getId();
        }


        // =========================================
        // NO EXACT MATCH
        // SEND TO SEARCH PAGE
        // =========================================

        String encodedKeyword =
                URLEncoder.encode(
                        searchKeyword,
                        StandardCharsets.UTF_8
                );


        return "redirect:/search?keyword="
                + encodedKeyword;
    }


    // =========================================
    // SEARCH PAGE
    // =========================================

    @GetMapping("/search")
    public String search(

            @RequestParam(
                    required = false
            )
            String keyword,

            @RequestParam(
                    required = false
            )
            Long genreId,

            @RequestParam(
                    required = false
            )
            Long tagId,

            @RequestParam(
                    required = false
            )
            String status,

            @RequestParam(
                    required = false
            )
            String demographic,

            @RequestParam(
                    required = false
            )
            String type,

            @RequestParam(
                    required = false
            )
            Integer releaseYear,

            @RequestParam(
                    required = false
            )
            Integer minimumChapterCount,

            HttpSession session,
            Model model
    ) {

        // =========================================
        // GET GENRES + TAGS
        // =========================================

        List<Genre> genres =
                genreService.getAllGenres();


        List<Tag> tags =
                tagService.getAllTags();


        // =========================================
        // SEARCH
        // =========================================

        List<Manga> searchResults =
                mangaService.searchManga(
                        keyword,
                        genreId,
                        tagId,
                        status,
                        demographic,
                        type,
                        releaseYear,
                        minimumChapterCount
                );


        // =========================================
        // SEARCH RESULTS
        // =========================================

        model.addAttribute(
                "searchResults",
                searchResults
        );


        model.addAttribute(
                "resultCount",
                searchResults.size()
        );


        // =========================================
        // FILTER OPTIONS
        // =========================================

        model.addAttribute(
                "genres",
                genres
        );


        model.addAttribute(
                "tags",
                tags
        );


        // =========================================
        // KEEP SELECTED FILTERS
        // =========================================

        model.addAttribute(
                "keyword",
                keyword
        );


        model.addAttribute(
                "selectedGenreId",
                genreId
        );


        model.addAttribute(
                "selectedTagId",
                tagId
        );


        model.addAttribute(
                "selectedStatus",
                status
        );


        model.addAttribute(
                "selectedDemographic",
                demographic
        );


        model.addAttribute(
                "selectedType",
                type
        );


        model.addAttribute(
                "selectedReleaseYear",
                releaseYear
        );


        model.addAttribute(
                "selectedMinimumChapterCount",
                minimumChapterCount
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


        return "search";
    }
}