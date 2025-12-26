package fr.but3.model;

public class Principal {

    private int userId;
    private String username;
    private String role;
    private String profileImage;

    public Principal(Integer userId, String username, String role, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.profileImage = profileImage;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}