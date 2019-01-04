package tcm.quim.labweb.Controller;

import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.List;

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
    public String editUserWeb(@Valid User_web user_web, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            return "userForm";
        }

        User_web user_web1 = this.userRepository.getUserByUserName(principal.getName());

        user_web1.setName(user_web.getName());
        user_web1.setSurname(user_web.getSurname());
        user_web1.setDate_birth(user_web.getDate_birth());
        user_web1.setMail(user_web.getMail());
        user_web1.setPhone(user_web.getPhone());

        user_web1.setDate_edit_To_Now();

        userRepository.updateUser(user_web1);

        return "redirect:/editUser";
    }


    @GetMapping("getUsersThatImFriend")
    public String getUsersThatImFriend(Model model, Principal principal) {

        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);

        //List of Users that I'm a friend
        List<User_web> usersThatImFriend = this.userRepository.getUsersThatImFriend(user_web);

        model.addAttribute("friends", usersThatImFriend);

        return "getFriends";
    }

    @GetMapping("getMyFriends")
    public String getMyFriends(Model model, Principal principal) {

        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);

        List<User_web> friends = this.userRepository.getMyFriends(user_web);

        model.addAttribute("friends", friends);

        return "getFriends";
    }

    








}
