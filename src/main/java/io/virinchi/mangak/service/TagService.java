package io.virinchi.mangak.service;

import io.virinchi.mangak.model.Tag;
import io.virinchi.mangak.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;


    public TagService(
            TagRepository tagRepository
    ) {
        this.tagRepository = tagRepository;
    }


    // =========================================
    // GET ALL TAGS
    // =========================================

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }


    // =========================================
    // GET TAG BY ID
    // =========================================

    public Optional<Tag> getTagById(
            Long id
    ) {
        return tagRepository.findById(id);
    }


    // =========================================
    // GET TAG BY NAME
    // =========================================

    public Optional<Tag> getTagByName(
            String name
    ) {
        return tagRepository.findByName(name);
    }


    // =========================================
    // SAVE TAG
    // =========================================

    public Tag saveTag(
            Tag tag
    ) {
        return tagRepository.save(tag);
    }


    // =========================================
    // DELETE TAG
    // =========================================

    public void deleteTag(
            Long id
    ) {
        tagRepository.deleteById(id);
    }


    // =========================================
    // CHECK IF TAG EXISTS
    // =========================================

    public boolean existsByName(
            String name
    ) {
        return tagRepository.existsByName(name);
    }
}