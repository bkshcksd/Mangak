package io.virinchi.mangak.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manga")
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(length = 1000)
    private String description;


    private String coverImage;


    private String status;


    private String author;


    private String artist;


    // =========================================
    // DEMOGRAPHIC
    // =========================================

    @Column(length = 50)
    private String demographic;


    // =========================================
    // TYPE
    // =========================================

    @Column(length = 50)
    private String type;


    // =========================================
    // RELEASE YEAR
    // =========================================

    private Integer releaseYear;


    // =========================================
    // GENRES
    // =========================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "manga_genres",
            joinColumns = @JoinColumn(
                    name = "manga_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_id"
            )
    )
    private Set<Genre> genres =
            new HashSet<>();


    // =========================================
    // TAGS
    // =========================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "manga_tags",
            joinColumns = @JoinColumn(
                    name = "manga_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id"
            )
    )
    private Set<Tag> tags =
            new HashSet<>();


    // =========================================
    // DEFAULT CONSTRUCTOR
    // =========================================

    public Manga() {
    }


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public Manga(
            String title,
            String description,
            String coverImage,
            String status,
            String author,
            String artist,
            String demographic,
            String type,
            Integer releaseYear
    ) {
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.status = status;
        this.author = author;
        this.artist = artist;
        this.demographic = demographic;
        this.type = type;
        this.releaseYear = releaseYear;
    }


    // =========================================
    // ID
    // =========================================

    public Long getId() {
        return id;
    }


    public void setId(
            Long id
    ) {
        this.id = id;
    }


    // =========================================
    // TITLE
    // =========================================

    public String getTitle() {
        return title;
    }


    public void setTitle(
            String title
    ) {
        this.title = title;
    }


    // =========================================
    // DESCRIPTION
    // =========================================

    public String getDescription() {
        return description;
    }


    public void setDescription(
            String description
    ) {
        this.description = description;
    }


    // =========================================
    // COVER IMAGE
    // =========================================

    public String getCoverImage() {
        return coverImage;
    }


    public void setCoverImage(
            String coverImage
    ) {
        this.coverImage = coverImage;
    }


    // =========================================
    // STATUS
    // =========================================

    public String getStatus() {
        return status;
    }


    public void setStatus(
            String status
    ) {
        this.status = status;
    }


    // =========================================
    // AUTHOR
    // =========================================

    public String getAuthor() {
        return author;
    }


    public void setAuthor(
            String author
    ) {
        this.author = author;
    }


    // =========================================
    // ARTIST
    // =========================================

    public String getArtist() {
        return artist;
    }


    public void setArtist(
            String artist
    ) {
        this.artist = artist;
    }


    // =========================================
    // DEMOGRAPHIC
    // =========================================

    public String getDemographic() {
        return demographic;
    }


    public void setDemographic(
            String demographic
    ) {
        this.demographic = demographic;
    }


    // =========================================
    // TYPE
    // =========================================

    public String getType() {
        return type;
    }


    public void setType(
            String type
    ) {
        this.type = type;
    }


    // =========================================
    // RELEASE YEAR
    // =========================================

    public Integer getReleaseYear() {
        return releaseYear;
    }


    public void setReleaseYear(
            Integer releaseYear
    ) {
        this.releaseYear = releaseYear;
    }


    // =========================================
    // GENRES
    // =========================================

    public Set<Genre> getGenres() {
        return genres;
    }


    public void setGenres(
            Set<Genre> genres
    ) {
        this.genres = genres;
    }


    // =========================================
    // TAGS
    // =========================================

    public Set<Tag> getTags() {
        return tags;
    }


    public void setTags(
            Set<Tag> tags
    ) {
        this.tags = tags;
    }
}