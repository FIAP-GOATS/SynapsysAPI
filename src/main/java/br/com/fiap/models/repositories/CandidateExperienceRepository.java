package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.CandidateExperience;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateExperienceRepository {
    private Connection connection;

    public CandidateExperienceRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void registerCandidateExperience(CandidateExperience experience) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "INSERT INTO candidate_experience (candidate_id, company, role, description, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?);"
        );
        stm.setLong(1, experience.getCandidateId());
        stm.setString(2, experience.getCompanyName());
        stm.setString(3, experience.getRole());
        stm.setString(4, experience.getDescription());
        stm.setString(5, experience.getStartDate());
        stm.setString(6, experience.getEndDate());
        stm.executeUpdate();
    }

    public CandidateExperience getCandidateExperienceById(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM candidate_experience WHERE id = ?"
        );
        stm.setInt(1, id);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            CandidateExperience experience = new CandidateExperience();
            experience.setId(result.getInt("id"));
            experience.setCandidateId(result.getLong("candidate_id"));
            experience.setCompanyName(result.getString("company"));
            experience.setRole(result.getString("role"));
            experience.setDescription(result.getString("description"));
            experience.setStartDate(result.getString("start_date"));
            experience.setEndDate(result.getString("end_date"));
            return experience;
        } else {
            throw new SQLException("Experiência do candidato não encontrada");
        }
    }

    public void updateCandidateExperience(CandidateExperience experience) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "UPDATE candidate_experience SET company = ?, role = ?, description = ?, start_date = ?, end_date = ? WHERE id = ?"
        );
        stm.setString(1, experience.getCompanyName());
        stm.setString(2, experience.getRole());
        stm.setString(3, experience.getDescription());
        stm.setString(4, experience.getStartDate());
        stm.setString(5, experience.getEndDate());
        stm.setInt(6, experience.getId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Experiência não encontrada para atualização");
        }
    }

    public void deleteCandidateExperience(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM candidate_experience WHERE id = ?"
        );
        stm.setInt(1, id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Nenhuma experiência encontrada");
        }
    }
}
