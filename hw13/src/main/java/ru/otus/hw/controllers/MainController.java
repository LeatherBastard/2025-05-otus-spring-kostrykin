package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.user.CreateUserDto;
import ru.otus.hw.services.user.UserService;



@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

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
        model.addAttribute("userDto", new CreateUserDto("", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String addUser(Model model, @Valid @ModelAttribute("userDto") CreateUserDto userDto,
                          BindingResult bindingResult) {
        if (!userDto.password().equals(userDto.repeatPassword())) {
            model.addAttribute("error", "Entered passwords do not match!");
            return "register";
        }
        userService.insert(userDto);
        return "redirect:/";
    }
}
