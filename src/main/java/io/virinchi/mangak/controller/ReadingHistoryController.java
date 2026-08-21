package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.ReadingHistory;
import io.virinchi.mangak.service.ReadingHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReadingHistoryController {

    private final ReadingHistoryService readingHistoryService;


    public ReadingHistoryController(
            ReadingHistoryService readingHistoryService
    ) {
        this.readingHistoryService =
                readingHistoryService;
    }


    // =========================================
    // READING HISTORY PAGE
    // =========================================

    @GetMapping("/reading-history")
    public String readingHistory(
            HttpSession session,
            Model model
    ) {

        Long userId =
                (Long) session.getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        List<ReadingHistory> historyList =
                readingHistoryService
                        .getUserHistory(userId);


        model.addAttribute(
                "historyList",
                historyList
        );


        model.addAttribute(
                "username",
                session.getAttribute("username")
        );


        model.addAttribute(
                "role",
                session.getAttribute("role")
        );


        return "reading-history";
    }


    // =========================================
    // REMOVE ONE HISTORY ITEM
    // =========================================

    @PostMapping("/reading-history/remove/{mangaId}")
    public String removeHistory(
            @PathVariable Long mangaId,
            HttpSession session
    ) {

        Long userId =
                (Long) session.getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        readingHistoryService.removeHistory(
                userId,
                mangaId
        );


        return "redirect:/reading-history";
    }


    // =========================================
    // CLEAR ALL HISTORY
    // =========================================

    @PostMapping("/reading-history/clear")
    public String clearHistory(
            HttpSession session
    ) {

        Long userId =
                (Long) session.getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        readingHistoryService.clearHistory(
                userId
        );


        return "redirect:/reading-history";
    }
}