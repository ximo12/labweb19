package tcm.quim.labweb.Domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User_web {

    private int id;
    private String username;
    private String name;
    private String surname;
    private String mail;
    private int phone;
    private LocalDateTime date_create;
    private LocalDateTime date_edit;
    private LocalDateTime date_birth;


    public User_web() {
    }

    public User_web(int id, String username, String name, String surname, String mail, int phone, LocalDateTime date_birth) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.phone = phone;
        this.date_birth = date_birth;
        this.date_create = LocalDateTime.now();
        this.date_edit = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    

    public void setDate_edit_To_Now() {
        this.setDate_edit(LocalDateTime.now());
    }
}
