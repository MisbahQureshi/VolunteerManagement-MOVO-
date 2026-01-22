package com.mo.volunteer.controller;

import com.mo.volunteer.entity.Event;
import com.mo.volunteer.entity.Volunteer;
import com.mo.volunteer.repo.EventRepository;
import com.mo.volunteer.repo.VolunteerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/volunteers")
public class VolunteerController {

    private final VolunteerRepository volunteerRepository;
    private final EventRepository eventRepository;

    public VolunteerController(VolunteerRepository volunteerRepository, EventRepository eventRepository) {
        this.volunteerRepository = volunteerRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("volunteers", volunteerRepository.findAll());
        return "volunteers";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("volunteer", new Volunteer());
        model.addAttribute("events",
                eventRepository.findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now()));
        return "volunteer-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Volunteer v = volunteerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found: " + id));
        model.addAttribute("volunteer", v);
        model.addAttribute("events",
                eventRepository.findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now()));
        return "volunteer-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Volunteer volunteer, @RequestParam("eventId") Long eventId) {
        Event e = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        volunteer.setEvent(e);
        if (volunteer.getTimeIn() != null && volunteer.getTimeOut() != null
                && volunteer.getTimeOut().isBefore(volunteer.getTimeIn())) {
            throw new IllegalArgumentException("Time out cannot be before time in");
        }
        volunteerRepository.save(volunteer);
        return "redirect:/volunteers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        volunteerRepository.deleteById(id);
        return "redirect:/volunteers";
    }
}
