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
import tcm.quim.labweb.Exception.Exception_Post_Web;
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
        model.addAttribute("Post_web", new Post_web());
        return "postForm";

    }

    @PostMapping("newPost")
    public String createPostWeb(@Valid Post_web post_web, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            return "postForm";
        }

        throw new Exception_Post_Web("test error");
/*
        String username = principal.getName();

        User_web user_web = this.userRepository.getUserByUserName(username);

        postRepository.addNewPost(post_web, user_web);
        return "redirect:/getPosts";
        */
    }


    /*
    EDIT POST_WEB
     */
    @GetMapping("editPost/{id}")
    public String editPostWeb(@PathVariable String id, Model model, Principal principal) {
        String name = principal.getName();
        User_web user_web = userRepository.getUserByUserName (name);

        if (!this.postRepository.existPostById(id)){
            return "redirect:/getPosts";
        }

        Post_web post_web = this.postRepository.getPostById(Integer.parseInt(id));

        User_web user_web_owner = post_web.getOwner();

        if (!user_web.getUsername().equals(user_web_owner.getUsername())){
            if (!this.userCanEdit(user_web, user_web_owner, post_web)){
                return "redirect:/error";
            }
        }

        model.addAttribute("Post_Web", post_web);

        return "postFormEdit";
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

       if (!this.userRepository.existRelationFriend(user_web, user_web1)){
           return "error";
       }

       if (this.postRepository.existPostShared(user_web1, post_web)){
           return "redirect:/getPosts";
       }

       this.postRepository.addShare(shared_post_web);


        return "redirect:/getPosts";
    }








}
