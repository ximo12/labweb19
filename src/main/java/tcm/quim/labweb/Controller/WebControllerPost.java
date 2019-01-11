package tcm.quim.labweb.Controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class WebControllerPost {

    PostRepository postRepository;
    UserRepository userRepository;

    public WebControllerPost(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    /*
    CREATE POST_WEB
     */
    @GetMapping("newPost")
    public String newPostWeb(Model model) {
        try{
            model.addAttribute("Post_web", new Post_web());
            return "postForm";
        }catch (Exception e){
            throw new Exception_General("Error creating new Post: " + e);
        }

    }

    @PostMapping("newPost")
    public String createPostWeb(@Valid Post_web post_web, Errors errors, Principal principal) {
        try{
            if (errors.hasErrors()) {
                return "postForm";
            }

            String username = principal.getName();

            User_web user_web = this.userRepository.getUserByUserName(username);

            postRepository.addNewPost(post_web, user_web);
            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error creating New post: " + e);
        }
    }


    /*
    EDIT POST_WEB
     */
    @GetMapping("editPost/{id}")
    public String editPostWeb(@PathVariable int id, Model model, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            if (!this.postRepository.existPostById(id)){
                throw new Exception_General("Post Id not Exist");
            }

            Post_web post_web = this.postRepository.getPostById(id);

            User_web user_web_owner = post_web.getOwner();

            if (!user_web.getUsername().equals(user_web_owner.getUsername())){
                if (!this.userCanEdit(user_web, user_web_owner, post_web)){
                    throw new Exception_General("You can't edit Post");
                }
            }

            model.addAttribute("Post_Web", post_web);

            return "postFormEdit";
        }catch (Exception e){
            throw new Exception_General("Error Editing post: " + e);
        }

    }

    private Boolean userCanEdit (User_web user_web, User_web user_web_owner, Post_web post_web){

        if (!this.userRepository.existRelationFriend(user_web_owner, user_web)){
            return false;
        }

        if (!this.postRepository.existPostShared(user_web, post_web)){
            return false;
        }

        return true;

    }

    @PostMapping("editPost/{id}")
    public String editPostWeb(@Valid Post_web post_web, Errors errors) {
        try {
            if (errors.hasErrors()) {
                return "postFormEdit";
            }

            post_web.setDate_Edit_Now();

            postRepository.savePost(post_web);
            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Editing post: " + e);
        }
            }



    /*
    GET POST
     */
    @GetMapping("getPosts")
    public String getAllPosts(Model model, Principal principal) {
        try {
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            List<Post_web> post_webList = postRepository.getAllPostsSharedWithUser(user_web);

            List<Post_web> post_web_OwnerList = postRepository.getMyPosts(user_web);

            for (Post_web post_web: post_web_OwnerList) {
                if (!this.compareIfPostIsInArray(post_webList, post_web)){
                    post_webList.add(post_web);
                }
            }

            List<User_web> friend_webList = userRepository.getUsersThatImFriend(user_web);

            for (User_web user_web1: friend_webList) {
                List<Post_web> post_webList1 = postRepository.getMyPosts(user_web1);

                for (Post_web post_web2: post_webList1) {
                    if (!this.compareIfPostIsInArray(post_webList, post_web2)){
                        post_webList.add(post_web2);
                    }
                }

            }

            model.addAttribute("postList", post_webList);
            return "/getPosts";
        } catch (Exception e){
            throw new Exception_General("Error Getting Posts: " + e);
        }

    }

    private boolean compareIfPostIsInArray(List<Post_web> post_webList, Post_web post_web) {

        for (Post_web post1: post_webList) {
            if (post1.getId() == post_web.getId()){
                return true;
            }
        }
        return false;

    }


    @GetMapping("getMyPosts")
    public String getMyPosts(Model model, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);
            model.addAttribute("postList", postRepository.getMyPosts(user_web));
            return "/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Getting Posts:  " + e);
        }
    }

    @GetMapping("getPostById/{id}")
    public String getPostById(Model model, @PathVariable int id, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            if(!this.postRepository.existPostById(id)){
                throw new Exception_General("Id Post no Exist");
            }

            Post_web post_web = this.postRepository.getPostById(id);

            if (!post_web.getOwner().getUsername().equals(user_web.getUsername())){
                if (!this.userRepository.existRelationFriend(post_web.getOwner(), user_web)){
                    throw new Exception_General("User can view this Post");
                }
            }

            model.addAttribute("post", postRepository.getPostById(id));
            return "getPost";
        }catch (Exception e){
            throw new Exception_General("Error Getting Posts: ");
        }
    }



    //SHARE POSTS
    @GetMapping("sharePost")
    public String sharePost(Model model) {
        try{
            model.addAttribute("post_share", new Shared_Post_web());
            return "postShareForm";
        }catch (Exception e){
            throw new Exception_General("Error Sharing Posts: " + e);
        }
    }


   @PostMapping("sharePost")
    public String sharePost(Shared_Post_web shared_post_web, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

            if (!this.postRepository.existPostById(shared_post_web.getPost_id())){
                throw new Exception_General("Post Id no Exist");
            }

            Post_web post_web = this.postRepository.getPostById(shared_post_web.getPost_id());

            //user is owner?
            if (!post_web.getOwner().getUsername().equals(user_web.getUsername())){
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

            if (this.postRepository.existPostShared(user_web1, post_web)){
                throw new Exception_General("Post is already shared");
            }

            this.postRepository.addShare(shared_post_web);

            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Sharing Posts: " + e);
        }

    }








}
