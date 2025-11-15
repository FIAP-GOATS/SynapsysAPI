package br.com.fiap.authentication;

import br.com.fiap.models.dto.Request.AuthDTO;
import io.jsonwebtoken.Claims;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/// Utility class for extracting user information from JWT tokens in HTTP Authorization headers.
public class AuthUtil {

    public static AuthDTO extractUser(String authHeader) {

        /// Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("status", "error", "error", "Token vazio"))
                            .build()
            );
        }

        /// Extract the token from the Authorization header.
        String token = authHeader.substring("Bearer ".length());

        /// Validate the token and extract claims.
        Claims claims;
        try {
            claims = JwtUtil.validateAndGetClaims(token);
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("status", "error", "error", "Token inválido"))
                            .build()
            );
        }

        /// Extract user ID and role from the claims.
        Long userId = Long.valueOf(claims.getSubject());
        String role = claims.get("role", String.class);

        /// Return an AuthDTO containing the user ID and role.
        return new AuthDTO(userId, role);
    }
}

