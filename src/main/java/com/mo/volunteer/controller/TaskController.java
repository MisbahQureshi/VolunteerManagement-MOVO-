package com.mo.volunteer.controller;

import com.mo.volunteer.entity.Event;
import com.mo.volunteer.entity.Task;
import com.mo.volunteer.entity.Volunteer;
import com.mo.volunteer.repo.EventRepository;
import com.mo.volunteer.repo.TaskRepository;
import com.mo.volunteer.repo.VolunteerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;
    private final VolunteerRepository volunteerRepository;

    public TaskController(TaskRepository taskRepository, EventRepository eventRepository,
            VolunteerRepository volunteerRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
        this.volunteerRepository = volunteerRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("events",
                eventRepository.findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now()));
        return "task-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        model.addAttribute("task", t);
        model.addAttribute("events",
                eventRepository.findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now()));

        if (t.getEvent() != null) {
            model.addAttribute("volunteers",
                    volunteerRepository.findByEvent_IdOrderByLastNameAsc(t.getEvent().getId()));
        }
        return "task-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Task task,
            @RequestParam("eventId") Long eventId,
            @RequestParam("volunteerId") Long volunteerId) {

        Event e = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        Volunteer v = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found: " + volunteerId));

        if (v.getEvent() == null || !v.getEvent().getId().equals(e.getId())) {
            throw new IllegalArgumentException("Selected volunteer is not in the selected event.");
        }

        task.setEvent(e);
        task.setVolunteer(v);

        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }

    // to fetchVolunteers for the dropdown
    @GetMapping("/volunteers")
    @ResponseBody
    public List<VolunteerOption> volunteersByEvent(@RequestParam("eventId") Long eventId) {
        return volunteerRepository.findByEvent_IdOrderByLastNameAsc(eventId)
                .stream()
                .map(v -> new VolunteerOption(
                        v.getId(),
                        v.getStudentId(),
                        v.getFirstName(),
                        v.getLastName()))
                .collect(Collectors.toList());
    }

    public record VolunteerOption(Long id, String studentId, String firstName, String lastName) {
    }
}
