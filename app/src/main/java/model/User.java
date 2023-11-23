package model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String password;
    private String position;

    public User() {

    }
    public User(int userId, String password, String position) {
        this.userId = userId;
        this.password = password;
        this.position = position;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (o.getClass() != this.getClass())
            return false;
        User user = (User) o;
        if (user.getUserId() == this.getUserId())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(userId) + " === " + password + " === " + position;
    }
}
