package io.virinchi.mangak.config;

import io.virinchi.mangak.model.Genre;
import io.virinchi.mangak.model.Tag;
import io.virinchi.mangak.service.GenreService;
import io.virinchi.mangak.service.TagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GenreTagInitializer implements CommandLineRunner {

    private final GenreService genreService;
    private final TagService tagService;


    public GenreTagInitializer(
            GenreService genreService,
            TagService tagService
    ) {
        this.genreService = genreService;
        this.tagService = tagService;
    }


    @Override
    public void run(String... args) {

        // =========================================
        // GENRES
        // =========================================

        String[] genres = {
                "Action",
                "Adventure",
                "Comedy",
                "Drama",
                "Fantasy",
                "Horror",
                "Mystery",
                "Romance",
                "Sci-Fi",
                "Slice of Life",
                "Sports",
                "Supernatural",
                "Thriller",
                "Psychological",
                "Martial Arts"
        };


        for (String genreName : genres) {

            if (!genreService.existsByName(genreName)) {

                Genre genre =
                        new Genre(
                                genreName
                        );

                genreService.saveGenre(
                        genre
                );
            }
        }


        // =========================================
        // TAGS
        // =========================================

        String[] tags = {
                "Magic",
                "Dungeon",
                "School",
                "Regression",
                "Reincarnation",
                "System",
                "Leveling",
                "Overpowered MC",
                "Demons",
                "Monsters",
                "Cultivation",
                "Historical",
                "Time Travel",
                "Survival",
                "Game"
        };


        for (String tagName : tags) {

            if (!tagService.existsByName(tagName)) {

                Tag tag =
                        new Tag(
                                tagName
                        );

                tagService.saveTag(
                        tag
                );
            }
        }
    }
}