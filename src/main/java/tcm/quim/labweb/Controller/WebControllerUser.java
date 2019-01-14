package tcm.quim.labweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tcm.quim.labweb.Domain.Friend_web;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.DomainController.UserController;
import tcm.quim.labweb.Exception.Exception_General;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class WebControllerUser {

    UserController userController;

    public WebControllerUser(UserController userController) {
        this.userController = userController;
    }

    @GetMapping("index")
    public String index() {
        try{
            return "redirect:/index";
        }catch (Exception e){
            throw new Exception_General("Error general" + e);
        }
    }

    /*
    EDIT
     */
    @GetMapping("editUser")
    public String editUserWeb(Model model, Principal principal) {
        try{
            User_web user_web = this.userController.getUser(principal);
            model.addAttribute("User_web", user_web);
            return "userForm";
        }catch (Exception e){
            throw new Exception_General("Error Editing User: " + e);
        }
    }

    @PostMapping("editUser")
    public String editUserWeb(@Valid User_web user_web, Errors errors, Principal principal) {
        try{
            if (errors.hasErrors()) {
                return "userForm";
            }

            this.userController.editUser(user_web, principal);

            return "redirect:/editUser";
        }catch (Exception e){
            throw new Exception_General("Error Editing User: " + e);
        }
    }


    /*
    GET FRIEND
     */
    @GetMapping("getUsersThatImFriend")
    public String getUsersThatImFriend(Model model, Principal principal) {
        try{

            List<User_web> usersThatImFriend = this.userController.getUsersThatImFriend(principal);

            model.addAttribute("friends", usersThatImFriend);

            return "getFriends";
        }catch (Exception e){
            throw new Exception_General("Error Getting Friends: " + e);
        }

    }

    @GetMapping("getMyFriends")
    public String getMyFriends(Model model, Principal principal) {
        try{

            List<User_web> friends = this.userController.getMyFriends(principal);
            model.addAttribute("friends", friends);

            return "getFriends";
        }catch (Exception e){
            throw new Exception_General("Error Getting Friends: " + e);
        }
    }


    /*
    ADD FRIEND
     */
    @GetMapping("addFriend")
    public String sharePost(Model model) {
        try{
            model.addAttribute("friend", this.userController.getNewFriend());
            return "friendForm";
        }catch (Exception e){
            throw new Exception_General("Error Adding New Friend: " + e);
        }
    }

    @PostMapping("addFriend")
    public String sharePost(Friend_web friend_web, Principal principal) {
        try{

            this.userController.addFriend(friend_web, principal);

            return "redirect:/getMyFriends";
        }catch (Exception e){
            throw new Exception_General("Error Adding New Friend: " + e);
        }
    }


    /*
    DELETE FRIEND
     */
    @GetMapping("deleteFriend")
    public String deleteFriend(Model model) {
        try{
            model.addAttribute("friend_delete", this.userController.getNewFriend());
            return "friendDeleteForm";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Friend: " + e);
        }
    }

    @PostMapping("deleteFriend")
    public String deleteFriend(Friend_web friend_web, Principal principal) {
        try{

            this.userController.deleteFriend(friend_web, principal);

            return "redirect:/getMyFriends";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Friend: " + e);
        }

    }








}
