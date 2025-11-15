package br.com.fiap.authentication;

import io.jsonwebtoken.Claims;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class AuthUtil {

    public static AuthData extractUser(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token vazio")
                            .build()
            );
        }

        String token = authHeader.substring("Bearer ".length());

        Claims claims;
        try {
            claims = JwtUtil.validateAndGetClaims(token);
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token inválido")
                            .build()
            );
        }

        Long userId = Long.valueOf(claims.getSubject());
        String role = claims.get("role", String.class);

        return new AuthData(userId, role);
    }
}

