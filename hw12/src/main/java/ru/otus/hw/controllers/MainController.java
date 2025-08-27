package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/")
    public String mainPage(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", null);
        return "register";
    }
}
