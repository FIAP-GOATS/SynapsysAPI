package br.com.fiap.controllers;

import br.com.fiap.authentication.JwtUtil;
import br.com.fiap.authentication.PasswordUtil;
import br.com.fiap.models.dto.Request.LoginDTO;
import br.com.fiap.models.entities.User;
import br.com.fiap.models.repositories.UserRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Map;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginController {

    public LoginController() throws SQLException {
    }

    UserRepository userRepository = new UserRepository();

    @POST
    public Response login(LoginDTO loginDTO) throws SQLException {
        try {
            User user = userRepository.getUserByEmail(loginDTO.getEmail());

            if (user == null) {
                return Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("status", "error", "message", "Email ou senha inválidos"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            boolean isPasswordValid = PasswordUtil.checkPassword(loginDTO.getPassword(), user.getPassword());

            if (!loginDTO.getEmail().equals(user.getEmail()) || !isPasswordValid) {
                return Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("status", "error", "message", "Email ou senha inválidos"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            String token = JwtUtil.generate(user.getId(), user.getEmail());

            return Response.ok(Map.of("status", "success", "token", token, "role", user.getRole().name().toLowerCase(), "username", user.getUsername())).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao fazer login: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

    }


}
