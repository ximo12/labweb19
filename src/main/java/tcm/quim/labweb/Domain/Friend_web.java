package tcm.quim.labweb.Domain;

public class Friend_web {

    private int id;
    private String username1;
    private String username2;


    public Friend_web(int id, String username1,String username2) {
        this.id = id;
        this.username1 = username1;
        this.username2 = username2;
    }

    public Friend_web() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }
}
