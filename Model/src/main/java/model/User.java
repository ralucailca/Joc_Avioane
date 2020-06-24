package model;

import java.io.Serializable;
import java.util.Objects;

public class User extends Entity<String> implements Serializable {
    private String username;
    private String password;

    public User(String user, String password) {
        this.username = user;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String s) {
        super.setId(s);
    }

    @Override
    public String toString() {
        return "Jucator{" +
                "user='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUser() {
        return username;
    }

    public void setUser(String user) {
        this.username = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
