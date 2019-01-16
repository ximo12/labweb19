package tcm.quim.labweb.DomainController;

import org.springframework.stereotype.Controller;
import tcm.quim.labweb.Domain.Post_web;
import tcm.quim.labweb.Domain.Shared_Post_web;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.Exception.Exception_General;
import tcm.quim.labweb.Repositories.PostRepository;
import tcm.quim.labweb.Repositories.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {

    UserRepository userRepository;
    PostRepository postRepository;
    UserController userController;

    public PostController(UserRepository userRepository, PostRepository postRepository, UserController userController) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userController = userController;
    }

    public Post_web getNewPost() {
        return new Post_web();
    }

    public void newPost(Post_web post_web, Principal principal) {

        User_web user_web = this.userController.getUser(principal);

        postRepository.addNewPost(post_web, user_web);
    }

    public Post_web getPostToEditById(int id, Principal principal) {
        User_web user_web = this.userController.getUser(principal);

        if (!this.postRepository.existPostById(id)){
            throw new Exception_General("Post Id not Exist");
        }

        Post_web post_web = this.postRepository.getPostById(id);

        if (!this.isOwner(user_web, post_web)){
            if (!this.userCanEdit(user_web, post_web)){
                throw new Exception_General("You can't edit Post");
            }
        }


        return post_web;
    }

    public void savePost(Post_web post_web) {
        post_web.setDate_Edit_Now();
        this.postRepository.savePost(post_web);
    }

    public List<Post_web> getAllPosts(Principal principal) {

        User_web user_web = this.userController.getUser(principal);

        //GET ALL MY POSTS
        List<Post_web> post_webList = this.postRepository.getMyPosts(user_web);

        //GET ALL USERS THAT IM FRIEND
        List<User_web> friend_webList = userRepository.getUsersThatImFriend(user_web);

        //FOR ALL USERS, GET POSTS
        for (User_web user_web1: friend_webList) {
            List<Post_web> post_webList1 = postRepository.getMyPostsForOtherUser(user_web1);
            post_webList.addAll(post_webList1);
        }
        return post_webList;
    }

    public List<Post_web> getMyPosts(Principal principal) {
        User_web user_web = this.userController.getUser(principal);
        List<Post_web> post_webs = this.postRepository.getMyPosts(user_web);
        return post_webs;
    }

    public Post_web getPostById(int id, Principal principal) {
        User_web user_web = this.userController.getUser(principal);

        if(!this.postRepository.existPostById(id)){
            throw new Exception_General("Id Post no Exist");
        }

        Post_web post_web = this.postRepository.getPostById(id);

        if (!this.isOwner(user_web, post_web)){
            if (!this.userRepository.existRelationFriend(post_web.getOwner(), user_web)){
                throw new Exception_General("User can view this Post");
            }
        }
        return post_web;
    }


    private Boolean isOwner (User_web user_web, Post_web post_web){
        if (post_web.getOwner().getUsername().equals(user_web.getUsername())){
            return true;
        }
        return false;
    }

    private void deleteAllPostsSharedByPost (Post_web post_web){
        List<Shared_Post_web> shared_post_webs = this.postRepository.getAllSharedPostByPost(post_web);

        for (Shared_Post_web shared_post_web: shared_post_webs) {
            this.postRepository.deleteSharedPostWeb(shared_post_web);
        }

    }

    private Boolean userCanEdit (User_web user_web, Post_web post_web){

        User_web user_web_owner = this.userRepository.getUserByUserName(post_web.getOwner().getUsername());

        if (!this.userRepository.existRelationFriend(user_web_owner, user_web)){
            return false;
        }

        if (!this.postRepository.existPostSharedUserPost(user_web, post_web)){
            return false;
        }

        return true;

    }


    public void deletePost(Post_web post_web, Principal principal) {

        User_web user_web = this.userController.getUser(principal);

        if (!this.postRepository.existPostById(post_web.getId())){
            throw new Exception_General("Post Id not Exist");
        }

        Post_web post_web_toUse = this.postRepository.getPostById(post_web.getId());

        if (!this.isOwner(user_web, post_web_toUse)){
            throw new Exception_General("User is not Owner");
        }

        if (this.postRepository.existPostShared(post_web)){
            this.deleteAllPostsSharedByPost(post_web);
        }

        this.postRepository.deletePost(post_web);

    }

    public Shared_Post_web getNewSharePost() {
        return new Shared_Post_web();
    }

    public void sharePost(Shared_Post_web shared_post_web, Principal principal) {

        User_web user_web = this.userController.getUser(principal);

        if (!this.postRepository.existPostById(shared_post_web.getPost_id())){
            throw new Exception_General("Post Id no Exist");
        }

        Post_web post_web = this.postRepository.getPostById(shared_post_web.getPost_id());

        //user is owner?
        if (!this.isOwner(user_web, post_web)){
            throw new Exception_General("User is not Owner");
        }

        //Check username exists
        if (!this.userRepository.existUserByUsername(shared_post_web.getUsername())){
            throw new Exception_General("User not Exist");
        }

        User_web user_web1 = this.userRepository.getUserByUserName(shared_post_web.getUsername());

        if (!this.userRepository.existRelationFriend(user_web, user_web1)){
            throw new Exception_General("No exist relationship");
        }

        if (this.postRepository.existPostSharedUserPost(user_web1, post_web)){
            throw new Exception_General("Post is shared");
        }

        this.postRepository.addShare(shared_post_web);

    }

    public void deleteSharePost(Shared_Post_web shared_post_web, Principal principal) {

        User_web user_web = this.userController.getUser(principal);

        if (!this.postRepository.existPostById(shared_post_web.getPost_id())){
            throw new Exception_General("Post Id not Exist");
        }

        if (!this.userRepository.existUserByUsername(shared_post_web.getUsername())) {
            throw new Exception_General("Post Id not Exist");
        }

        Post_web post_web = this.postRepository.getPostById(shared_post_web.getPost_id());

        User_web user_web1 = this.userRepository.getUserByUserName(shared_post_web.getUsername());

        if (!this.isOwner(user_web, post_web)){
            throw new Exception_General("User is not Owner");
        }

        if (!this.postRepository.existPostSharedUserPost(user_web1, post_web)){
            throw new Exception_General("Shared Post no exist");
        }

        Shared_Post_web shared_post_web1 = this.postRepository.getSharedPostWeb(user_web1, post_web);

        this.postRepository.deleteSharedPostWeb(shared_post_web1);

    }
}
