package tcm.quim.labweb.DomainController;

import org.springframework.stereotype.Controller;
import tcm.quim.labweb.Domain.Friend_web;
import tcm.quim.labweb.Domain.Post_web;
import tcm.quim.labweb.Domain.Shared_Post_web;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.Exception.Exception_General;
import tcm.quim.labweb.Repositories.PostRepository;
import tcm.quim.labweb.Repositories.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {


    UserRepository userRepository;
    PostRepository postRepository;

    public UserController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public User_web getUser(Principal principal) {
        String name = principal.getName();
        return this.userRepository.getUserByUserName(name);
    }

    public void editUser(User_web user_web, Principal principal) {

        User_web user_web1 = this.getUser(principal);

        user_web1.setName(user_web.getName());
        user_web1.setSurname(user_web.getSurname());
        user_web1.setDate_birth(user_web.getDate_birth());
        user_web1.setMail(user_web.getMail());
        user_web1.setPhone(user_web.getPhone());
        user_web1.setDate_edit_To_Now();
        userRepository.updateUser(user_web1);
    }

    public List<User_web> getUsersThatImFriend(Principal principal) {

        User_web user_web = this.getUser(principal);

        //List of Users that I'm a friend
        return this.userRepository.getUsersThatImFriend(user_web);
    }

    public List<User_web> getMyFriends(Principal principal) {
        User_web user_web = this.getUser(principal);
        return this.userRepository.getMyFriends(user_web);
    }

    public Friend_web getNewFriend() {
        return new Friend_web();
    }

    public void addFriend(Friend_web friend_web, Principal principal) {
        User_web user_web = this.getUser(principal);

        friend_web.setUsername1(user_web.getUsername());

        if (!this.userRepository.existUserByUsername(friend_web.getUsername2())){
            throw new Exception_General("No exist Username");
        }

        User_web user_web1 = this.userRepository.getUserByUserName(friend_web.getUsername2());

        if (this.userRepository.existRelationFriend(user_web, user_web1)){
            throw new Exception_General("Users are friends");
        }

        this.userRepository.addNewFriend(user_web, user_web1);
    }

    public void deleteFriend(Friend_web friend_web, Principal principal) {
        User_web user_web = this.getUser(principal);

        //CHECK IF EXIST USER2
        if (!this.userRepository.existUserByUsername(friend_web.getUsername2())){
            throw new Exception_General("User not Exist");
        }

        User_web user_web1 = this.userRepository.getUserByUserName(friend_web.getUsername2());

        //CHECK IF EXIST RELATION
        if (!this.userRepository.existRelationFriend(user_web, user_web1)){
            throw new Exception_General("Users are not Friends");
        }

        Friend_web friendWeb = this.userRepository.getRelationFriend(user_web, user_web1);

        //GET ALL POSTS USER1
        List<Post_web> post_webList = this.postRepository.getMyPosts(user_web);

        //CHECK FOR ALL POSTS USER1 IF EXIST SHARE POST
        for (Post_web post_web:post_webList) {
            List<Shared_Post_web> shared_post_webs = this.postRepository.getAllSharedPostByPost(post_web);

            //FOR ALL SHARE_POST BY POST CHECK IF POST IS SHARED WITH USER TO DELETE FRIEND
            //IF EXIST SHARED POST, DELETE SHARED_POST
            for (Shared_Post_web shared_post_web: shared_post_webs) {
                if (!shared_post_web.getUsername().equals(user_web1.getUsername())){
                    this.postRepository.deleteSharedPostWeb(shared_post_web);
                }
            }
        }

        this.userRepository.deleteFriend(friendWeb);

    }
}
