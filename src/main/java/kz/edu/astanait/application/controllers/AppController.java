package kz.edu.astanait.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/session-expired")
    public String sessionExpired(Model model) {
        model.addAttribute("message","Your session is expired");
        model.addAttribute("title", "Session is expired");
        return "index";
    }
}
