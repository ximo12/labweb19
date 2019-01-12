package tcm.quim.labweb.Domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Shared_Post_web {

    private int post_user_id;
    private String username;
    private int post_id;


    public Shared_Post_web(String username, int post_id) {
        this.username = username;
        this.post_id = post_id;
    }

    public Shared_Post_web() {
    }

    public Shared_Post_web(int post_user_id, String username, int post_id) {
        this.post_user_id = post_user_id;
        this.username = username;
        this.post_id = post_id;
    }

    public int getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(int post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
