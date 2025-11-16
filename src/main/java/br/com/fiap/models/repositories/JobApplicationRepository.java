package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.JobApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationRepository {
    private Connection connection;

    public JobApplicationRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void createJobApplication(JobApplication jobApplication) throws SQLException {
        String sql = "INSERT INTO job_applications (job_id, candidate_id, status) VALUES (?, ?, ?)";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setInt(1, jobApplication.getJobId());
        stm.setLong(2, jobApplication.getCandidateId());
        stm.setString(3, jobApplication.getStatus());
        stm.executeUpdate();
        stm.close();
    }

    public JobApplication getJobApplicationById(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM job_applications WHERE id = ?");
        stm.setInt(1, id);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            JobApplication jobApplication = new JobApplication();
            jobApplication.setId(result.getInt("id"));
            jobApplication.setJobId(result.getInt("job_id"));
            jobApplication.setCandidateId(result.getLong("candidate_id"));
            jobApplication.setStatus(result.getString("status"));
            return jobApplication;
        } else {
            throw new SQLException("Candidatura não encontrada para o ID: " + id);
        }
    }

    public List<JobApplication> getJobApplicationsByJobId(int jobId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM job_applications WHERE job_id = ?");
        stm.setInt(1, jobId);
        ResultSet result = stm.executeQuery();
        List<JobApplication> jobApplications = new ArrayList<>();
        while (result.next()) {
            JobApplication jobApplication = new JobApplication();
            jobApplication.setId(result.getInt("id"));
            jobApplication.setJobId(result.getInt("job_id"));
            jobApplication.setCandidateId(result.getLong("candidate_id"));
            jobApplication.setStatus(result.getString("status"));
            jobApplications.add(jobApplication);
        }
        return jobApplications;
    }

    public List<JobApplication> getJobApplicationsByCandidateId(int candidateId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM job_applications WHERE candidate_id = ?");
        stm.setInt(1, candidateId);
        ResultSet result = stm.executeQuery();
        List<JobApplication> jobApplications = new ArrayList<>();
        while (result.next()) {
            JobApplication jobApplication = new JobApplication();
            jobApplication.setId(result.getInt("id"));
            jobApplication.setJobId(result.getInt("job_id"));
            jobApplication.setCandidateId(result.getLong("candidate_id"));
            jobApplication.setStatus(result.getString("status"));
            jobApplications.add(jobApplication);
        }
        return jobApplications;
    }

    public List<JobApplication> getAllJobApplications() throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM job_applications");
        ResultSet result = stm.executeQuery();
        List<JobApplication> jobApplications = new ArrayList<>();
        while (result.next()) {
            JobApplication jobApplication = new JobApplication();
            jobApplication.setId(result.getInt("id"));
            jobApplication.setJobId(result.getInt("job_id"));
            jobApplication.setCandidateId(result.getLong("candidate_id"));
            jobApplication.setStatus(result.getString("status"));
            jobApplications.add(jobApplication);
        }
        return jobApplications;
    }

    public void updateJobApplication(JobApplication jobApplication) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE job_applications SET job_id = ?, candidate_id = ?, status = ? WHERE id = ?");
        stm.setInt(1, jobApplication.getJobId());
        stm.setLong(2, jobApplication.getCandidateId());
        stm.setString(3, jobApplication.getStatus());
        stm.setInt(4, jobApplication.getId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidatura não encontrada para atualização, ID: " + jobApplication.getId());
        }
        stm.close();
    }

    public void deleteJobApplication(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM job_applications WHERE id = ?");
        stm.setInt(1, id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidatura não encontrada para exclusão, ID: " + id);
        }
        stm.close();
    }

    public void changeApplicationStatus(int id, String status) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE job_applications SET status = ? WHERE id = ?");
        stm.setString(1, status);
        stm.setInt(2, id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Candidatura não encontrada para alteração de status, ID: " + id);
        }
        stm.close();
    }

}
