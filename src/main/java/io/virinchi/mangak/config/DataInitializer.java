package io.virinchi.mangak.config;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.service.ChapterService;
import io.virinchi.mangak.service.MangaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MangaService mangaService;
    private final ChapterService chapterService;

    public DataInitializer(
            MangaService mangaService,
            ChapterService chapterService
    ) {
        this.mangaService = mangaService;
        this.chapterService = chapterService;
    }

    @Override
    public void run(String... args) {

        addManga(
                "Asper Kanojo",
                "Asper Kanojo",
                "/images/1.jpg"
        );

        addManga(
                "Let This Grieving Soul Retire!",
                "Let This Grieving Soul Retire!",
                "/images/2.jpg"
        );

        addManga(
                "The Player Hides His Past",
                "The Player Hides His Past",
                "/images/3.jpg"
        );

        addManga(
                "X-Rank: Surviving in the Demon Realm",
                "X-Rank: Surviving in the Demon Realm",
                "/images/4.jpg"
        );

        addManga(
                "The Return of the Disaster-Class Hero",
                "The Return of the Disaster-Class Hero",
                "/images/5.jpg"
        );

        addManga(
                "Taming the Munchkin",
                "Taming the Munchkin",
                "/images/6.jpg"
        );

        addManga(
                "The 100th Regression of the Max-Level Player",
                "The 100th Regression of the Max-Level Player",
                "/images/7.jpg"
        );

        addManga(
                "Isekai Romance Manual You Can Use From Today",
                "Isekai Romance Manual You Can Use From Today",
                "/images/8.jpg"
        );

        addManga(
                "Kanojo no Tomodachi (Her Friend)",
                "Kanojo no Tomodachi (Her Friend)",
                "/images/9.jpg"
        );

        addManga(
                "Boy's Abyss",
                "Boy's Abyss",
                "/images/10.jpg"
        );

        addManga(
                "Surviving the Apocalypse",
                "Surviving the Apocalypse",
                "/images/11.jpg"
        );

        addManga(
                "Title unclear from the image",
                "Title currently unknown.",
                "/images/12.jpg"
        );

        addManga(
                "For the Beautiful Juliet",
                "For the Beautiful Juliet",
                "/images/13.jpg"
        );

        addManga(
                "The Captivity King",
                "The Captivity King",
                "/images/14.jpg"
        );

        addManga(
                "Sword Devouring Sword Master",
                "Sword Devouring Sword Master",
                "/images/15.jpg"
        );

        addManga(
                "Wild West Murim",
                "Wild West Murim",
                "/images/16.jpg"
        );

        addManga(
                "Redo of Healer",
                "Redo of Healer",
                "/images/17.jpg"
        );

        addManga(
                "Doctor, Live Again / Doctor's Rebirth",
                "Doctor, Live Again / Doctor's Rebirth",
                "/images/18.jpg"
        );

        addManga(
                "Mobile Suit Gundam SEED",
                "Mobile Suit Gundam SEED",
                "/images/19.jpg"
        );

        addManga(
                "Cheonhwa Archive's Young Master",
                "Cheonhwa Archive's Young Master",
                "/images/20.jpg"
        );

        addManga(
                "Can't Escape, Won't Let You Escape",
                "Can't Escape, Won't Let You Escape",
                "/images/21.jpg"
        );

        addManga(
                "The Mage Who Was Constantly Called Incompetent Was Actually the World's Strongest but Was Unaware of It Due to Being Confined",
                "The Mage Who Was Constantly Called Incompetent Was Actually the World's Strongest but Was Unaware of It Due to Being Confined",
                "/images/22.jpg"
        );

        addManga(
                "Solo Leveling",
                "Sung Jin-Woo begins his journey from the weakest hunter.",
                "/images/solo.jpg"
        );


        /* =====================================
           ADD TEMPORARY CHAPTERS
        ===================================== */

        List<Manga> mangaList = mangaService.getAllManga();

        for (Manga manga : mangaList) {

            addChapter(
                    manga,
                    1,
                    "Chapter 1",
                    "This is temporary content for Chapter 1."
            );

            addChapter(
                    manga,
                    2,
                    "Chapter 2",
                    "This is temporary content for Chapter 2."
            );

            addChapter(
                    manga,
                    3,
                    "Chapter 3",
                    "This is temporary content for Chapter 3."
            );
        }
    }


    /* =====================================
       ADD MANGA
    ===================================== */

    private void addManga(
            String title,
            String description,
            String coverImage
    ) {

        if (!mangaService.existsByTitle(title)) {

            Manga manga = new Manga();

            manga.setTitle(title);
            manga.setDescription(description);
            manga.setCoverImage(coverImage);
            manga.setStatus("ONGOING");
            manga.setAuthor("Unknown");
            manga.setArtist("Unknown");

            mangaService.saveManga(manga);
        }
    }


    /* =====================================
       ADD CHAPTER
    ===================================== */

    private void addChapter(
            Manga manga,
            Integer chapterNumber,
            String title,
            String content
    ) {

        if (!chapterService.chapterExists(
                manga.getId(),
                chapterNumber
        )) {

            Chapter chapter = new Chapter();

            chapter.setManga(manga);
            chapter.setChapterNumber(chapterNumber);
            chapter.setTitle(title);
            chapter.setContent(content);

            chapterService.saveChapter(chapter);
        }
    }
}