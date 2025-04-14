package com.student.model;

public class User {
    private String username;
    private String passwordHash;
    private Role role;
    private boolean activated;

    public User(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.activated = true;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isActivated() { return activated; }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}