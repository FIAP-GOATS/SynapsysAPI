package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.CandidateEducation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateEducationRepository {
    private Connection connection;

    public CandidateEducationRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }
    public void registerCandidateEducation(CandidateEducation candidateEducation) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO candidate_education (candidate_id, institution, course, level, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?);");
        stm.setInt(1, candidateEducation.getCandidateId());
        stm.setString(2, candidateEducation.getInstitution());
        stm.setString(3, candidateEducation.getCourse());
        stm.setString(4, candidateEducation.getLevel());
        stm.setString(5, candidateEducation.getStartDate());
        stm.setString(6, candidateEducation.getEndDate());
        stm.executeUpdate();
    }

    public CandidateEducation getCandidateEducationById(int candidateId) throws SQLException{
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM candidate_education WHERE candidate_id = ?");
        stm.setInt(1, candidateId);
        ResultSet result = stm.executeQuery();
        if(result.next()){
            CandidateEducation candidateEducation = new CandidateEducation();
            candidateEducation.setCandidateId(result.getInt("candidate_id"));
            candidateEducation.setInstitution(result.getString("institution"));
            candidateEducation.setCourse(result.getString("course"));
            candidateEducation.setLevel(result.getString("level"));
            candidateEducation.setStartDate(result.getString("start_date"));
            candidateEducation.setEndDate(result.getString("end_date"));
            return candidateEducation;
        } else {
            throw new SQLException("Candidate Education not found");
        }
    }
    public void updateCandidateEducation(CandidateEducation candidateEducation) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE candidate_education SET institution = ?, course = ?, level = ?, start_date = ?, end_date = ? WHERE candidate_id = ?");
        stm.setString(1, candidateEducation.getInstitution());
        stm.setString(2, candidateEducation.getCourse());
        stm.setString(3, candidateEducation.getLevel());
        stm.setString(4, candidateEducation.getStartDate());
        stm.setString(5, candidateEducation.getEndDate());
        stm.setInt(6, candidateEducation.getCandidateId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidato não encontrado");
        }
    }

    public void deleteCandidateEducation(int candidateId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM candidate_education WHERE candidate_id = ?");
        stm.setInt(1, candidateId);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No Candidate Education found to delete for candidateId: " + candidateId);
        }
    }
}
