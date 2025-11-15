package br.com.fiap.controllers;

import br.com.fiap.authentication.PasswordUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.RegisterDTO;
import br.com.fiap.models.entities.User;
import br.com.fiap.models.enums.Role;
import br.com.fiap.models.repositories.UserRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Map;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    public UserController() throws SQLException {
    }

    UserRepository userRepository = new UserRepository();

    /// Endpoint for registering a new user.
    @POST
    @Path("/register")
    public Response RegisterUser(RegisterDTO registerDTO) throws SQLException{

        /// Admin registration is not allowed. Only by direct database manipulation.
        if(registerDTO.getRole().equals("admin")){
            return Response.status(Response.Status.BAD_REQUEST).entity("Operação inválida").build();
        }

        /// Validate email format.
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!registerDTO.getEmail().matches(emailRegex)) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("status", "error", "error", "E-mail inválido"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        /// Check if email is already registered.
        boolean emailExists = userRepository.checkIfEmailExists(registerDTO.getEmail());

        if (emailExists) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(Map.of("status", "error", "error", "Email já cadastrado"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        /// Validate password length.
        if(registerDTO.getPassword().length() > 12){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("status", "error", "error", "A senha deve conter 12 caracteres ou menos"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        /// Hash the password.
        String hashPassword = PasswordUtil.hashPassword(registerDTO.getPassword());

        /// Determine user role.
        Role userRole = registerDTO.getRole().equals("candidate") ? Role.CANDIDATE : Role.COMPANY;

        /// Create and save the new user.
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(hashPassword);
        user.setRole(userRole);
        userRepository.registerUser(user);

        return Response.status(Response.Status.CREATED).entity("Usuário cadastrado com sucesso").build();
    }

    /// Endpoint for admin to delete a user by ID.
    @DELETE
    @Path("{id}")
    public Response deleteUserById(@HeaderParam("Authorization") String authHeader, @PathParam("id") Long id) throws SQLException {

        // Extract user information from the Authorization header.
        AuthDTO authData = br.com.fiap.authentication.AuthUtil.extractUser(authHeader);

        if(!authData.getRole().equals("admin")){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("status", "error", "error", "Permissão negada"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Delete the user account
        userRepository.deleteUser(id);

        return Response.status(Response.Status.OK)
                .entity(Map.of("status", "success", "message", "Usuário deletado com sucesso"))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }


    ///  Endpoint for the user to delete their own account.
    @DELETE
    @Path("/delete-account")
    public Response deleteAccount(@HeaderParam("Authorization") String authHeader) throws SQLException {

        // Extract user information from the Authorization header.
        AuthDTO authData = br.com.fiap.authentication.AuthUtil.extractUser(authHeader);
        Long userId = authData.getUserId();

        if(authData.getRole().equals("admin")){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("status", "error", "error", "Não é possível apagar a conta de administrador"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Delete the user account
        userRepository.deleteUser(userId);

        return Response.status(Response.Status.OK)
                .entity(Map.of("status", "success", "message", "Conta deletada com sucesso"))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
