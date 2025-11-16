package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.CandidateSkills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidateSkillsRepository {
    private Connection connection;

    public CandidateSkillsRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void addSkilltoCandidate(CandidateSkills skill) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "INSERT INTO candidate_skills (candidate_id, skill_name, level) VALUES (?, ?, ?);"
        );
        stm.setLong(1, skill.getCandidateId());
        stm.setString(2, skill.getSkillName());
        stm.setInt(3, skill.getLevel());
        stm.executeUpdate();
    }

    public CandidateSkills getCandidateSkillById(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM candidate_skills WHERE id = ?"
        );
        stm.setInt(1, id);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            CandidateSkills skill = new CandidateSkills();
            skill.setId(result.getInt("id"));
            skill.setCandidateId(result.getInt("candidate_id"));
            skill.setSkillName(result.getString("skill_name").toLowerCase());
            skill.setLevel(result.getInt("level"));
            return skill;
        } else {
            throw new SQLException("Habilidade do candidato não encontrada");
        }
    }

    public CandidateSkills getCandidateSkillByName(String name, Long candidateId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM candidate_skills WHERE skill_name = ? AND candidate_id = ?"
        );
        stm.setString(1, name.toLowerCase());
        stm.setLong(2, candidateId);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            CandidateSkills skill = new CandidateSkills();
            skill.setId(result.getInt("id"));
            skill.setCandidateId(result.getInt("candidate_id"));
            skill.setSkillName(result.getString("skill_name").toLowerCase());
            skill.setLevel(result.getInt("level"));
            return skill;
        } else {
            throw new SQLException("Habilidade do candidato não encontrada");
        }
    }

    public void updateCandidateSkill(CandidateSkills skill) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "UPDATE candidate_skills SET skill_name = ?, level = ? WHERE id = ?"
        );
        stm.setString(1, skill.getSkillName());
        stm.setInt(2, skill.getLevel());
        stm.setInt(3, skill.getId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Habilidade não encontrada para atualização");
        }
    }



    public List<CandidateSkills> getSkillsByUser(String username) throws SQLException{
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM candidate_skills WHERE candidate_id = (SELECT id FROM users WHERE username = ?)"
        );
        stm.setString(1, username);
        ResultSet result = stm.executeQuery();
        List<CandidateSkills> skills = new ArrayList<>();
        while (result.next()) {
            CandidateSkills skill = new CandidateSkills();
            skill.setId(result.getInt("id"));
            skill.setCandidateId(result.getInt("candidate_id"));
            skill.setSkillName(result.getString("skill_name").toLowerCase());
            skill.setLevel(result.getInt("level"));
            skills.add(skill);
        }
        return skills;
    }

    public void deleteCandidateSkill(int skill_id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM candidate_skills WHERE id = ?"
        );
        stm.setInt(1, skill_id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Nenhuma habilidade encontrada para deletar");
        }
    }

    public Boolean checkIfSkillExists(String skillName, Long candidateId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT COUNT(*) AS count FROM candidate_skills WHERE skill_name = ? AND candidate_id = ?"
        );
        stm.setString(1, skillName.toLowerCase());
        stm.setLong(2, candidateId);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            int count = result.getInt("count");
            return count > 0;
        } else {
            return false;
        }
    }
}
