package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CandidateRepository {

    private Connection connection;

    public CandidateRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void registerCandidate(Candidate candidato) throws SQLException{
        PreparedStatement stm = connection.prepareStatement("INSERT INTO candidates (user_id, display_name, purpose, work_style, interests, created_at) VALUES (?, ?, ?, ?, ?, ?)");
        stm.setInt(1, candidato.getUserId());
        stm.setString(2, candidato.getDisplayName());
        stm.setString(3, candidato.getPurpose());
        stm.setString(4, candidato.getWorkStyle());
        stm.setString(5, candidato.getInterests());
        stm.setObject(6, candidato.getCreatedAt());
        stm.executeUpdate();
    }

    public List<Candidate> getAllCandidates() throws SQLException{
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM candidates");
        ResultSet result = stm.executeQuery();
        List<Candidate> candidates = new ArrayList<>();
        while (result.next()) {
            Candidate candidate = new Candidate();
            candidate.setUserId(result.getInt("user_id"));
            candidate.setDisplayName(result.getString("display_name"));
            candidate.setPurpose(result.getString("purpose"));
            candidate.setWorkStyle(result.getString("work_style"));
            candidate.setInterests(result.getString("interests"));
            candidate.setCreatedAt(result.getObject("created_at", java.time.LocalDateTime.class));
            candidates.add(candidate);
        }
        return candidates;
    }

    public Candidate getCandidateByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM candidates WHERE user_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);
            ResultSet result = stm.executeQuery();
            if(!result.next()){
                throw  new SQLException("Candidato não encontrado");
            }
            int id = result.getInt("user_id");
            String displayName = result.getString("display_name");
            String purpose = result.getString("purpose");
            String workStyle = result.getString("work_style");
            String interests = result.getString("interests");
            LocalDateTime createdAt = result.getObject("created_at", java.time.LocalDateTime.class);
            return new Candidate(id, displayName, purpose, workStyle, interests);
        }
    }

    public void updateCandidate(Candidate candidate) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE candidates SET display_name = ?, purpose = ?, work_style = ?, interests = ? WHERE user_id = ?");
        stm.setString(1, candidate.getDisplayName());
        stm.setString(2, candidate.getPurpose());
        stm.setString(3, candidate.getWorkStyle());
        stm.setString(4, candidate.getInterests());
        stm.setInt(5, candidate.getUserId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidato não encontrado");
        }
    }

    public void deleteCandidate(int userId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM candidates WHERE user_id = ?");
        stm.setInt(1, userId);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidato não encontrado");
        }
    }

}
