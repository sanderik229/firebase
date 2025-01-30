package com.example.firebasep7;

public class User {

    private String id;
    private String email;

    private String role;

    public User(){}

    public User(String id, String email, String role){
        this.role=role;
        this.id=id;
        this.email=email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
