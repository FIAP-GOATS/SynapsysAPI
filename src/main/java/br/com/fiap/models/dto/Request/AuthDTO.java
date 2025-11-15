package br.com.fiap.models.dto.Request;

public class AuthDTO {
    private Long userId;
    private String role;

    public AuthDTO(Long userId, String role) {
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
