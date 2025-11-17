package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.CandidateEducation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        stm.setLong(1, candidateEducation.getCandidateId());
        stm.setString(2, candidateEducation.getInstitution());
        stm.setString(3, candidateEducation.getCourse());
        stm.setString(4, candidateEducation.getLevel());
        stm.setString(5, candidateEducation.getStartDate());
        stm.setString(6, candidateEducation.getEndDate());
        stm.executeUpdate();
    }

    public CandidateEducation getCandidateEducationById(Long candidateId) throws SQLException{
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM candidate_education WHERE candidate_id = ?");
        stm.setLong(1, candidateId);
        ResultSet result = stm.executeQuery();
        if(result.next()){
            CandidateEducation candidateEducation = new CandidateEducation();
            candidateEducation.setCandidateId(result.getLong("candidate_id"));
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

    public CandidateEducation getCandidateEducationByInstitutionAndCourse(String institution, String course) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM candidate_education WHERE institution = ? AND course = ?");
        stm.setString(1, institution);
        stm.setString(2, course);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            CandidateEducation candidateEducation = new CandidateEducation();
            candidateEducation.setId(result.getInt("id"));
            candidateEducation.setCandidateId(result.getLong("candidate_id"));
            candidateEducation.setInstitution(result.getString("institution"));
            candidateEducation.setCourse(result.getString("course"));
            candidateEducation.setLevel(result.getString("level"));
            candidateEducation.setStartDate(result.getString("start_date"));
            candidateEducation.setEndDate(result.getString("end_date"));
            return candidateEducation;
        } else {
            throw new SQLException("Candidate Education not found for institution: " + institution + " and course: " + course);
        }
    }

    public List<CandidateEducation> getCandidateEducationByUsername(String username) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT ce.* FROM candidate_education ce " +
                        "JOIN candidates c ON ce.candidate_id = c.user_id " +
                        "JOIN users u ON c.user_id = u.id " +
                        "WHERE u.username = ?"
        );
        stm.setString(1, username);
        ResultSet result = stm.executeQuery();
        List<CandidateEducation> educationList = new java.util.ArrayList<>();
        while (result.next()) {
            CandidateEducation candidateEducation = new CandidateEducation();
            candidateEducation.setId(result.getInt("id"));
            candidateEducation.setCandidateId(result.getLong("candidate_id"));
            candidateEducation.setInstitution(result.getString("institution"));
            candidateEducation.setCourse(result.getString("course"));
            candidateEducation.setLevel(result.getString("level"));
            candidateEducation.setStartDate(result.getString("start_date"));
            candidateEducation.setEndDate(result.getString("end_date"));
            educationList.add(candidateEducation);
        }
        return educationList;
    }

    public void updateCandidateEducation(CandidateEducation candidateEducation) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE candidate_education SET institution = ?, course = ?, level = ?, start_date = ?, end_date = ? WHERE candidate_id = ?");
        stm.setString(1, candidateEducation.getInstitution());
        stm.setString(2, candidateEducation.getCourse());
        stm.setString(3, candidateEducation.getLevel());
        stm.setString(4, candidateEducation.getStartDate());
        stm.setString(5, candidateEducation.getEndDate());
        stm.setLong(6, candidateEducation.getCandidateId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidato não encontrado");
        }
    }

    public void deleteCandidateEducation(int educationID) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM candidate_education WHERE id = ?");
        stm.setInt(1, educationID);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No Candidate Education found to delete");
        }
    }
}
