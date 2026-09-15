package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.Genre;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.model.Tag;
import io.virinchi.mangak.service.ChapterService;
import io.virinchi.mangak.service.GenreService;
import io.virinchi.mangak.service.MangaService;
import io.virinchi.mangak.service.TagService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminMangaController {

    private final MangaService mangaService;
    private final ChapterService chapterService;
    private final GenreService genreService;
    private final TagService tagService;


    public AdminMangaController(
            MangaService mangaService,
            ChapterService chapterService,
            GenreService genreService,
            TagService tagService
    ) {
        this.mangaService = mangaService;
        this.chapterService = chapterService;
        this.genreService = genreService;
        this.tagService = tagService;
    }


    // =========================================
    // GET MANGA IN ID ORDER
    // =========================================

    private List<Manga> getOrderedManga() {

        return mangaService
                .getAllManga()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Manga::getId
                        )
                )
                .toList();
    }


    // =========================================
    // GET MANGA USING ADMIN NUMBER
    // 1 = FIRST MANGA
    // 2 = SECOND MANGA
    // ETC
    // =========================================

    private Manga getMangaByAdminNumber(
            int adminNumber
    ) {

        List<Manga> mangaList =
                getOrderedManga();


        int index =
                adminNumber - 1;


        if (
                index < 0
                        ||
                        index >= mangaList.size()
        ) {

            return null;
        }


        return mangaList.get(index);
    }


    // =========================================
    // FIND ADMIN NUMBER USING DATABASE ID
    // =========================================

    private int getAdminNumber(
            Long mangaId
    ) {

        List<Manga> mangaList =
                getOrderedManga();


        for (
                int i = 0;
                i < mangaList.size();
                i++
        ) {

            if (
                    mangaList
                            .get(i)
                            .getId()
                            .equals(mangaId)
            ) {

                return i + 1;
            }
        }


        return 1;
    }


    // =========================================
    // ADMIN HOME
    // =========================================

    @GetMapping
    public String adminHome() {

        List<Manga> mangaList =
                getOrderedManga();


        if (mangaList.isEmpty()) {

            return "redirect:/mangak";
        }


        return "redirect:/admin/manga/1";
    }


    // =========================================
    // MANGA ADMIN PAGE
    // =========================================

    @GetMapping("/manga/{adminNumber}")
    public String mangaAdmin(
            @PathVariable int adminNumber,
            HttpSession session,
            Model model
    ) {

        Manga manga =
                getMangaByAdminNumber(
                        adminNumber
                );


        if (manga == null) {

            return "redirect:/admin/manga/1";
        }


        List<Chapter> chapters =
                chapterService
                        .getChaptersByManga(
                                manga.getId()
                        );


        List<Genre> genres =
                genreService
                        .getAllGenres();


        List<Tag> tags =
                tagService
                        .getAllTags();


        // =========================================
        // ADD SELECTED MANGA
        // =========================================

        model.addAttribute(
                "manga",
                manga
        );


        // =========================================
        // ADD ALL MANGA FOR SELECTOR
        // =========================================

        model.addAttribute(
                "mangaList",
                getOrderedManga()
        );


        model.addAttribute(
                "chapters",
                chapters
        );


        model.addAttribute(
                "genres",
                genres
        );


        model.addAttribute(
                "tags",
                tags
        );


        model.addAttribute(
                "adminNumber",
                adminNumber
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


        return "admin-manga";
    }


    // =========================================
    // UPDATE MANGA
    // =========================================

    @PostMapping("/manga/{adminNumber}/update")
    public String updateManga(
            @PathVariable int adminNumber,

            @RequestParam String title,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            String author,

            @RequestParam(required = false)
            String artist,

            @RequestParam String status,

            @RequestParam(required = false)
            String demographic,

            @RequestParam(required = false)
            String type,

            @RequestParam(required = false)
            Integer releaseYear,

            @RequestParam(
                    required = false
            )
            List<Long> genreIds,

            @RequestParam(
                    required = false
            )
            List<Long> tagIds
    ) {

        Manga manga =
                getMangaByAdminNumber(
                        adminNumber
                );


        if (manga == null) {

            return "redirect:/admin/manga/1";
        }


        // =========================================
        // BASIC INFORMATION
        // =========================================

        manga.setTitle(
                title
        );


        manga.setDescription(
                description
        );


        manga.setAuthor(
                author
        );


        manga.setArtist(
                artist
        );


        manga.setStatus(
                status
        );


        manga.setDemographic(
                demographic
        );


        manga.setType(
                type
        );


        manga.setReleaseYear(
                releaseYear
        );


        // =========================================
        // GENRES
        // =========================================

        Set<Genre> selectedGenres =
                new HashSet<>();


        if (genreIds != null) {

            for (Long genreId : genreIds) {

                Optional<Genre> genreOptional =
                        genreService
                                .getGenreById(
                                        genreId
                                );


                genreOptional.ifPresent(
                        selectedGenres::add
                );
            }
        }


        manga.setGenres(
                selectedGenres
        );


        // =========================================
        // TAGS
        // =========================================

        Set<Tag> selectedTags =
                new HashSet<>();


        if (tagIds != null) {

            for (Long tagId : tagIds) {

                Optional<Tag> tagOptional =
                        tagService
                                .getTagById(
                                        tagId
                                );


                tagOptional.ifPresent(
                        selectedTags::add
                );
            }
        }


        manga.setTags(
                selectedTags
        );


        // =========================================
        // SAVE
        // =========================================

        mangaService.saveManga(
                manga
        );


        return "redirect:/admin/manga/"
                + adminNumber;
    }


    // =========================================
    // ADD CHAPTER
    // =========================================

    @PostMapping("/manga/{adminNumber}/chapter/add")
    public String addChapter(
            @PathVariable int adminNumber,

            @RequestParam Integer chapterNumber,

            @RequestParam String title
    ) {

        Manga manga =
                getMangaByAdminNumber(
                        adminNumber
                );


        if (manga == null) {

            return "redirect:/admin/manga/1";
        }


        Long mangaId =
                manga.getId();


        if (
                chapterService.chapterExists(
                        mangaId,
                        chapterNumber
                )
        ) {

            return "redirect:/admin/manga/"
                    + adminNumber
                    + "?chapterExists=true";
        }


        Chapter chapter =
                new Chapter();


        chapter.setManga(
                manga
        );


        chapter.setChapterNumber(
                chapterNumber
        );


        chapter.setTitle(
                title
        );


        chapter.setContent(
                ""
        );


        chapterService.saveChapter(
                chapter
        );


        return "redirect:/admin/manga/"
                + adminNumber;
    }


    // =========================================
    // EDIT CHAPTER
    // =========================================

    @PostMapping("/chapter/{chapterId}/update")
    public String updateChapter(
            @PathVariable Long chapterId,

            @RequestParam Integer chapterNumber,

            @RequestParam String title
    ) {

        Optional<Chapter> chapterOptional =
                chapterService
                        .getChapterById(
                                chapterId
                        );


        if (chapterOptional.isEmpty()) {

            return "redirect:/admin";
        }


        Chapter chapter =
                chapterOptional.get();


        Long mangaId =
                chapter
                        .getManga()
                        .getId();


        int adminNumber =
                getAdminNumber(
                        mangaId
                );


        if (
                !chapter
                        .getChapterNumber()
                        .equals(chapterNumber)
                        &&
                        chapterService.chapterExists(
                                mangaId,
                                chapterNumber
                        )
        ) {

            return "redirect:/admin/manga/"
                    + adminNumber
                    + "?chapterExists=true";
        }


        chapter.setChapterNumber(
                chapterNumber
        );


        chapter.setTitle(
                title
        );


        chapterService.saveChapter(
                chapter
        );


        return "redirect:/admin/manga/"
                + adminNumber;
    }


    // =========================================
    // DELETE CHAPTER
    // =========================================

    @PostMapping("/chapter/{chapterId}/delete")
    public String deleteChapter(
            @PathVariable Long chapterId
    ) {

        Optional<Chapter> chapterOptional =
                chapterService
                        .getChapterById(
                                chapterId
                        );


        if (chapterOptional.isEmpty()) {

            return "redirect:/admin";
        }


        Long mangaId =
                chapterOptional
                        .get()
                        .getManga()
                        .getId();


        int adminNumber =
                getAdminNumber(
                        mangaId
                );


        chapterService.deleteChapter(
                chapterId
        );


        return "redirect:/admin/manga/"
                + adminNumber;
    }


    // =========================================
    // DELETE MANGA
    // =========================================

    @PostMapping("/manga/{adminNumber}/delete")
    public String deleteManga(
            @PathVariable int adminNumber
    ) {

        Manga manga =
                getMangaByAdminNumber(
                        adminNumber
                );


        if (manga == null) {

            return "redirect:/admin";
        }


        mangaService.deleteManga(
                manga.getId()
        );


        return "redirect:/admin";
    }
}