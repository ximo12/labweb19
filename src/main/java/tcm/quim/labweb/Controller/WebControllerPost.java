package tcm.quim.labweb.Controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import tcm.quim.labweb.Domain.Post_web;
import tcm.quim.labweb.Domain.Shared_Post_web;
import tcm.quim.labweb.Domain.User_web;
import tcm.quim.labweb.Repositories.PostRepository;
import tcm.quim.labweb.Repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;

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
        model.addAttribute("Post_web", new Post_web());
        return "postForm";

    }

    @PostMapping("newPost")
    public String createPostWeb(@Valid Post_web post_web, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            return "postForm";
        }

        String username = principal.getName();

        User_web user_web = this.userRepository.getUserByUserName(username);

        postRepository.addNewPost(post_web, user_web);
        return "redirect:/getPosts";
    }


    /*
    EDIT POST_WEB
     */
    @GetMapping("editPost/{id}")
    public String editPostWeb(@PathVariable String id, Model model) {
        Post_web post_web = this.postRepository.getPostById(Integer.parseInt(id));
        model.addAttribute("Post_Web", post_web);

        return "postFormEdit";
    }

    @PostMapping("editPost/{id}")
    public String editPostWeb(@Valid Post_web post_web, Errors errors) {
        if (errors.hasErrors()) {
            return "postFormEdit";
        }

        post_web.setDate_Edit_Now();

        postRepository.savePost(post_web);
        return "redirect:/getPosts";
    }



    /*
    GET POST
     */
    @GetMapping("getPosts")
    public String getAllPosts(Model model, Principal principal) {
        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);
        model.addAttribute("postList", postRepository.getAllPosts(user_web));
       return "/getPosts";
    }

    @GetMapping("getMyPosts")
    public String getMyPosts(Model model, Principal principal) {
        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);
        model.addAttribute("postList", postRepository.getMyPosts(user_web));
        return "/getPosts";
    }

    @GetMapping("getPostById/{id}")
    public String getPostById(Model model, @PathVariable int id, Principal principal) {
        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);

        Post_web post_web;

        try {
            post_web = postRepository.getPostById(id);
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/getPosts";
        }

        if (!post_web.getOwner().getUsername().equals(user_web.getUsername())){
            return "redirect:/getPosts";
        }

        model.addAttribute("post", postRepository.getPostById(id));
        return "getPost";
    }



    //SHARE POSTS
    @GetMapping("sharePost")
    public String sharePost(Model model) {

        model.addAttribute("post_share", new Shared_Post_web());
        return "postShareForm";
    }


   @PostMapping("sharePost")
    public String sharePost(Shared_Post_web shared_post_web, Principal principal) {
        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);

        Post_web post_web;
        User_web user_web1;

        try {
            post_web = postRepository.getPostById(shared_post_web.getPost_id());
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/getPosts";
        }


        //user is owner?
        if (!post_web.getOwner().getUsername().equals(user_web.getUsername())){
            return "redirect:/getPosts";
        }

        //Check username exists
       try {
           user_web1 = this.userRepository.getUserByUserName(shared_post_web.getUsername());
       } catch (EmptyResultDataAccessException e) {
           return "redirect:/getPosts";
       }

       //TODO CKECK IF USER IS MY FRIEND

       //Check if shared exists
       try {
           this.postRepository.getSharedPostWeb(user_web1, post_web);
       } catch (EmptyResultDataAccessException e) {
           this.postRepository.addShare(shared_post_web);
       }

        return "redirect:/getPosts";
    }








}
