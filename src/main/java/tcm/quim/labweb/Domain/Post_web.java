package tcm.quim.labweb.Domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post_web {

    private int id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 4, max = 100, message = "Title must be between 4 an 100 characters long")
    private String title;

    @NotNull(message = "Text cannot be null")
    @Size(min = 4, max = 5000, message = "Title must be between 4 an 5000 characters long")
    private String text;
    private Boolean is_public;
    private LocalDateTime date_create;
    private LocalDateTime date_edit;

    private User_web owner;

    private List user_share;

    public Post_web() {
        this.date_create = LocalDateTime.now();
        this.date_edit = LocalDateTime.now();
        this.user_share = new ArrayList<User_web>();
    }

    public Post_web(int id, @NotNull(message = "Title cannot be null") @Size(min = 4, max = 100, message = "Title must be between 4 an 100 characters long") String title, @NotNull(message = "Text cannot be null") @Size(min = 4, max = 5000, message = "Title must be between 4 an 5000 characters long") String text, Boolean is_public, LocalDateTime date_create, LocalDateTime date_edit, User_web owner) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.is_public = is_public;
        this.date_create = date_create;
        this.date_edit = date_edit;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.setDate_editNow();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.setDate_editNow();
    }

    public LocalDateTime getDate_create() {
        return date_create;
    }

    public LocalDateTime getDate_edit() {
        return date_edit;
    }

    private void setDate_editNow() {
        this.date_edit = LocalDateTime.now();
    }

    public User_web getOwner() {
        return owner;
    }

    public Boolean getIs_public() {
        return is_public;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate_create(LocalDateTime date_create) {
        this.date_create = date_create;
    }

    public void setDate_edit(LocalDateTime date_edit) {
        this.date_edit = date_edit;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
        this.setDate_editNow();
    }

    public List getUser_share() {
        return user_share;
    }

    public void addUser_share(User_web user_share) {
        if (!this.user_share.contains(user_share)){
            this.user_share.add(user_share);
            this.setDate_editNow();
        }

    }

    public void setOwner(User_web owner) {
        this.owner = owner;
    }

    public void removeUser_share(User_web user_share) {
        if (this.user_share.contains(user_share)){
            this.user_share.remove(user_share);
            this.setDate_editNow();
        }

    }


    public void setDate_Edit_Now() {
        this.setDate_edit(LocalDateTime.now());
    }
}
