package br.com.fiap.authentication;

public class AuthData {
    private Long userId;
    private String role;

    public AuthData(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}

