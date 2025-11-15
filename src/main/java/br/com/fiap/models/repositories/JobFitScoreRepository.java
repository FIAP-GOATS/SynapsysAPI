package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.JobFitScore;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JobFitScoreRepository {

    private final Connection connection;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public JobFitScoreRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public JobFitScore create(JobFitScore score) throws SQLException {
        String sql = "INSERT INTO job_fit_scores (job_id, candidate_id, technical_score, cultural_score, total_score, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, score.getJobId());
            stm.setInt(2, score.getCandidateId());
            stm.setDouble(3, score.getTechnicalScore());
            stm.setDouble(4, score.getCulturalScore());
            stm.setDouble(5, score.getTotalScore());
            stm.setString(6, score.getCreatedAt().format(DATE_TIME_FORMATTER));
            stm.executeUpdate();

            ResultSet keys = stm.getGeneratedKeys();
            if (keys.next()) {
                score.setId(keys.getInt(1));
            }
            return score;
        }
    }

    public JobFitScore getById(int id) throws SQLException {
        String sql = "SELECT * FROM job_fit_scores WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            throw new SQLException("JobFitScore não encontrado");
        }
    }

    public JobFitScore getByJobAndCandidate(int jobId, int candidateId) throws SQLException {
        String sql = "SELECT * FROM job_fit_scores WHERE job_id = ? AND candidate_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, jobId);
            stm.setInt(2, candidateId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            throw new SQLException("Score não encontrado");
        }
    }

    public List<JobFitScore> getAll() throws SQLException {
        String sql = "SELECT * FROM job_fit_scores";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            List<JobFitScore> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public List<JobFitScore> getByJobId(int jobId) throws SQLException {
        String sql = "SELECT * FROM job_fit_scores WHERE job_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, jobId);
            ResultSet rs = stm.executeQuery();
            List<JobFitScore> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public List<JobFitScore> getByCandidateId(int candidateId) throws SQLException {
        String sql = "SELECT * FROM job_fit_scores WHERE candidate_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, candidateId);
            ResultSet rs = stm.executeQuery();
            List<JobFitScore> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public void update(JobFitScore score) throws SQLException {
        String sql = "UPDATE job_fit_scores SET job_id = ?, candidate_id = ?, technical_score = ?, cultural_score = ?, total_score = ? WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, score.getJobId());
            stm.setInt(2, score.getCandidateId());
            stm.setDouble(3, score.getTechnicalScore());
            stm.setDouble(4, score.getCulturalScore());
            stm.setDouble(5, score.getTotalScore());
            stm.setInt(6, score.getId());
            int rows = stm.executeUpdate();
            if (rows == 0) {
                throw new SQLException("JobFitScore não encontrado para update");
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM job_fit_scores WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            int rows = stm.executeUpdate();
            if (rows == 0) {
                throw new SQLException("JobFitScore não encontrado para delete");
            }
        }
    }

    private JobFitScore map(ResultSet rs) throws SQLException {
        JobFitScore score = new JobFitScore();
        score.setId(rs.getInt("id"));
        score.setJobId(rs.getInt("job_id"));
        score.setCandidateId(rs.getInt("candidate_id"));
        score.setTechnicalScore(rs.getDouble("technical_score"));
        score.setCulturalScore(rs.getDouble("cultural_score"));
        score.setTotalScore(rs.getDouble("total_score"));
        String created = rs.getString("created_at");
        if (created != null) {
            score.setCreatedAt(LocalDateTime.parse(created, DATE_TIME_FORMATTER));
        }
        return score;
    }
}
