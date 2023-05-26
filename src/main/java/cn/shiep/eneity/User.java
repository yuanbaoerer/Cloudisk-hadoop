package cn.shiep.eneity;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
public class User {

    private Integer id;
    private String userName;
    private String userPwd;


    public User() {
    }

    public User(Integer id, String userName, String userPwd) {
        this.id = id;
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}
