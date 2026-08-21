package io.virinchi.mangak.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chapter_pages")
public class ChapterPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Lob
    @Column(
            name = "image_data",
            nullable = false,
            columnDefinition = "LONGBLOB"
    )
    private byte[] imageData;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "chapter_id",
            nullable = false
    )
    private Chapter chapter;


    public ChapterPage() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }


    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}