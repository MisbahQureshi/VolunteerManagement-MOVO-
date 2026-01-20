package com.mo.volunteer.controller;

import com.mo.volunteer.entity.Volunteer;
import com.mo.volunteer.repo.VolunteerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/volunteers")
public class VolunteerController {

    private final VolunteerRepository volunteerRepository;

    public VolunteerController(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @GetMapping
    public String list(Model model){
        model.addAttribute("volunteers", volunteerRepository.findAll());
        return "volunteers";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("volunteer", new Volunteer());
        return "volunteer-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Volunteer v = volunteerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found: " + id));
        model.addAttribute("volunteer", v);
        return "volunteer-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Volunteer volunteer) {
        volunteerRepository.save(volunteer);
        return "redirect:/volunteers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        volunteerRepository.deleteById(id);
        return "redirect:/volunteers";
    }
}
