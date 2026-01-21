package com.mo.volunteer.controller;

import com.mo.volunteer.entity.Event;
import com.mo.volunteer.repo.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @GetMapping
    public String list(Model model){
        model.addAttribute("events", eventRepository.findAll());
        return "events";
    }

    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("event",new Event());
        return "event-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        model.addAttribute("event",e);
        return "event-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/events";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/events";
    }

}
