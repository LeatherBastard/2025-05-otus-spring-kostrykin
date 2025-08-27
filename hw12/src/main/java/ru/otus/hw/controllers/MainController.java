package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.user.CreateUserDto;

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
    public String registerPage(Model model,@Valid @ModelAttribute("userDto") CreateUserDto uDto)
    {
        return "register";
    }
}
