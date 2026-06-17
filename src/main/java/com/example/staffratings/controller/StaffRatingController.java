package com.example.staffratings.controller;

import com.example.staffratings.enums.RoleType;
import com.example.staffratings.model.StaffRating;
import com.example.staffratings.repository.StaffRatingRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StaffRatingController {

    private final StaffRatingRepository repository;

    public StaffRatingController(StaffRatingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("ratings", repository.findAll());
        return "index";
    }

    @GetMapping("/staff/new")
    public String newForm(Model model) {
        model.addAttribute("staffRating", new StaffRating());
        model.addAttribute("roleTypes", RoleType.values());
        return "create";
    }

    @PostMapping("/staff")
    public String create(@Valid StaffRating staffRating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roleTypes", RoleType.values());
            return "create";
        }
        try {
            repository.save(staffRating);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "duplicate", "A rating with this email already exists.");
            model.addAttribute("roleTypes", RoleType.values());
            return "create";
        }
        return "redirect:/";
    }

    @GetMapping("/staff/{id}")
    public String detail(@PathVariable Long id, Model model) {
        StaffRating rating = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff rating id: " + id));
        model.addAttribute("staffRating", rating);
        return "detail";
    }

    @GetMapping("/staff/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        StaffRating rating = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff rating id: " + id));
        model.addAttribute("staffRating", rating);
        model.addAttribute("roleTypes", RoleType.values());
        return "edit";
    }

    @PostMapping("/staff/{id}/edit")
    public String update(@PathVariable Long id, @Valid StaffRating staffRating,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            staffRating.setId(id);
            model.addAttribute("roleTypes", RoleType.values());
            return "edit";
        }
        staffRating.setId(id);
        try {
            repository.save(staffRating);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "duplicate", "A rating with this email already exists.");
            model.addAttribute("roleTypes", RoleType.values());
            return "edit";
        }
        return "redirect:/staff/" + id;
    }

    @PostMapping("/staff/{id}/delete")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}
