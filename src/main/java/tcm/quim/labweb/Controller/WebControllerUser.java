package tcm.quim.labweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PutMapping;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.Repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class WebControllerUser {

    UserRepository userRepository;


    @PutMapping("editUser")
    public String editUserWeb(@Valid User_web user_web, Errors errors) {
        if (errors.hasErrors()) {
            return "userForm";
        }

        userRepository.saveUser(user_web);
        return "redirect:/userForm";
    }





}
