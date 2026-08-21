package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Chapter;
import io.virinchi.mangak.model.Manga;
import io.virinchi.mangak.repository.MangaRepository;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MangaService {

    private final MangaRepository mangaRepository;


    public MangaService(
            MangaRepository mangaRepository
    ) {
        this.mangaRepository = mangaRepository;
    }


    // =========================================
    // GET ALL MANGA
    // =========================================

    public List<Manga> getAllManga() {

        return mangaRepository.findAll();
    }


    // =========================================
    // GET PAGINATED MANGA
    // =========================================

    public Page<Manga> getMangaPage(
            Pageable pageable
    ) {

        return mangaRepository.findAll(
                pageable
        );
    }


    // =========================================
    // GET MANGA BY ID
    // =========================================

    public Optional<Manga> getMangaById(
            Long id
    ) {

        return mangaRepository.findById(
                id
        );
    }


    // =========================================
    // GET MANGA BY EXACT TITLE
    // =========================================

    public Optional<Manga> getMangaByExactTitle(
            String title
    ) {

        return mangaRepository
                .findByTitleIgnoreCase(
                        title
                );
    }


    // =========================================
    // SAVE MANGA
    // =========================================

    public Manga saveManga(
            Manga manga
    ) {

        return mangaRepository.save(
                manga
        );
    }


    // =========================================
    // DELETE MANGA
    // =========================================

    public void deleteManga(
            Long id
    ) {

        mangaRepository.deleteById(
                id
        );
    }


    // =========================================
    // CHECK IF TITLE EXISTS
    // =========================================

    public boolean existsByTitle(
            String title
    ) {

        return mangaRepository
                .existsByTitle(
                        title
                );
    }


    // =========================================
    // SEARCH MANGA
    // =========================================

    public List<Manga> searchManga(
            String keyword,
            Long genreId,
            Long tagId,
            String status,
            String demographic,
            String type,
            Integer releaseYear,
            Integer minimumChapterCount
    ) {

        Specification<Manga> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction();


        // =========================================
        // SEARCH BY:
        // TITLE
        // AUTHOR
        // ARTIST
        // =========================================

        if (
                keyword != null
                        &&
                        !keyword.trim().isEmpty()
        ) {

            String searchKeyword =
                    "%"
                            + keyword
                            .trim()
                            .toLowerCase()
                            + "%";


            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->

                                    criteriaBuilder.or(


                                            // TITLE

                                            criteriaBuilder.like(
                                                    criteriaBuilder.lower(
                                                            root.get("title")
                                                    ),
                                                    searchKeyword
                                            ),


                                            // AUTHOR

                                            criteriaBuilder.like(
                                                    criteriaBuilder.lower(
                                                            root.get("author")
                                                    ),
                                                    searchKeyword
                                            ),


                                            // ARTIST

                                            criteriaBuilder.like(
                                                    criteriaBuilder.lower(
                                                            root.get("artist")
                                                    ),
                                                    searchKeyword
                                            )

                                    )
                    );
        }


        // =========================================
        // FILTER BY GENRE
        // =========================================

        if (genreId != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) -> {

                                query.distinct(
                                        true
                                );


                                return criteriaBuilder.equal(

                                        root.join(
                                                "genres"
                                        ).get(
                                                "id"
                                        ),

                                        genreId
                                );
                            }
                    );
        }


        // =========================================
        // FILTER BY TAG
        // =========================================

        if (tagId != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) -> {

                                query.distinct(
                                        true
                                );


                                return criteriaBuilder.equal(

                                        root.join(
                                                "tags"
                                        ).get(
                                                "id"
                                        ),

                                        tagId
                                );
                            }
                    );
        }


        // =========================================
        // FILTER BY STATUS
        // =========================================

        if (
                status != null
                        &&
                        !status.trim().isEmpty()
        ) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->

                                    criteriaBuilder.equal(

                                            criteriaBuilder.lower(
                                                    root.get(
                                                            "status"
                                                    )
                                            ),

                                            status
                                                    .trim()
                                                    .toLowerCase()
                                    )
                    );
        }


        // =========================================
        // FILTER BY DEMOGRAPHIC
        // =========================================

        if (
                demographic != null
                        &&
                        !demographic.trim().isEmpty()
        ) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->

                                    criteriaBuilder.equal(

                                            criteriaBuilder.lower(
                                                    root.get(
                                                            "demographic"
                                                    )
                                            ),

                                            demographic
                                                    .trim()
                                                    .toLowerCase()
                                    )
                    );
        }


        // =========================================
        // FILTER BY TYPE
        // =========================================

        if (
                type != null
                        &&
                        !type.trim().isEmpty()
        ) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->

                                    criteriaBuilder.equal(

                                            criteriaBuilder.lower(
                                                    root.get(
                                                            "type"
                                                    )
                                            ),

                                            type
                                                    .trim()
                                                    .toLowerCase()
                                    )
                    );
        }


        // =========================================
        // FILTER BY RELEASE YEAR
        // =========================================

        if (releaseYear != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->

                                    criteriaBuilder.equal(
                                            root.get(
                                                    "releaseYear"
                                            ),
                                            releaseYear
                                    )
                    );
        }


        // =========================================
        // FILTER BY MINIMUM CHAPTER COUNT
        // =========================================

        if (
                minimumChapterCount != null
                        &&
                        minimumChapterCount > 0
        ) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) -> {


                                // =========================================
                                // SUBQUERY:
                                // COUNT CHAPTERS FOR CURRENT MANGA
                                // =========================================

                                Subquery<Long> chapterCountSubquery =
                                        query.subquery(
                                                Long.class
                                        );


                                Root<Chapter> chapterRoot =
                                        chapterCountSubquery.from(
                                                Chapter.class
                                        );


                                chapterCountSubquery.select(
                                        criteriaBuilder.count(
                                                chapterRoot
                                        )
                                );


                                chapterCountSubquery.where(

                                        criteriaBuilder.equal(

                                                chapterRoot
                                                        .get("manga")
                                                        .get("id"),

                                                root.get(
                                                        "id"
                                                )
                                        )
                                );


                                // =========================================
                                // CHAPTER COUNT >= MINIMUM
                                // =========================================

                                return criteriaBuilder.greaterThanOrEqualTo(
                                        chapterCountSubquery,
                                        minimumChapterCount.longValue()
                                );
                            }
                    );
        }


        // =========================================
        // GET RESULTS
        // =========================================

        return mangaRepository.findAll(
                specification
        );
    }
}