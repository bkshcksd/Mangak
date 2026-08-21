package io.virinchi.mangak.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "tags",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "name"
                )
        }
)
public class Tag {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    @Column(
            nullable = false,
            unique = true
    )
    private String name;


    @ManyToMany(
            mappedBy = "tags"
    )
    private Set<Manga> mangaList =
            new HashSet<>();


    public Tag() {
    }


    public Tag(
            String name
    ) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }


    public void setId(
            Long id
    ) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(
            String name
    ) {
        this.name = name;
    }


    public Set<Manga> getMangaList() {
        return mangaList;
    }


    public void setMangaList(
            Set<Manga> mangaList
    ) {
        this.mangaList = mangaList;
    }
}