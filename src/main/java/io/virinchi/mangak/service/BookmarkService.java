package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Bookmark;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.model.User;
import io.virinchi.mangak.repository.BookmarkRepository;
import io.virinchi.mangak.repository.MangaRepository;
import io.virinchi.mangak.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final MangaRepository mangaRepository;


    public BookmarkService(
            BookmarkRepository bookmarkRepository,
            UserRepository userRepository,
            MangaRepository mangaRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.mangaRepository = mangaRepository;
    }


    // =========================================
    // GET USER BOOKMARKS
    // =========================================

    public List<Bookmark> getBookmarksByUser(
            Long userId
    ) {

        return bookmarkRepository
                .findByUserIdOrderByIdDesc(userId);
    }


    // =========================================
    // CHECK BOOKMARK
    // =========================================

    public boolean isBookmarked(
            Long userId,
            Long mangaId
    ) {

        return bookmarkRepository
                .existsByUserIdAndMangaId(
                        userId,
                        mangaId
                );
    }


    // =========================================
    // GET SPECIFIC BOOKMARK
    // =========================================

    public Optional<Bookmark> getBookmark(
            Long userId,
            Long mangaId
    ) {

        return bookmarkRepository
                .findByUserIdAndMangaId(
                        userId,
                        mangaId
                );
    }


    // =========================================
    // ADD TO MY LIST
    // =========================================

    public Bookmark addBookmark(
            Long userId,
            Long mangaId
    ) {

        if (
                bookmarkRepository
                        .existsByUserIdAndMangaId(
                                userId,
                                mangaId
                        )
        ) {

            return bookmarkRepository
                    .findByUserIdAndMangaId(
                            userId,
                            mangaId
                    )
                    .orElse(null);
        }


        Optional<User> userOptional =
                userRepository.findById(userId);


        Optional<Manga> mangaOptional =
                mangaRepository.findById(mangaId);


        if (
                userOptional.isEmpty()
                        ||
                        mangaOptional.isEmpty()
        ) {

            return null;
        }


        Bookmark bookmark =
                new Bookmark();


        bookmark.setUser(
                userOptional.get()
        );


        bookmark.setManga(
                mangaOptional.get()
        );


        bookmark.setReadingStatus(
                "READING"
        );


        return bookmarkRepository
                .save(bookmark);
    }


    // =========================================
    // UPDATE READING STATUS
    // =========================================

    public boolean updateReadingStatus(
            Long userId,
            Long mangaId,
            String status
    ) {

        Optional<Bookmark> bookmarkOptional =
                bookmarkRepository
                        .findByUserIdAndMangaId(
                                userId,
                                mangaId
                        );


        if (bookmarkOptional.isEmpty()) {

            return false;
        }


        if (
                !status.equals("READING")
                        &&
                        !status.equals("COMPLETED")
                        &&
                        !status.equals("PLAN_TO_READ")
                        &&
                        !status.equals("ON_HOLD")
                        &&
                        !status.equals("DROPPED")
        ) {

            return false;
        }


        Bookmark bookmark =
                bookmarkOptional.get();


        bookmark.setReadingStatus(
                status
        );


        bookmarkRepository.save(
                bookmark
        );


        return true;
    }


    // =========================================
    // REMOVE FROM MY LIST
    // =========================================

    @Transactional
    public void removeBookmark(
            Long userId,
            Long mangaId
    ) {

        bookmarkRepository
                .deleteByUserIdAndMangaId(
                        userId,
                        mangaId
                );
    }


    // =========================================
    // TOGGLE BOOKMARK
    // =========================================

    @Transactional
    public boolean toggleBookmark(
            Long userId,
            Long mangaId
    ) {

        boolean bookmarked =
                bookmarkRepository
                        .existsByUserIdAndMangaId(
                                userId,
                                mangaId
                        );


        if (bookmarked) {

            bookmarkRepository
                    .deleteByUserIdAndMangaId(
                            userId,
                            mangaId
                    );


            return false;
        }


        addBookmark(
                userId,
                mangaId
        );


        return true;
    }

}