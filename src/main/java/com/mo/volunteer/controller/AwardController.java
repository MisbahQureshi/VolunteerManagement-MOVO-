package com.mo.volunteer.controller;

import com.mo.volunteer.repo.AwardRow;
import com.mo.volunteer.repo.VolunteerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AwardController {

    private final VolunteerRepository volunteerRepository;

    public AwardController(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @GetMapping("/awards")
    public String awards(Model model) {

        LocalDateTime start = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime end   = start.plusYears(1);

        List<AwardRow> rows = volunteerRepository.awardRowsForYear(start, end, 20L * 60L);
        model.addAttribute("rows", rows);
        return "awards";
    }
}

