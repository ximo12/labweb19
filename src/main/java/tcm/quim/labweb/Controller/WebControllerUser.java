package tcm.quim.labweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.Repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class WebControllerUser {

    UserRepository userRepository;

    public WebControllerUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("editUser")
    public String editUserWeb(Model model, Principal principal) {
        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName(name);
        model.addAttribute("User_web", user_web);
        return "userForm";

    }


    @PostMapping("editUser")
    public String editUserWeb(@Valid User_web user_web, Errors errors) {
        if (errors.hasErrors()) {
            return "userForm";
        }

        User_web user_web1 = this.userRepository.getUserByUserName(user_web.getUsername());

        if (user_web.getId() != user_web1.getId()){
            return "userForm";
        }

        user_web.setDate_edit_To_Now();

        userRepository.updateUser(user_web);

        return "redirect:/editUser";
    }


    @PutMapping("addFriend/{userId}")
    public String addNewFriendd(Model model, Principal principal, int userId) {

        User_web user_web = userRepository.getUserByUserName(principal.getName());

        User_web user_web_2 = userRepository.getUserById(userId);

        user_web.addFriend(user_web_2);

        user_web_2.addFriend(user_web);

        userRepository.saveUser(user_web);
        userRepository.saveUser(user_web_2);

        model.addAttribute("User_web", user_web);
        return "myFriends";
    }









}
