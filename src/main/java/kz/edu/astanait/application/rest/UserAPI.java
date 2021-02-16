package kz.edu.astanait.application.rest;

import kz.edu.astanait.application.models.User;
import kz.edu.astanait.application.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserAPI {

    private final IUserService userService;

    @Autowired
    public UserAPI(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getByUsername/{username}")
    public @ResponseBody
    User getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    @GetMapping("/checkPasswordEquality/{password}")
    public @ResponseBody
    boolean isEqualPassword(@PathVariable String password,
                            HttpSession session){
        User user = (User) session.getAttribute("user");
        return userService.checkPasswordEquality(user.getPassword(), password);
    }
}
