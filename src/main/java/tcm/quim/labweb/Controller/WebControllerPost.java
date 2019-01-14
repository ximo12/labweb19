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
import tcm.quim.labweb.DomainController.PostController;
import tcm.quim.labweb.Exception.Exception_General;
import tcm.quim.labweb.Repositories.PostRepository;
import tcm.quim.labweb.Repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class WebControllerPost {

    PostController postController;

    public WebControllerPost(PostController postController) {
        this.postController = postController;
    }


    /*
    CREATE POST_WEB
     */
    @GetMapping("newPost")
    public String newPostWeb(Model model) {
        try{
            model.addAttribute("Post_web", this.postController.getNewPost());
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

            this.postController.newPost(post_web, principal);

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

            Post_web post_web = this.postController.getPostToEditById(id, principal);

            model.addAttribute("Post_Web", post_web);

            return "postFormEdit";
        }catch (Exception e){
            throw new Exception_General("Error Editing post: " + e);
        }

    }



    @PostMapping("editPost/{id}")
    public String editPostWeb(@Valid Post_web post_web, Errors errors) {
        try {
            if (errors.hasErrors()) {
                return "postFormEdit";
            }

            this.postController.savePost(post_web);

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

            List<Post_web> post_webList = this.postController.getAllPosts(principal);

            model.addAttribute("postList", post_webList);
            return "/getPosts";
        } catch (Exception e){
            throw new Exception_General("Error Getting Posts: " + e);
        }

    }

    @GetMapping("getMyPosts")
    public String getMyPosts(Model model, Principal principal) {
        try{
            List<Post_web> post_webs = this.postController.getMyPosts(principal);
            model.addAttribute("postList", post_webs);
            return "/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Getting Posts:  " + e);
        }
    }

    @GetMapping("getPostById/{id}")
    public String getPostById(Model model, @PathVariable int id, Principal principal) {
        try{
            Post_web post_web = this.postController.getPostById(id, principal);

            model.addAttribute("post", post_web);
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
            model.addAttribute("post_delete", this.postController.getNewPost());
            return "postDeleteForm";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Post: " + e);
        }
    }

    @PostMapping("deletePost")
    public String deletePost(Post_web post_web, Principal principal) {
        try{

            this.postController.deletePost(post_web, principal);

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
            model.addAttribute("post_share", this.postController.getNewSharePost());
            return "postShareForm";
        }catch (Exception e){
            throw new Exception_General("Error Sharing Posts: " + e);
        }
    }

   @PostMapping("sharePost")
    public String sharePost(Shared_Post_web shared_post_web, Principal principal) {
        try{

            this.postController.sharePost(shared_post_web, principal);

            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Sharing Posts: " + e);
        }

    }

    @GetMapping("deleteSharePost")
    public String deleteSharePost(Model model) {
        try{
            model.addAttribute("share_post_delete", this.postController.getNewSharePost());
            return "sharePostDeleteForm";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Post: " + e);
        }
    }

    @PostMapping("deleteSharePost")
    public String deleteSharePost(Shared_Post_web shared_post_web, Principal principal) {
        try{

            this.postController.deleteSharePost(shared_post_web, principal);

            return "redirect:/getPosts";
        }catch (Exception e){
            throw new Exception_General("Error Deleting Shared Posts: " + e);
        }

    }















}
