package fr.but3.model;

public class Principal {

    private int userId;
    private String username;
    private String role;

    public Principal(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}