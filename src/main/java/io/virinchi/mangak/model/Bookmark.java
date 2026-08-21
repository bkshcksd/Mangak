package io.virinchi.mangak.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "bookmarks",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "manga_id"}
                )
        }
)
public class Bookmark {

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
    // READING STATUS
    // =========================================

    @Column(
            name = "reading_status",
            nullable = false
    )
    private String readingStatus = "READING";


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public Bookmark() {
    }


    public Bookmark(
            User user,
            Manga manga
    ) {
        this.user = user;
        this.manga = manga;
        this.readingStatus = "READING";
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


    public String getReadingStatus() {
        return readingStatus;
    }


    public void setReadingStatus(
            String readingStatus
    ) {
        this.readingStatus = readingStatus;
    }
}