package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.user.CreateUserDto;
import ru.otus.hw.services.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public String addUser(Model model, @Valid @ModelAttribute("userDto") CreateUserDto userDto, BindingResult bindingResult) {
        if (!userDto.password().equals(userDto.repeatPassword())) {
            model.addAttribute("error","Entered passwords do not match!");
           return "register";
        }
        return "redirect:/";
    }
}
