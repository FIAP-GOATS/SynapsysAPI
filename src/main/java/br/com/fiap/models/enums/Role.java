package br.com.fiap.models.enums;

public enum Role {
    CANDIDATE,
    COMPANY,
    ADMIN;

    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
