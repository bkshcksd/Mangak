package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.model.ReadingHistory;
import io.virinchi.mangak.model.User;
import io.virinchi.mangak.repository.ReadingHistoryRepository;
import io.virinchi.mangak.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReadingHistoryService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final UserRepository userRepository;


    public ReadingHistoryService(
            ReadingHistoryRepository readingHistoryRepository,
            UserRepository userRepository
    ) {
        this.readingHistoryRepository = readingHistoryRepository;
        this.userRepository = userRepository;
    }


    // =========================================
    // GET USER READING HISTORY
    // =========================================

    public List<ReadingHistory> getUserHistory(
            Long userId
    ) {

        return readingHistoryRepository
                .findByUserIdOrderByLastReadAtDesc(userId);
    }


    // =========================================
    // GET HISTORY FOR ONE MANGA
    // =========================================

    public Optional<ReadingHistory> getHistory(
            Long userId,
            Long mangaId
    ) {

        return readingHistoryRepository
                .findByUserIdAndMangaId(
                        userId,
                        mangaId
                );
    }


    // =========================================
    // RECORD / UPDATE READING HISTORY
    // =========================================

    @Transactional
    public void recordReading(
            Long userId,
            Chapter chapter
    ) {

        if (
                userId == null
                        ||
                        chapter == null
                        ||
                        chapter.getManga() == null
        ) {
            return;
        }


        Optional<User> userOptional =
                userRepository.findById(userId);


        if (userOptional.isEmpty()) {
            return;
        }


        Manga manga =
                chapter.getManga();


        Optional<ReadingHistory> historyOptional =
                readingHistoryRepository
                        .findByUserIdAndMangaId(
                                userId,
                                manga.getId()
                        );


        ReadingHistory history;


        // EXISTING HISTORY
        if (historyOptional.isPresent()) {

            history =
                    historyOptional.get();


            history.setChapter(
                    chapter
            );


            history.setLastReadAt(
                    LocalDateTime.now()
            );

        }

        // NEW HISTORY
        else {

            history =
                    new ReadingHistory();


            history.setUser(
                    userOptional.get()
            );


            history.setManga(
                    manga
            );


            history.setChapter(
                    chapter
            );


            history.setLastReadAt(
                    LocalDateTime.now()
            );
        }


        readingHistoryRepository.save(
                history
        );
    }


    // =========================================
    // REMOVE ONE HISTORY ITEM
    // =========================================

    @Transactional
    public void removeHistory(
            Long userId,
            Long mangaId
    ) {

        readingHistoryRepository
                .deleteByUserIdAndMangaId(
                        userId,
                        mangaId
                );
    }


    // =========================================
    // CLEAR ALL HISTORY
    // =========================================

    @Transactional
    public void clearHistory(
            Long userId
    ) {

        readingHistoryRepository
                .deleteByUserId(
                        userId
                );
    }
}