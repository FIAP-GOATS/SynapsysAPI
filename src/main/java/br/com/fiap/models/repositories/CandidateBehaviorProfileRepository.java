package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.CandidateBehaviorProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateBehaviorProfileRepository {

    private Connection connection;

    public CandidateBehaviorProfileRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void createCandidateBehaviorProfile(CandidateBehaviorProfile behaviorProfile) throws SQLException{
        PreparedStatement stm = connection.prepareStatement("INSERT INTO CandidateBehaviorProfile (candidateId, aiProfile) VALUES (?, ?)");
        stm.setLong(1, behaviorProfile.getCandidateId());
        stm.setString(2, behaviorProfile.getAiProfile());
        stm.executeUpdate();
    }

    public CandidateBehaviorProfile getCandidateBehaviorProfileByCandidateId(int candidateId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM CandidateBehaviorProfile WHERE candidateId = ?");
        stm.setInt(1, candidateId);
        ResultSet result = stm.executeQuery();
        if(result.next()){
            CandidateBehaviorProfile behaviorProfile = new CandidateBehaviorProfile();
            behaviorProfile.setCandidateId(result.getLong("candidateId"));
            behaviorProfile.setAiProfile(result.getString("aiProfile"));
            return behaviorProfile;
        } else {
            throw new SQLException("Candidate Behavior Profile not found");
        }
    }

    public void updateCandidateBehaviorProfile(CandidateBehaviorProfile behaviorProfile) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE CandidateBehaviorProfile SET aiProfile = ? WHERE candidateId = ?");
        stm.setString(1, behaviorProfile.getAiProfile());
        stm.setLong(2, behaviorProfile.getCandidateId());
        stm.executeUpdate();
    }

    public void deleteCandidateBehaviorProfile(int candidateId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM CandidateBehaviorProfile WHERE candidateId = ?");
        stm.setInt(1, candidateId);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No Candidate Behavior Profile found to delete for candidateId: " + candidateId);
        }
    }

}
