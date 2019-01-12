package tcm.quim.labweb.Controller;

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

            if (!this.isOwner(user_web, post_web)){
                if (!this.userCanEdit(user_web, post_web)){
                    throw new Exception_General("You can't edit Post");
                }
            }

            model.addAttribute("Post_Web", post_web);

            return "postFormEdit";
        }catch (Exception e){
            throw new Exception_General("Error Editing post: " + e);
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

            List<Post_web> post_webList = this.postRepository.getMyPosts(user_web);

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

            if (!this.isOwner(user_web, post_web)){
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


    /*
    DELETE POSTS
    */
    @GetMapping("deletePost")
    public String deletePost(Model model) {
        try{
            model.addAttribute("post_delete", new Post_web());
            return "postDeleteForm";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Post: " + e);
        }
    }

    @PostMapping("deletePost")
    public String deletePost(Post_web post_web, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

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

            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Posts: " + e);
        }

    }



    /*
    SHARE POSTS
    */
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

            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Sharing Posts: " + e);
        }

    }

    @GetMapping("deleteSharePost")
    public String deleteSharePost(Model model) {
        try{
            model.addAttribute("share_post_delete", new Shared_Post_web());
            return "sharePostDeleteForm";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Post: " + e);
        }
    }

    @PostMapping("deleteSharePost")
    public String deleteSharePost(Shared_Post_web shared_post_web, Principal principal) {
        try{
            String name = principal.getName();
            User_web user_web = userRepository.getUserByUserName (name);

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

            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Shared Posts: " + e);
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










}
