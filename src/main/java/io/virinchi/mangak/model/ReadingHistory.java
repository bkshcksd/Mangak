package io.virinchi.mangak.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reading_history",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "manga_id"}
                )
        }
)
public class ReadingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================
    // USER
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    // =========================================
    // MANGA
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "manga_id",
            nullable = false
    )
    private Manga manga;


    // =========================================
    // LAST CHAPTER
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "chapter_id",
            nullable = false
    )
    private Chapter chapter;


    // =========================================
    // LAST READ TIME
    // =========================================

    @Column(
            name = "last_read_at",
            nullable = false
    )
    private LocalDateTime lastReadAt;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public ReadingHistory() {
    }


    public ReadingHistory(
            User user,
            Manga manga,
            Chapter chapter
    ) {

        this.user = user;
        this.manga = manga;
        this.chapter = chapter;
        this.lastReadAt = LocalDateTime.now();
    }


    // =========================================
    // GETTERS AND SETTERS
    // =========================================

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public Manga getManga() {
        return manga;
    }


    public void setManga(Manga manga) {
        this.manga = manga;
    }


    public Chapter getChapter() {
        return chapter;
    }


    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }


    public LocalDateTime getLastReadAt() {
        return lastReadAt;
    }


    public void setLastReadAt(
            LocalDateTime lastReadAt
    ) {
        this.lastReadAt = lastReadAt;
    }
}