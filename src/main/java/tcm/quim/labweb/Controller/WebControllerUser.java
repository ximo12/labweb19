package tcm.quim.labweb.Controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import tcm.quim.labweb.Domain.Friend_web;
import tcm.quim.labweb.Domain.Post_web;
import tcm.quim.labweb.Domain.Shared_Post_web;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.Exception.Exception_General;
import tcm.quim.labweb.Repositories.PostRepository;
import tcm.quim.labweb.Repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class WebControllerUser {

    UserRepository userRepository;
    PostRepository postRepository;

    public WebControllerUser(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName(name);
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

            User_web user_web1 = this.userRepository.getUserByUserName(principal.getName());

            user_web1.setName(user_web.getName());
            user_web1.setSurname(user_web.getSurname());
            user_web1.setDate_birth(user_web.getDate_birth());
            user_web1.setMail(user_web.getMail());
            user_web1.setPhone(user_web.getPhone());

            user_web1.setDate_edit_To_Now();

            userRepository.updateUser(user_web1);

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
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            //List of Users that I'm a friend
            List<User_web> usersThatImFriend = this.userRepository.getUsersThatImFriend(user_web);

            model.addAttribute("friends", usersThatImFriend);

            return "getFriends";
        }catch (Exception e){
            throw new Exception_General("Error Getting Friends: " + e);
        }

    }

    @GetMapping("getMyFriends")
    public String getMyFriends(Model model, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            List<User_web> friends = this.userRepository.getMyFriends(user_web);

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
            model.addAttribute("friend", new Friend_web());
            return "friendForm";
        }catch (Exception e){
            throw new Exception_General("Error Adding New Friend: " + e);
        }
    }

    @PostMapping("addFriend")
    public String sharePost(Friend_web friend_web, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            friend_web.setUsername1(user_web.getUsername());

            if (!this.userRepository.existUserByUsername(friend_web.getUsername2())){
                throw new Exception_General("No exist Username");
            }

            User_web user_web1 = this.userRepository.getUserByUserName(friend_web.getUsername2());

            if (this.userRepository.existRelationFriend(user_web, user_web1)){
                throw new Exception_General("Users are friends");
            }

            this.userRepository.addNewFriend(user_web, user_web1);

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
            model.addAttribute("friend_delete", new Friend_web());
            return "friendDeleteForm";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Friend: " + e);
        }
    }

    @PostMapping("deleteFriend")
    public String deleteFriend(Friend_web friend_web, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            if (!this.userRepository.existUserByUsername(friend_web.getUsername2())){
                throw new Exception_General("User not Exist");
            }

            User_web user_web1 = this.userRepository.getUserByUserName(friend_web.getUsername2());

            if (!this.userRepository.existRelationFriend(user_web, user_web1)){
                throw new Exception_General("Users are not Friends");
            }

            Friend_web friendWeb = this.userRepository.getRelationFriend(user_web, user_web1);

            List<Post_web> post_webList = this.postRepository.getMyPosts(user_web);

            for (Post_web post_web:post_webList) {
                List<Shared_Post_web> shared_post_webs = this.postRepository.getAllSharedPostByPost(post_web);

                for (Shared_Post_web shared_post_web: shared_post_webs) {
                    if (!shared_post_web.getUsername().equals(user_web1.getUsername())){
                        this.postRepository.deleteSharedPostWeb(shared_post_web);
                    }
                }
            }

            this.userRepository.deleteFriend(friendWeb);

            return "redirect:/getMyFriends";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Friend: " + e);
        }

    }








}
