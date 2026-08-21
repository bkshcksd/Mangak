package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.model.User;
import io.virinchi.mangak.service.MangaService;
import io.virinchi.mangak.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/manga")
public class MangaRestController {

    private final MangaService mangaService;
    private final UserService userService;


    public MangaRestController(
            MangaService mangaService,
            UserService userService
    ) {

        this.mangaService = mangaService;
        this.userService = userService;
    }


    // =========================================
    // API DASHBOARD PAGE
    // =========================================

    @GetMapping
    public String apiDashboard(
            Model model
    ) {

        List<Manga> mangaList =
                mangaService.getAllManga();


        List<User> userList =
                userService.getAllUsers();


        model.addAttribute(
                "mangaList",
                mangaList
        );


        model.addAttribute(
                "users",
                userList
        );


        model.addAttribute(
                "mangaCount",
                mangaList.size()
        );


        model.addAttribute(
                "userCount",
                userList.size()
        );


        return "api-manga";
    }


    // =========================================
    // GET ALL MANGA JSON
    // =========================================

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllManga() {

        List<Map<String, Object>> mangaList =
                mangaService.getAllManga()
                        .stream()
                        .map(this::convertMangaToMap)
                        .toList();


        return ResponseEntity.ok(
                mangaList
        );
    }


    // =========================================
    // GET MANGA BY ID JSON
    // =========================================

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMangaById(
            @PathVariable Long id
    ) {

        Optional<Manga> mangaOptional =
                mangaService.getMangaById(
                        id
                );


        if (mangaOptional.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        Map<String, Object> mangaData =
                convertMangaToMap(
                        mangaOptional.get()
                );


        return ResponseEntity.ok(
                mangaData
        );
    }


    // =========================================
    // CREATE MANGA
    // =========================================

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createManga(
            @RequestBody Manga manga
    ) {

        manga.setId(
                null
        );


        Manga savedManga =
                mangaService.saveManga(
                        manga
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        convertMangaToMap(
                                savedManga
                        )
                );
    }


    // =========================================
    // UPDATE MANGA
    // =========================================

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateManga(
            @PathVariable Long id,
            @RequestBody Manga updatedManga
    ) {

        Optional<Manga> mangaOptional =
                mangaService.getMangaById(
                        id
                );


        if (mangaOptional.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        Manga manga =
                mangaOptional.get();


        manga.setTitle(
                updatedManga.getTitle()
        );


        manga.setDescription(
                updatedManga.getDescription()
        );


        manga.setCoverImage(
                updatedManga.getCoverImage()
        );


        manga.setStatus(
                updatedManga.getStatus()
        );


        manga.setAuthor(
                updatedManga.getAuthor()
        );


        manga.setArtist(
                updatedManga.getArtist()
        );


        manga.setDemographic(
                updatedManga.getDemographic()
        );


        manga.setType(
                updatedManga.getType()
        );


        manga.setReleaseYear(
                updatedManga.getReleaseYear()
        );


        Manga savedManga =
                mangaService.saveManga(
                        manga
                );


        return ResponseEntity.ok(
                convertMangaToMap(
                        savedManga
                )
        );
    }


    // =========================================
    // DELETE MANGA REST API
    // =========================================

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteManga(
            @PathVariable Long id
    ) {

        Optional<Manga> mangaOptional =
                mangaService.getMangaById(
                        id
                );


        if (mangaOptional.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        mangaService.deleteManga(
                id
        );


        return ResponseEntity
                .noContent()
                .build();
    }


    // =========================================
    // DELETE USER FROM DASHBOARD
    // =========================================

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id
    ) {

        Optional<User> userOptional =
                userService.getUserById(
                        id
                );


        if (userOptional.isEmpty()) {

            return "redirect:/api/manga?userNotFound";
        }


        User user =
                userOptional.get();


        // Never allow ADMIN account to be deleted
        if ("ADMIN".equalsIgnoreCase(
                user.getRole()
        )) {

            return "redirect:/api/manga?adminProtected";
        }


        userService.deleteUser(
                id
        );


        return "redirect:/api/manga?userDeleted";
    }


    // =========================================
    // CONVERT MANGA TO CLEAN JSON
    // =========================================

    private Map<String, Object> convertMangaToMap(
            Manga manga
    ) {

        Map<String, Object> data =
                new LinkedHashMap<>();


        data.put(
                "id",
                manga.getId()
        );


        data.put(
                "title",
                manga.getTitle()
        );


        data.put(
                "description",
                manga.getDescription()
        );


        data.put(
                "coverImage",
                manga.getCoverImage()
        );


        data.put(
                "status",
                manga.getStatus()
        );


        data.put(
                "author",
                manga.getAuthor()
        );


        data.put(
                "artist",
                manga.getArtist()
        );


        data.put(
                "demographic",
                manga.getDemographic()
        );


        data.put(
                "type",
                manga.getType()
        );


        data.put(
                "releaseYear",
                manga.getReleaseYear()
        );


        data.put(
                "genres",
                manga.getGenres()
                        .stream()
                        .map(genre -> genre.getName())
                        .toList()
        );


        data.put(
                "tags",
                manga.getTags()
                        .stream()
                        .map(tag -> tag.getName())
                        .toList()
        );


        return data;
    }
}