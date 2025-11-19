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
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public JobFitScoreRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public JobFitScore create(JobFitScore score) throws SQLException {
        // Banco tem coluna "score" (única nota total) e "created_at"
        String sql = "INSERT INTO job_fit_scores (job_id, candidate_id, score, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, score.getJobId());
            stm.setInt(2, score.getCandidateId());
            stm.setDouble(3, score.getTotalScore()); // grava totalScore em "score"
            stm.setString(4, score.getCreatedAt().format(DATE_TIME_FORMATTER));
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
        // Mantemos o schema atual do banco: coluna "score" (sem technical_score/cultural_score/total_score)
        String sql = "UPDATE job_fit_scores SET job_id = ?, candidate_id = ?, score = ? WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, score.getJobId());
            stm.setInt(2, score.getCandidateId());
            stm.setDouble(3, score.getTotalScore()); // atualiza o campo "score" com totalScore
            stm.setInt(4, score.getId());
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

    // Helper para checar se a coluna existe no ResultSet
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++) {
            if (columnName.equalsIgnoreCase(meta.getColumnLabel(i))) {
                return true;
            }
        }
        return false;
    }

    private JobFitScore map(ResultSet rs) throws SQLException {
        JobFitScore score = new JobFitScore();

        // Campos que existem no schema atual
        if (hasColumn(rs, "id")) {
            score.setId(rs.getInt("id"));
        }
        if (hasColumn(rs, "job_id")) {
            score.setJobId(rs.getInt("job_id"));
        }
        if (hasColumn(rs, "candidate_id")) {
            score.setCandidateId(rs.getInt("candidate_id"));
        }

        // Total score
        // Prioridade:
        // 1) total_score (se existir)
        // 2) score (coluna atual do banco)
        if (hasColumn(rs, "total_score")) {
            score.setTotalScore(rs.getDouble("total_score"));
        } else if (hasColumn(rs, "score")) {
            score.setTotalScore(rs.getDouble("score"));
        }

        // Campos técnicos/culturais são opcionais: preenche só se existirem
        if (hasColumn(rs, "technical_score")) {
            score.setTechnicalScore(rs.getDouble("technical_score"));
        }
        if (hasColumn(rs, "cultural_score")) {
            score.setCulturalScore(rs.getDouble("cultural_score"));
        }

        // created_at (se existir)
        if (hasColumn(rs, "created_at")) {
            String created = rs.getString("created_at");
            if (created != null) {
                score.setCreatedAt(LocalDateTime.parse(created, DATE_TIME_FORMATTER));
            }
        }

        return score;
    }
}
