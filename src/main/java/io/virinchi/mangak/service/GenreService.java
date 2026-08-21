package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Genre;
import io.virinchi.mangak.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;


    public GenreService(
            GenreRepository genreRepository
    ) {
        this.genreRepository = genreRepository;
    }


    // =========================================
    // GET ALL GENRES
    // =========================================

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }


    // =========================================
    // GET GENRE BY ID
    // =========================================

    public Optional<Genre> getGenreById(
            Long id
    ) {
        return genreRepository.findById(id);
    }


    // =========================================
    // GET GENRE BY NAME
    // =========================================

    public Optional<Genre> getGenreByName(
            String name
    ) {
        return genreRepository.findByName(name);
    }


    // =========================================
    // SAVE GENRE
    // =========================================

    public Genre saveGenre(
            Genre genre
    ) {
        return genreRepository.save(genre);
    }


    // =========================================
    // DELETE GENRE
    // =========================================

    public void deleteGenre(
            Long id
    ) {
        genreRepository.deleteById(id);
    }


    // =========================================
    // CHECK IF GENRE EXISTS
    // =========================================

    public boolean existsByName(
            String name
    ) {
        return genreRepository.existsByName(name);
    }
}