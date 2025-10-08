package model;

//User Class definition
public class User {
    //Class attributes
    private String username;
    private String password;
    private String displayName;
    private boolean online;

    //default constructor
    public User() {
        this.username = "";
        this.password = "";
        this.displayName = "";
        this.online = false;
    }

    //constructor that sets all attributes
    public User(String username, String password, String displayName, boolean online) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }

    //getters and setters for all attributes
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean getOnline() {
        return online;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    //equals() method
    @Override
    public boolean equals(Object o) {
        //check for reference equality
        if (this == o) return true;
        //check for null and class type equality
        if (o == null || getClass() != o.getClass()) return false;
        //cast the object and compare the unique attribute
        User user = (User) o;
        return username.equals(user.username);

    }
}
