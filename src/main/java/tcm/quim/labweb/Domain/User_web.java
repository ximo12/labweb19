package tcm.quim.labweb.Domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User_web {

    private int id;
    private String name;
    private String surname;
    private String mail;
    private int phone;
    private LocalDateTime date_create;
    private LocalDateTime date_edit;
    private LocalDateTime date_birth;

    private List friends;
    private List post_owner;
    private List post_share;

    public User_web(String name, String surname, String mail, int phone, LocalDateTime date_birth) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.phone = phone;
        this.date_birth = date_birth;
        this.friends = new ArrayList<User_web>();
        this.post_owner = new ArrayList<Post_web>();
        this.post_share = new ArrayList<Post_web>();
        this.date_create = LocalDateTime.now();
        this.date_edit = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.setDate_editNow();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        this.setDate_editNow();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
        this.setDate_editNow();
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
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

    public LocalDateTime getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(LocalDateTime date_birth) {
        this.date_birth = date_birth;
        this.setDate_editNow();
    }

    public List getFriends() {
        return friends;
    }

    public void addFriend(User_web friend) {
        if (!friends.contains(friend)){
            this.friends.add(friend);
            this.setDate_editNow();
        }
    }

    public void removeFriend(User_web friend) {
        if (friends.contains(friend)){
            this.friends.remove(friend);
            this.setDate_editNow();
        }
    }

    public List getPost_owner() {
        return post_owner;
    }

    public void addPost_owner(Post_web post_owner) {
        if (!this.post_owner.contains(post_owner)){
            this.post_owner.add(post_owner);
            this.setDate_editNow();
        }
    }

    public void removePost_owner(Post_web post_owner) {
        if (this.post_owner.contains(post_owner)){
            this.post_owner.add(post_owner);
            this.setDate_editNow();
        }
    }

    public List getPost_share() {
        return post_share;
    }

    public void addPost_share(Post_web post_share) {
        if (!this.post_share.contains(post_share)){
            this.post_share.remove(post_share);
            this.setDate_editNow();
        }
    }

    public void removePost_share(Post_web post_share) {
        if (this.post_share.contains(post_share)){
            this.post_share.remove(post_share);
            this.setDate_editNow();
        }
    }

}
