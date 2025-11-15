package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.User;
import br.com.fiap.models.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private Connection connection;

    public UserRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void registerUser(User user) throws SQLException {
       PreparedStatement stm = connection.prepareStatement("INSERT INTO users (email, password, role) VALUES (?, ?, ?)");
       stm.setString(1, user.getEmail());
       stm.setString(2, user.getPassword());
       stm.setString(3, user.getRole().name().toLowerCase());
       stm.executeUpdate();
    }

    public List<User> getAllUsers() throws SQLException{
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM users");
        ResultSet result = stm.executeQuery();
        List<User> users = new ArrayList<>();
        while (result.next()) {
            User user = new User();
            user.setEmail(result.getString("email"));
            user.setPassword(result.getString("password"));
            user.setRole(Role.fromString(result.getString("role")));;
            users.add(user);
        }
        return users;
    }

    public User getUserById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setLong(1, id);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                User user = new User();
                user.setId(result.getLong("id"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setRole(Role.fromString(result.getString("role")));
                return user;
            } else {
                throw new SQLException("Usuário não encontrado");
            }
        }
    }

    public User getUserByEmail(String email) throws SQLException{
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        stm.setString(1, email);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            User user = new User();
            user.setId(result.getLong("id"));
            user.setEmail(result.getString("email"));
            user.setPassword(result.getString("password"));
            user.setRole(Role.fromString(result.getString("role")));
            return user;
        } else {
            return  null;
        }
    }

    public boolean checkIfEmailExists(String email) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE email = ?");
        stm.setString(1, email);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            int count = result.getInt("count");
            return count > 0;
        }
        return false;
    }

    public void updateUser(User user) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE users SET email = ?, password = ?, role = ? WHERE id = ?");
        stm.setString(1, user.getEmail());
        stm.setString(2, user.getPassword());
        stm.setString(3, user.getRole().name());
        stm.setLong(4, user.getId());
        int rowsAffected = stm.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Usuário não encontrado");
        }
    }

    public void deleteUser(Long id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        stm.setLong(1, id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Usuário não encontrado");
        }
    }

}
