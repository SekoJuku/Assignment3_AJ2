package kz.edu.astanait.application.controllers;

import kz.edu.astanait.application.models.User;
import kz.edu.astanait.application.services.interfaces.ICardService;
import kz.edu.astanait.application.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final IUserService userService;
    private final ICardService cardService;

    @Autowired
    public UserController(IUserService userService, ICardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @GetMapping("/registration")
    public ModelAndView showRegistrationPage() {
        ModelAndView mav = new ModelAndView("registration");
        mav.addObject("userForm", new User());
        return mav;
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm) {
        userService.register(userForm);
        return "login";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("new-password") String newPassword,
                                 HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Long id = user.getId();

        userService.changePassword(newPassword,id);

        model.addAttribute("title","Password changed");
        model.addAttribute("message", "Password successfully changed");
        return "index";
    }

    @GetMapping("/profile")
    public String showProfilePage(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        model.addAttribute("cards", cardService.findByUserId(user.getId()));
        return "profile";
    }
}
