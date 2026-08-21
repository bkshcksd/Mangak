package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.Tag;
import io.virinchi.mangak.service.TagService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class NavbarModelAdvice {

    private final TagService tagService;


    public NavbarModelAdvice(
            TagService tagService
    ) {
        this.tagService = tagService;
    }


    // =========================================
    // TAGS FOR NAVBAR
    // =========================================

    @ModelAttribute("navbarTags")
    public List<Tag> navbarTags() {

        return tagService.getAllTags();
    }
}